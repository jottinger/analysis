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
    SentimentAnalyzer analyzer = new SentimentAnalyzer();
    @Test
    public void testInstance() {
        System.out.println(analyzer.findSentiment("This is a good thing"));
        System.out.println(analyzer.findSentiment("Hibernate is terrible."));
    }

    @DataProvider
    Object[][] getSubjectData() {
        return new Object[][] {
                {"I'm going to hibernate. Hibernate is good.", "hibernate", true},
                {"You should Hibernate, people.", "hibernate", false},
                {"Hibernate is awesome", "hibernate", true},
                {"To save an object, use Hibernate", "hibernate", true},
                {"Do you know how to Hibernate?", "hibernate", false},
                {"Do you know how to use Hibernate?", "hibernate", true},
                {"can you hibernate?", "hibernate", false},
                {"can you use Hibernate?", "hibernate", true},
        };
    }
    @Test(dataProvider = "getSubjectData")
    public void testHasSubject(String corpus, String noun, boolean result) {
        boolean val=analyzer.hasSubject(corpus, noun);
        assertEquals(val, result);
    }
}
