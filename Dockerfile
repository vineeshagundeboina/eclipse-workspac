FROM openjdk:8
ADD target/fedmobile-sme-core.jar fedmobile-sme-core.jar
EXPOSE 8057
ENTRYPOINT ["java","-jar","fedmobile-sme-core.jar"]