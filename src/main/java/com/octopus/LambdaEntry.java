package com.octopus;

import com.amazonaws.services.lambda.runtime.Context;

public class LambdaEntry {
    public boolean runCucumber(String feature) throws Throwable {
        return true;
    }
}