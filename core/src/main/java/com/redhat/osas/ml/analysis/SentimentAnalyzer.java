package com.redhat.osas.ml.analysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentimentAnalyzer {
    final MaxentTagger tagger;
    final StanfordCoreNLP pipeline;
    final Map<String, String> uppercaseWord = new ConcurrentHashMap<>();

    public SentimentAnalyzer() {
        tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public boolean hasSubject(String corpus, String text) {
        Pattern pattern=Pattern.compile(text, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(corpus);
        if(matcher.find()) {
            String normalizedText = null;
            String normalizedKey = text.toLowerCase();

            if (!uppercaseWord.containsKey(normalizedKey)) {
                uppercaseWord.put(normalizedKey, Character.toUpperCase(text.charAt(0)) + normalizedKey.substring(1));
            }
            normalizedText = uppercaseWord.get(text.toLowerCase());

            corpus=matcher.replaceAll(normalizedText);

            StringReader reader = new StringReader(corpus);
            DocumentPreprocessor preprocessor = new DocumentPreprocessor(reader);
            for (List<? extends HasWord> sentence : preprocessor) {
                // we're looking for text as a noun in the corpus.
                List<TaggedWord> words = tagger.tagSentence(sentence);
                System.out.println(corpus);
                System.out.println(words);
                for (TaggedWord w : words) {
                    if (w.word().equalsIgnoreCase(text) && w.tag().startsWith("NN")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    int findSentiment(String text) {
        int mainSentiment = 0;
        if (text != null && text.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(text);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }
}
