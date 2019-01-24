package com.octopus;

import com.octopus.eventhandlers.EventHandler;
import com.octopus.eventhandlers.impl.SeqLogging;
import org.junit.Test;

import java.util.HashMap;

public class SeqTest {
    private static final EventHandler SEQ_RESULTS = new SeqLogging();

    @Test
    public void testSlackMessage() {
        SEQ_RESULTS.finished(
                "unit test",
                true,
                "feature file",
                "content",
                "",
                new HashMap<String, String>() {{
                    this.put(SeqLogging.SEQ_LEVEL, System.getenv(SeqLogging.SEQ_LEVEL));
                    this.put(SeqLogging.SEQ_API_KEY, System.getenv(SeqLogging.SEQ_API_KEY));
                    this.put(SeqLogging.SEQ_URL, System.getenv(SeqLogging.SEQ_URL));
                    this.put(SeqLogging.SEQ_MESSAGE, "This is a test");
                }},
                new HashMap<>());
    }
}
