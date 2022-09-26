FROM debian:stable

RUN apt-get update && apt-get install -y curl wget xvfb chromium chromium-driver fluxbox xcompmgr
RUN wget https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_linux-x64_bin.tar.gz -O openjdk.tar.gz && \
    tar xzf openjdk.tar.gz && \
    for d in jdk*; do mv $d jdk; done &&  \
    mv jdk /opt/
RUN apt-get install bzip2 libdbus-glib-1-2
RUN wget https://archive.mozilla.org/pub/firefox/releases/105.0/linux-x86_64/en-US/firefox-105.0.tar.bz2 -O firefox.tar.bz2 && \
     tar xjf firefox.tar.bz2 && \
     mv firefox /opt/
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.31.0/geckodriver-v0.31.0-linux64.tar.gz -O geckodriver.tar.gz && \
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
# 1001 is the UID of the Github Action runner user, which allows the image to access the mounted volumes
RUN useradd -u 1001 -ms /bin/bash runner
ADD docker/fluxbox-init /home/runner/.fluxbox/init
RUN chown -R runner:runner /opt
USER runner

CMD ["--help"]
ENTRYPOINT ["/opt/webdriver"]

