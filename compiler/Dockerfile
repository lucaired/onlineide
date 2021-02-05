# runner for the compile project containing gcc

FROM maven:3.6.3-jdk-11

RUN apt-get update && apt-get install -y --no-install-recommends \
		gcc \
       	&& rm -rf /var/lib/apt/lists/*
