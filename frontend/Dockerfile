FROM node:lts-slim

ARG NG_CLI_VERSION=11.1.1
LABEL angular-cli=$NG_CLI_VERSION node=$NODE_VERSION

RUN add-apt-repository ppa:openjdk-r/ppa \
RUN add-apt-repository ppa:openjdk-r/ppa \
  apt-get update && apt-get install -y \
  build-essential \
  git \
  openjdk-11-jdk \
  maven \
  curl

RUN npm install -g @angular/cli@$NG_CLI_VERSION \
RU npm cache clean --force

CMD /bin/bash