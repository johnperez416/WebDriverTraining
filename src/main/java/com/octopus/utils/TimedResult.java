package com.octopus.utils;

public interface TimedResult<T> {
    T getResult();
    long getMillis();
}
