package com.redhat.osas.ml.analysis.sources.twitter;

import com.redhat.osas.ml.analysis.model.SourceData;
import com.redhat.osas.ml.analysis.sources.StreamSource;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class TwitterStreamSource implements StreamSource {
    Properties properties = new Properties();

    public TwitterStreamSource() {
        //properties.setProperty("twitter.consumer.key", System.getenv("TWITTER_OAUTH_CONSUMER_KEY"));
        //properties.setProperty("twitter.consumer.secret", System.getenv("TWITTER_OAUTH_CONSUMER_SECRET"));
        //properties.setProperty("twitter.access.token", System.getenv("TWITTER_OAUTH_ACCESS_TOKEN"));
        //properties.setProperty("twitter.access.token.secret", System.getenv("TWITTER_OAUTH_ACCESS_TOKEN_SECRET"));
        InputStream is = this.getClass().getResourceAsStream("/twitter-auth.properties");
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<SourceData> getData(String... terms) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(properties.getProperty("twitter.consumer.key"))
                .setOAuthConsumerSecret(properties.getProperty("twitter.consumer.secret"))
                .setOAuthAccessToken(properties.getProperty("twitter.access.token"))
                .setOAuthAccessTokenSecret(properties.getProperty("twitter.access.token.secret"));
        TwitterFactory factory = new TwitterFactory(cb.build());
        Twitter twitter = factory.getInstance();
        StringBuilder sb = new StringBuilder(String.join(" ", terms));
        sb.append(" -filter:retweets -filter:links -filter:replies -filter:images");
        Query query = new Query(sb.toString());
        query.setCount(20);
        query.setLocale("en");
        query.setLang("en");
        try {
            QueryResult queryResult = twitter.search(query);
            List<Status> statuses = queryResult.getTweets();
            List<SourceData> returnedList = new ArrayList<>();
            statuses.stream().map(s -> new SourceData(s.getText())).forEach(returnedList::add);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
