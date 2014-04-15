package com.redhat.osas.ml.analysis.sources;

import com.redhat.osas.ml.analysis.model.SourceData;

import java.util.List;

/**
 * User: jottinge
 * Date: 4/15/14
 * Time: 1:20 PM
 */
public interface StreamSource {
    List<SourceData> getData(String ... terms);
}
