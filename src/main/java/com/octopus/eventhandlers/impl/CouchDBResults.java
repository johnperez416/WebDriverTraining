package com.octopus.eventhandlers.impl;

import com.google.gson.Gson;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.eventhandlers.EventHandler;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Map;

public class CouchDBResults implements EventHandler {
    public static final String COUCHDB_URL = "CouchDB-Url";
    public static final String COUCHDB_DOCUMENT = "CouchDB-Document";
    public static final String COUCHDB_DATABASE = "CouchDB-Database";
    public static final String COUCHDB_USERNAME = "CouchDB-Username";
    public static final String COUCHDB_PASSWORD = "CouchDB-Password";
    public static final String COUCHDB_FAILURE_ONLY = "CouchDB-Failure-Only";
    private static final DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String txtOutput,
                                        final String htmlOutputDir,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!headers.containsKey(COUCHDB_URL) ||
                !headers.containsKey(COUCHDB_DATABASE) ||
                !headers.containsKey(COUCHDB_DOCUMENT) ||
                !headers.containsKey(COUCHDB_USERNAME) ||
                !headers.containsKey(COUCHDB_PASSWORD)) {
            System.out.println("The " +
                    COUCHDB_URL + ", " +
                    COUCHDB_DOCUMENT + ", " +
                    COUCHDB_DATABASE + ", " +
                    COUCHDB_USERNAME + " and " +
                    COUCHDB_PASSWORD +
                    " headers must be defined to save the results into in CouchDB");
            return previousResults;
        }

        if (proceed(status, headers, COUCHDB_FAILURE_ONLY)) {


            final CredentialsProvider provider = new BasicCredentialsProvider();
            final UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials(headers.get(COUCHDB_USERNAME), headers.get(COUCHDB_PASSWORD));
            provider.setCredentials(AuthScope.ANY, credentials);

            final String currentRevision = getCurrentRevision(headers, provider);
            saveResults(status, headers, provider, currentRevision);
        }

        return previousResults;
    }

    private void saveResults(final boolean status,
                             final Map<String, String> headers,
                             final CredentialsProvider provider,
                             final String currentRevision) {
        final String result = status ? df.format(AutomatedBrowserBase.getStaticAverageWaitTime() / 1000) : "";

        try (final CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(provider).build()) {
            final HttpGet httpGet = new HttpGet(headers.get(COUCHDB_URL) + "/" +
                    headers.get(COUCHDB_DATABASE) + "/" +
                    headers.get(COUCHDB_DOCUMENT));

            try (final CloseableHttpResponse response = client.execute(httpGet)) {
                if (!(response.getStatusLine().getStatusCode() == 200)) {
                    throw new Exception("Failed to post to CouchDB");
                }
            }

            final HttpPut httpPut = new HttpPut(headers.get(COUCHDB_URL) + "/" +
                    headers.get(COUCHDB_DATABASE) + "/" +
                    headers.get(COUCHDB_DOCUMENT));
            httpPut.setHeader("Content-Type", "application/json");
            final String body = "{\"_rev\": \"" + currentRevision + "\", \"Average\": " + result + ", \"Executed\":" + Instant.now().getEpochSecond() + "}";
            httpPut.setEntity(new StringEntity(body));
            try (final CloseableHttpResponse response = client.execute(httpPut)) {
                if (!(response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201)) {
                    throw new Exception("Failed to post to CouchDB");
                }
            }
        } catch (final Exception ex) {
            System.out.println("Failed to send result to CouchDB.");
        }
    }

    private String getCurrentRevision(final Map<String, String> headers, final CredentialsProvider provider) {
        try (final CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(provider).build()) {
            final HttpGet httpGet = new HttpGet(headers.get(COUCHDB_URL) + "/" +
                    headers.get(COUCHDB_DATABASE) + "/" +
                    headers.get(COUCHDB_DOCUMENT));

            try (final CloseableHttpResponse response = client.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    return (new Gson()).fromJson(EntityUtils.toString(response.getEntity()), CouchDBDocument.class)._rev;
                }
            }
        } catch (final Exception ex) {
            // ignored
        }

        return null;
    }
}

class CouchDBDocument {
    String _rev;
}
