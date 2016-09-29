FROM anapsix/alpine-java:8_jdk_unlimited

MAINTAINER venkat@nikhu.com

# Environment with default values
ENV APP_NAME shopping-cart-akka-http-microservice-assembly
ENV APP_VERSION 1.0
ENV APP_ARTIFACT_NAME $APP_NAME-$APP_VERSION.jar

RUN mkdir /opt/$APP_NAME-$APP_VERSION

# Copy application artificats to docker container
ADD target/scala-2.11/$APP_ARTIFACT_NAME /opt/$APP_NAME-$APP_VERSION
WORKDIR /opt/$APP_NAME-$APP_VERSION

EXPOSE 8080

# Run application
CMD ["java", "-jar", "/opt/shopping-cart-akka-http-microservice-assembly-1.0/shopping-cart-akka-http-microservice-assembly-1.0.jar"]


#
# docker build -t nikhu/shopping-cart-microservice:1.0 .
#
# docker run -itd -p 8080:8080 --name shopping-cart-microservice nikhu/shopping-cart-microservice:1.0
#
# docker exec -it shopping-cart-microservice /bin/bash
#
# docker rm $(docker stop $(docker ps -a -q --filter ancestor=nikhu/shopping-cart-microservice:1.0 --format="{{.ID}}"))
