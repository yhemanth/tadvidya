FROM openjdk:8-jdk
ARG version=1.0-SNAPSHOT
ENV EXTERNAL_JDBC_URL "jdbc:postgresql://localhost:5432/tadvidya?currentSchema=tadvidya"
COPY tadvidya-${version}.zip /opt/tadvidya/
RUN unzip /opt/tadvidya/tadvidya-${version}.zip -d /opt/tadvidya/
WORKDIR /opt/tadvidya/tadvidya-${version}
EXPOSE 9000
CMD ["bin/tadvidya", "-Dplay.http.secret.key=KJPO9FHA2AWG9T8IQUH4ZVSV6QXXEGH6"]