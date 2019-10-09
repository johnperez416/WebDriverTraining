package com.octopus.utils;

import org.springframework.retry.support.RetryTemplate;

public interface RetryService {
    RetryTemplate getTemplate(int retries, int backoff);
    default RetryTemplate getTemplate() {return getTemplate(3, 100);}
    default RetryTemplate getTemplate(int retries) {return getTemplate(retries, 100);}
}
