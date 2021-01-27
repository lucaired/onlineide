FROM ubuntu:18.04

RUN apt-get update && apt-get install -y \
  build-essential \
  git \
  openjdk-11-jdk \
  maven \
  curl

RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g @angular/cli

CMD /bin/bash