package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.SaveException;
import com.octopus.utils.OSUtils;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.OSUtilsImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.regex.Pattern;

public class BrowserMobDecorator extends AutomatedBrowserBase {

    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final OSUtils OS_UTILS = new OSUtilsImpl();

    private BrowserMobProxy proxy;

    public BrowserMobDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        proxy = new BrowserMobProxyServer();
        proxy.start(0);

        final DesiredCapabilities desiredCapabilities =
                getAutomatedBrowser().getDesiredCapabilities();

        final Proxy seleniumProxy = new Proxy();
        final String proxyStr = "localhost:" + proxy.getPort();

        seleniumProxy.setHttpProxy(proxyStr);
        seleniumProxy.setSslProxy(proxyStr);

        if (StringUtils.isNotBlank(SYSTEM_PROPERTY_UTILS.getProperty(Constants.NO_PROXY_LIST))) {
            seleniumProxy.setNoProxy(SYSTEM_PROPERTY_UTILS.getProperty(Constants.NO_PROXY_LIST));
        }

        desiredCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        return desiredCapabilities;
    }

    @Override
    public void destroy() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().destroy();
        }

        if (proxy != null) {
            proxy.stop();
        }
    }

    @Override
    public void captureHarFile() {
        proxy.newHar();
    }

    @Override
    public void captureCompleteHarFile() {
        final EnumSet<CaptureType> captureTypes =
                CaptureType.getAllContentCaptureTypes();
        captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
        captureTypes.addAll(CaptureType.getCookieCaptureTypes());
        proxy.setHarCaptureTypes(captureTypes);
        proxy.newHar();
    }

    @Override
    public void saveHarFile(final String file) {
        try {
            proxy.getHar().writeTo(new File(OS_UTILS.fixFileName(file)));
        } catch (final IOException ex) {
            throw new SaveException(ex);
        }
    }

    @Override
    public void blockRequestTo(String url, int responseCode) {
        proxy.addRequestFilter((request, contents, messageInfo) -> {
            if (Pattern.compile(url).matcher(messageInfo.getOriginalUrl()).matches()) {
                final HttpResponse response = new DefaultHttpResponse(
                        request.getProtocolVersion(),
                        HttpResponseStatus.valueOf(responseCode));

                response.headers().add(HttpHeaders.CONNECTION, "Close");

                return response;
            }

            return null;

        });

        getAutomatedBrowser().blockRequestTo(url, responseCode);
    }

    @Override
    public void alterResponseFrom(final String url, final int responseCode, final String responseBody) {
        proxy.addResponseFilter((response, contents, messageInfo) -> {
            if (Pattern.compile(url).matcher(messageInfo.getOriginalUrl()).matches()) {
                contents.setTextContents(responseBody);
                response.setStatus(HttpResponseStatus.valueOf(responseCode));
            }
        });

        getAutomatedBrowser().alterResponseFrom(url, responseCode, responseBody);
    }
}