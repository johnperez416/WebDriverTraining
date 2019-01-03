package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import io.vavr.control.Try;

import java.net.InetAddress;
import java.net.URL;

public class NetworkDebuggingDecorator extends AutomatedBrowserBase {

    public NetworkDebuggingDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public void goTo(final String url) {
        Try.run(() -> System.out.println("IP Address for " + url + " is " +
                InetAddress.getByName(new URL(url).getHost()).getHostAddress()));
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().goTo(url);
        }
    }
}
