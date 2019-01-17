package com.octopus.eventhandlers.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.eventhandlers.EventHandler;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

public class SaveKubernetesConfigMap implements EventHandler {
    public static final String KUBERNETES_URL = "Kubernetes-Url";
    public static final String KUBERNETES_TOKEN = "Kubernetes-Token";
    public static final String KUBERNETES_NAMESPACE = "Kubernetes-Namespace";
    public static final String KUBERNETES_CONFIGMAP = "Kubernetes-ConfigMap";
    public static final String KUBERNETES_CONFIGMAP_AVG = "Kubernetes-ConfigMapAvgKey";
    public static final String KUBERNETES_CONFIGMAP_AVG_MAX = "Kubernetes-ConfigMapAvgMax";
    public static final String KUBERNETES_CONFIGMAP_AVG_MIN = "Kubernetes-ConfigMapAvgMin";
    public static final String KUBERNETES_CONFIGMAP_EXE = "Kubernetes-ConfigMapExeKey";
    public static final String KUBERNETES_CONFIGMAP_EXE_PERIOD = "Kubernetes-ConfigMapExePeriod";
    private static final DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String txtOutput,
                                        final String htmlOutputDir,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!headers.containsKey(KUBERNETES_URL) ||
                !headers.containsKey(KUBERNETES_TOKEN) ||
                !headers.containsKey(KUBERNETES_NAMESPACE) ||
                !headers.containsKey(KUBERNETES_CONFIGMAP_AVG) ||
                !headers.containsKey(KUBERNETES_CONFIGMAP_EXE) ||
                !headers.containsKey(KUBERNETES_CONFIGMAP)) {
            System.out.println("The " +
                    KUBERNETES_URL + ", " +
                    KUBERNETES_TOKEN + ", " +
                    KUBERNETES_CONFIGMAP_AVG + ", " +
                    KUBERNETES_CONFIGMAP_EXE + ", " +
                    KUBERNETES_NAMESPACE + " and " +
                    KUBERNETES_CONFIGMAP +
                    " headers must be defined to save the results into a config map");
            return previousResults;
        }

        System.out.println(
                "Attempting to update configmap " + headers.get(KUBERNETES_CONFIGMAP) +
                        " in namespace " + headers.get(KUBERNETES_NAMESPACE) +
                        " in cluster " + headers.get(KUBERNETES_URL));

        final ApiClient client = Config.fromToken(
                headers.get(KUBERNETES_URL),
                headers.get(KUBERNETES_TOKEN),
                false);
        Configuration.setDefaultApiClient(client);

        final String result = status ? df.format(AutomatedBrowserBase.getStaticAverageWaitTime() / 1000) : "";
        final String averageTime = "{\"op\":\"add\",\"path\":\"/data/" + headers.get(KUBERNETES_CONFIGMAP_AVG) + "\"," +
                "\"value\":\"" + result + "\"}";
        applyPatch(averageTime, headers);

        if (headers.get(KUBERNETES_CONFIGMAP_AVG_MAX) != null && headers.get(KUBERNETES_CONFIGMAP_AVG_MIN) != null) {
            final String avgMeta = "{\"op\":\"add\",\"path\":\"/data/" + headers.get(KUBERNETES_CONFIGMAP_AVG) + ".meta\"," +
                    "\"value\":\"{\\\"max\\\":" + headers.get(KUBERNETES_CONFIGMAP_AVG_MAX) +
                    ",\\\"min\\\":" + headers.get(KUBERNETES_CONFIGMAP_AVG_MIN) +
                    ",\\\"preference\\\":\\\"small\\\"}\"}";
            applyPatch(avgMeta, headers);
        }

        final String testTime = "{\"op\":\"add\",\"path\":\"/data/" + headers.get(KUBERNETES_CONFIGMAP_EXE) + "\"," +
                "\"value\":\"" + Instant.now().getEpochSecond() + "\"}";
        applyPatch(testTime, headers);

        if (headers.get(KUBERNETES_CONFIGMAP_EXE_PERIOD) != null) {
            final String exeMeta = "{\"op\":\"add\",\"path\":\"/data/" + headers.get(KUBERNETES_CONFIGMAP_EXE) + ".meta\"," +
                    "\"value\":\"{\\\"maxrel\\\":0,\\\"minrel\\\":-" + headers.get(KUBERNETES_CONFIGMAP_EXE_PERIOD) +
                    ",\\\"preference\\\":\\\"large\\\"}\"}";
            applyPatch(exeMeta, headers);
        }

        return previousResults;
    }

    private void applyPatch(final String json, final Map<String, String> headers) {
        try {
            final CoreV1Api api = new CoreV1Api();

            final ArrayList<JsonObject> arr = new ArrayList<>();
            arr.add(((JsonElement) deserialize(
                    json,
                    JsonElement.class)).getAsJsonObject());

            api.patchNamespacedConfigMap(
                    headers.get(KUBERNETES_CONFIGMAP),
                    headers.get(KUBERNETES_NAMESPACE),
                    arr,
                    "false"
            );
        } catch (final Exception ex) {
            System.out.println("Failed to send result to Kubernetes.\n" + ex.toString());
        }
    }

    public Object deserialize(final String jsonStr, final Class<?> targetClass) {
        final Object obj = (new Gson()).fromJson(jsonStr, targetClass);
        return obj;
    }

}
