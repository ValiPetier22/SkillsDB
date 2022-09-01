FROM openjdk:11
WORKDIR usr/src
ADD ./target/jobseekerservice-0.0.1-SNAPSHOT.jar  /usr/src/jobseekerservice-0.0.1-SNAPSHOT.jar


ENTRYPOINT ["java","-jar","jobseekerservice-0.0.1-SNAPSHOT.jar"]