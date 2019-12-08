FROM debian:stable

RUN apt-get update && apt-get install -y curl wget xvfb chromium chromium-driver fluxbox xcompmgr
RUN wget https://download.java.net/java/GA/jdk13.0.1/cec27d702aa74d5a8630c65ae61e4305/9/GPL/openjdk-13.0.1_linux-x64_bin.tar.gz -O openjdk.tar.gz && \
    tar xzf openjdk.tar.gz && \
    for d in jdk*; do mv $d jdk; done &&  \
    mv jdk /opt/
RUN apt-get install bzip2 libdbus-glib-1-2
RUN wget https://download-installer.cdn.mozilla.net/pub/firefox/releases/71.0/linux-x86_64/en-US/firefox-71.0.tar.bz2 -O firefox.tar.bz2 && \
     tar xjf firefox.tar.bz2 && \
     mv firefox /opt/
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.26.0/geckodriver-v0.26.0-linux64.tar.gz -O geckodriver.tar.gz && \
    tar xzf geckodriver.tar.gz && \
    mv geckodriver /usr/bin/geckodriver

ADD docker/fluxbox-init /root/.fluxbox/init
ADD docker/webdriver /opt/webdriver
RUN chmod +x /opt/webdriver

ADD docker/chromium-wrapper /usr/bin/chromium-wrapper
RUN chmod +x /usr/bin/chromium-wrapper
RUN ln -s /usr/bin/chromium-wrapper /usr/bin/google-chrome
RUN ln -s /usr/bin/chromium-wrapper /usr/bin/chromium-browser

COPY target/webdrivertraining.*.jar /opt/webdriver.jar

# Firefox can't run as root, otherwise you'll get the error:
# Running Firefox as root in a regular user's session is not supported.
RUN useradd -u 1001 -ms /bin/bash runner
ADD docker/fluxbox-init /home/webdriver/.fluxbox/init
RUN chown -R runner:runner /opt
USER runner

ENTRYPOINT ["/opt/webdriver"]

