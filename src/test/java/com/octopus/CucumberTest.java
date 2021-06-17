package com.octopus;

import com.kevinmost.junit_retry_rule.Retry;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "com.octopus.decoratorbase")
@Retry
@Ignore
public class CucumberTest {

}