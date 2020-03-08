package com.octopus.utils;

import org.springframework.retry.support.RetryTemplate;

public interface RetryService {
    /**
     * @param retries Number of time to retry.
     * @param backoff Time to wait before a retry.
     * @return A RetryTemplate with the supplied values.
     */
    RetryTemplate getTemplate(int retries, int backoff);

    /**
     * @return A default RetryTemplate.
     */
    default RetryTemplate getTemplate() {
        return getTemplate(3, 100);
    }

    /**
     * @param retries The number of retries.
     * @return A RetryTemplate with the supplied values.
     */
    default RetryTemplate getTemplate(int retries) {
        return getTemplate(retries, 100);
    }
}
