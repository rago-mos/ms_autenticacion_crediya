FROM gradle:8.14-jdk21 AS temp_build_gradle

ENV INSTALL_DIR=/opt/app
WORKDIR $INSTALL_DIR
COPY . $INSTALL_DIR
RUN gradle :app-service:clean -x test
RUN gradle :app-service:bootJar -x test

FROM gradle:8.14-jdk21
ENV INSTALL_DIR=/opt/app
ARG USERNAME=user
ARG USER_UID=999
ARG USER_GID=$USER_UID
WORKDIR $INSTALL_DIR
COPY --from=temp_build_gradle /opt/app/applications/app-service/build/libs/*.jar /opt/app/app.jar
COPY --from=temp_build_gradle /opt/app/applications/app-service/src/main/resources/application.yaml $INSTALL_DIR/application.yaml
RUN groupadd --gid "$USER_GID" "$USERNAME" \
    && useradd --uid "$USER_UID" --gid "$USER_GID" -m "$USERNAME" \
    && chown -R "$USER_UID":"$USER_GID" "$INSTALL_DIR"
USER user
CMD ["java","-jar","app.jar","--spring.config.location=file:application.yaml"]
