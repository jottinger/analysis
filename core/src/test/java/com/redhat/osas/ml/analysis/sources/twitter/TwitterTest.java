package com.redhat.osas.ml.analysis.sources.twitter;

import com.redhat.osas.ml.analysis.model.SourceData;
import com.redhat.osas.ml.analysis.sources.StreamSource;
import org.testng.annotations.Test;

import java.util.List;

public class TwitterTest {
    @Test
    public void testTwitterSearch() {
        StreamSource source=new TwitterStreamSource();
        List<SourceData> data=source.getData("hibernate");
        System.out.println(data);
    }
}
