FROM gradle:8.14-jdk21 AS temp_build_gradle

ENV INSTALL_DIR=/opt/app
WORKDIR $INSTALL_DIR
COPY . $INSTALL_DIR
RUN gradle :app-service:bootJar -x test

FROM eclipse-temurin:21-jre AS runtime
ENV INSTALL_DIR=/opt/app
ARG USERNAME=user
ARG USER_UID=999
ARG USER_GID=$USER_UID
WORKDIR $INSTALL_DIR
COPY --from=temp_build_gradle /opt/app/applications/app-service/build/libs/*.jar /opt/app/app.jar
RUN groupadd --gid "$USER_GID" "$USERNAME" \
    && useradd --uid "$USER_UID" --gid "$USER_GID" -m "$USERNAME" \
    && chown -R "$USER_UID":"$USER_GID" "$INSTALL_DIR"
USER user
CMD ["java","-jar","app.jar"]
