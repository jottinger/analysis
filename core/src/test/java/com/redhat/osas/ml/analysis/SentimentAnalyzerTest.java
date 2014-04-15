package com.redhat.osas.ml.analysis;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * User: jottinge
 * Date: 4/15/14
 * Time: 5:30 PM
 */
public class SentimentAnalyzerTest {
    @Test
    public void testInstance() {
        SentimentAnalyzer analyzer=new SentimentAnalyzer();
        System.out.println(analyzer.findSentiment("This is a good thing"));
        System.out.println(analyzer.findSentiment("Hibernate is terrible."));
    }

    @DataProvider
    Object[][] getSubjectData() {
        return new Object[][] {
                {"I'm going to hibernate. Hibernate is good.", "hibernate", true},
                {"You should hibernate, people.", "hibernate", false},
                {"hibernate is awesome", "hibernate", true},
                {"To save an object, use hibernate", "hibernate", true},
        };
    }
    @Test(dataProvider = "getSubjectData")
    public void testHasSubject(String corpus, String noun, boolean result) {
        SentimentAnalyzer analyzer = new SentimentAnalyzer();
        boolean val=analyzer.hasSubject(corpus, noun);
        assertEquals(val, result);
    }
}
