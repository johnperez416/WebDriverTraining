package com.octopus.utils.impl;

import com.octopus.utils.TimedResult;

public class TimedResultImpl<T> implements TimedResult<T> {

    private T result;
    private long millis;

    public TimedResultImpl(final T result, final long millis) {
        this.result = result;
        this.millis = millis;
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public long getMillis() {
        return millis;
    }
}
