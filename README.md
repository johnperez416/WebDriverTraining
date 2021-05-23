This project contains the source code that accompanies the blog series at https://octopus.com/blog/selenium/0-toc/webdriver-toc.

The Docker image include Chromium, and can be run like this:

```
docker run \
  -v $(pwd)/test.feature:/tmp/test.feature \
  -v /tmp/screenshot:/tmp/screenshot \
  mcasperson/webdriver:<tag> \
  /tmp/test.feature
```