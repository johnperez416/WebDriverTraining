package com.octopus;

import com.octopus.eventhandlers.EventHandler;
import com.octopus.eventhandlers.impl.SaveKubernetesConfigMap;
import org.junit.Test;

import java.util.HashMap;

public class KubernetesTest {
    private static final EventHandler SAVE_KUBERNETES_CONFIGMAP = new SaveKubernetesConfigMap();

    @Test
    public void sendResultsToK8s() {
        SAVE_KUBERNETES_CONFIGMAP.finished(
                "unit test",
                true,
                "feature file",
                "content",
                new HashMap<String, String>() {{
                    this.put(SaveKubernetesConfigMap.KUBERNETES_CONFIGMAP, "kov-metadata");
                    this.put(SaveKubernetesConfigMap.KUBERNETES_NAMESPACE, "default");
                    this.put(SaveKubernetesConfigMap.KUBERNETES_TOKEN, System.getenv("k8stoken"));
                    this.put(SaveKubernetesConfigMap.KUBERNETES_URL, System.getenv("k8surl"));
                }});
    }
}
