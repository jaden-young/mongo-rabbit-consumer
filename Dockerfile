FROM openjdk:8-jre
VOLUME /tmp
ARG BASE_NAME
ARG VERSION

# Docker extracts archies with ADD command
ADD ${BASE_NAME}-${VERSION}.tar /app
WORKDIR /app/${BASE_NAME}-${VERSION}/bin

ENV MONGO_URL=""
ENV MONGO_DB=""
ENV MONGO_COLLECTION=""
ENV RABBIT_HOST=""

# Would be nice if we could just CMD ${BASE_NAME}
CMD ["./mongo-rabbit-consumer"]
