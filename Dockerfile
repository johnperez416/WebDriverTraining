FROM debian:stable

RUN apt-get update && apt-get install -y curl wget xvfb chromium chromium-driver iceweasel
RUN wget https://download.java.net/java/GA/jdk13.0.1/cec27d702aa74d5a8630c65ae61e4305/9/GPL/openjdk-13.0.1_linux-x64_bin.tar.gz -O openjdk.tar.gz && \
    tar xzf openjdk.tar.gz && \
    for d in jdk*; do mv $d jdk; done &&  \
    mv jdk /opt/

ADD xvfb-chromium /usr/bin/xvfb-chromium
RUN chmod +x /usr/bin/xvfb-chromium
RUN ln -s /usr/bin/xvfb-chromium /usr/bin/google-chrome
RUN ln -s /usr/bin/xvfb-chromium /usr/bin/chromium-browser

COPY target/webdrivertraining.1.0-SNAPSHOT.jar /opt/webdrivertraining.jar
ENTRYPOINT ["/opt/jdk/bin/java", "--enable-preview", "-jar", "/opt/webdrivertraining.jar"]