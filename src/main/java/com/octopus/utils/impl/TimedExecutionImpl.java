package com.octopus.utils.impl;

import com.octopus.utils.TimedExecution;
import com.octopus.utils.TimedResult;

import java.util.concurrent.Callable;

public class TimedExecutionImpl<T> implements TimedExecution<T> {
    @Override
    public TimedResult<T> timedExecution(final Callable<T> callable) {
        try {
            final long start = System.currentTimeMillis();
            final T result = callable.call();
            final long finish = System.currentTimeMillis();
            return new TimedResultImpl(result, finish - start);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
