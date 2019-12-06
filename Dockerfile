FROM debian:stable

RUN apt-get update && apt-get install -y curl wget xvfb chromium chromium-driver iceweasel fluxbox xcompmgr
RUN wget https://download.java.net/java/GA/jdk13.0.1/cec27d702aa74d5a8630c65ae61e4305/9/GPL/openjdk-13.0.1_linux-x64_bin.tar.gz -O openjdk.tar.gz && \
    tar xzf openjdk.tar.gz && \
    for d in jdk*; do mv $d jdk; done &&  \
    mv jdk /opt/
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.26.0/geckodriver-v0.26.0-linux64.tar.gz -O geckodriver.tar.gz && \
    tar xzf geckodriver.tar.gz && \
    mv geckodriver /usr/bin

ADD docker/fluxbox-init /root/.fluxbox/init
ADD docker/xvfb-chromium /usr/bin/xvfb-chromium
RUN chmod +x /usr/bin/xvfb-chromium
RUN ln -s /usr/bin/xvfb-chromium /usr/bin/google-chrome
RUN ln -s /usr/bin/xvfb-chromium /usr/bin/chromium-browser

COPY target/webdrivertraining.*.jar /opt/webdriver.jar
ENTRYPOINT ["/opt/jdk/bin/java", "--enable-preview", "-jar", "/opt/webdriver.jar"]