package com.octopus.utils;

import java.util.concurrent.Callable;

public interface TimedExecution<T> {
    TimedResult<T> timedExecution(final Callable<T> callable) throws Exception;
}
