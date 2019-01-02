package com.octopus;

import com.octopus.eventhandlers.EventHandler;
import com.octopus.eventhandlers.impl.SlackWebHook;
import org.junit.Test;

import java.util.HashMap;

public class SlackTest {
    private static final EventHandler SLACK_RESULTS = new SlackWebHook();

    @Test
    public void testSlackMessage() {
        SLACK_RESULTS.finished(
                "unit test",
                true,
                "feature file",
                "content",
                "",
                new HashMap<String, String>() {{
                    this.put("Hook-Url", System.getenv("HookUrl"));
                }},
                new HashMap<>());
    }
}
