package com.octopus.utils;

import org.springframework.retry.support.RetryTemplate;

public interface RetryService {
    RetryTemplate getTemplate(int retries, int backoff);
}
