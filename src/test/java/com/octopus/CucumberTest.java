package com.octopus;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "com.octopus.decoratorbase")
@Ignore("Ticketmonster has been shut down")
public class CucumberTest {

}