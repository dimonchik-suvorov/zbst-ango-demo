FROM ysihaoy/scala-play:2.12.2-2.6.0-sbt-0.13.15

# caching dependencies
COPY ["build.sbt", "/tmp/build/"]
COPY ["project/plugins.sbt", "project/build.properties", "/tmp/build/project/"]
RUN cd /tmp/build && \
  sbt compile && \
  rm -rf /tmp/build

# copy code
COPY . /root/app/
WORKDIR /root/app
RUN sbt compile
RUN sbt stage

EXPOSE 9000
CMD ["./target/universal/stage/bin/insidershomework", "-Dplay.http.secret.key='dontcare'"]