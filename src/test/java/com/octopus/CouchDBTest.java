package com.octopus;

import com.octopus.eventhandlers.EventHandler;
import com.octopus.eventhandlers.impl.CouchDBResults;
import org.junit.Test;

import java.util.HashMap;

public class CouchDBTest {
    private static final EventHandler COUCHDB = new CouchDBResults();

    @Test
    public void sendResultsToK8s() {
        COUCHDB.finished(
                "unit test",
                true,
                "feature file",
                "content",
                "",
                new HashMap<String, String>() {{
                    this.put(CouchDBResults.COUCHDB_DATABASE, System.getenv("couchdatabase"));
                    this.put(CouchDBResults.COUCHDB_DOCUMENT, System.getenv("couchdocument"));
                    this.put(CouchDBResults.COUCHDB_PASSWORD, System.getenv("couchpassword"));
                    this.put(CouchDBResults.COUCHDB_USERNAME, System.getenv("couchusername"));
                    this.put(CouchDBResults.COUCHDB_URL, System.getenv("couchurl"));
                }},
                new HashMap<>());
    }
}
