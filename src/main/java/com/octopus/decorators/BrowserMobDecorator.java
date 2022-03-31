package com.octopus.decorators;

import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarLog;
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
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHeaders;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * A decorator to enable the BrowserMob proxy and configure WebDriver to send traffic through it.
 */
public class BrowserMobDecorator extends AutomatedBrowserBase {

    /**
     * The shared SystemPropertyUtilsImpl instance.
     */
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    /**
     * The shared OSUtilsImpl instance.
     */
    private static final OSUtils OS_UTILS = new OSUtilsImpl();
    /**
     * The start of the error response code range.
     */
    private static final int HTTP_START_ERROR = 400;
    /**
     * The end of the error response code range.
     */
    private static final int HTTP_END_ERROR = 599;

    /**
     * The BrowserMob proxy instance.
     */
    private BrowserUpProxyServer proxy;

    final Proxy seleniumProxy;

    /**
     * Decorator constructor.
     *
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public BrowserMobDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        proxy = new BrowserUpProxyServer();
        proxy.setTrustAllServers(true);
        proxy.setUseEcc(true);
        proxy.start(0);

        seleniumProxy = new Proxy();
        final String proxyStr = "localhost:" + proxy.getPort();

        seleniumProxy.setHttpProxy(proxyStr);
        seleniumProxy.setSslProxy(proxyStr);

        if (StringUtils.isNotBlank(SYSTEM_PROPERTY_UTILS.getProperty(Constants.NO_PROXY_LIST))) {
            seleniumProxy.setNoProxy(SYSTEM_PROPERTY_UTILS.getProperty(Constants.NO_PROXY_LIST));
        }
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        final DesiredCapabilities desiredCapabilities =
            getAutomatedBrowser().getDesiredCapabilities();

        desiredCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        return desiredCapabilities;
    }

    @Override
    public FirefoxOptions getFirefoxOptions() {
        final FirefoxOptions options = new FirefoxOptions();
        options.setCapability(CapabilityType.PROXY, seleniumProxy);
        return options;
    }

    @Override
    public void destroy() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().destroy();
        }

        if (proxy != null) {
            if (proxy.getHar() != null) {
                proxy.endHar();
            }
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
    public void blockRequestTo(final String url, final int responseCode) {
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

    @Override
    public List<Pair<String, Integer>> getErrors() {
        return Optional.ofNullable(proxy.getHar())
                .map(Har::getLog)
                .map(HarLog::getEntries)
                .orElse(List.of())
                .stream()
                .filter(s -> Range.between(HTTP_START_ERROR, HTTP_END_ERROR).contains(s.getResponse().getStatus()))
                .map(s -> Pair.of(s.getRequest().getUrl(), s.getResponse().getStatus()))
                .collect(Collectors.toList());
    }
}