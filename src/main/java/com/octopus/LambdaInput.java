package com.octopus;

import java.util.Map;

/**
 * A class representing the input required when the app is launched from AWS Lambda.
 */
public class LambdaInput {
    private String id;
    private String feature;
    private Map<String, String> headers;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }
}