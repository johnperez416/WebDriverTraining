FROM ubuntu:latest

RUN apt-get update && apt-get install -y wget unzip default-jre firefox fonts-liberation libappindicator3-1 libnspr4 libnss3 libxss1 xdg-utils
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb; dpkg -i google-chrome-stable_current_amd64.deb
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.23.0/geckodriver-v0.23.0-linux64.tar.gz; \
    tar -xzf geckodriver-v0.23.0-linux64.tar.gz \
    cp geckodriver /usr/bin; \
    rm geckodriver-v0.23.0-linux64.tar.gz
RUN wget https://chromedriver.storage.googleapis.com/2.42/chromedriver_linux64.zip; \
    unzip chromedriver_linux64.zip; \
    cp chromedriver /usr/bin; \
    rm chromedriver_linux64.zip
RUN rm -rf /var/lib/apt/lists/*
COPY target/webdrivertraining.1.0-SNAPSHOT.jar /opt/ticketmonster.jar
CMD java -jar /opt/ticketmonster.jar /opt/test.feature