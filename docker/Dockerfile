# syntax=docker/dockerfile:1
#
# Codex Naturalis — minimal multi-arch image (linux/amd64 + linux/arm64).
# Build from the repo root:
#   docker build -f docker/Dockerfile -t codex-naturalis .
#
# Stages:
#   app-amd64  -> reuses the delivered fat jar (its JavaFX natives are linux x86-64 only)
#   app-arm64  -> builds from source; Maven picks the linux-aarch64 JavaFX natives
#   jre        -> jlink runtime with only the JDK modules the app needs (~50 MB)
#   assets     -> noVNC, websockify and the Selawik font from official releases (pinned sha256)
#   runtime    -> debian:trixie-slim + GTK3/X11 libs + Xvfb/x11vnc + gum + tini

ARG TARGETARCH

# ---------------------------------------------------------------- app: amd64
FROM scratch AS app-amd64
COPY deliveries/jar/codexnaturalis.jar /opt/app/lib/codexnaturalis.jar

# ---------------------------------------------------------------- app: arm64
FROM maven:3.9-eclipse-temurin-21 AS app-arm64-build
WORKDIR /src
COPY pom.xml .
COPY src/main ./src/main
# Only the libraries the code actually imports (JavaFX base/graphics/controls/fxml + Gson);
# tests are skipped here, like on the amd64 path that ships the prebuilt jar.
RUN --mount=type=cache,target=/root/.m2 \
    set -eux; \
    mvn -B -q -DskipTests package; \
    mkdir -p /opt/app/lib; \
    cp target/codexnaturalis-*.jar /opt/app/lib/codexnaturalis.jar; \
    mvn -B -q org.apache.maven.plugins:maven-dependency-plugin:3.8.1:copy-dependencies \
        -DincludeScope=runtime \
        -DincludeArtifactIds=javafx-base,javafx-graphics,javafx-controls,javafx-fxml,gson \
        -DoutputDirectory=/opt/app/lib
# Guard: the jar must contain FXML, images and card data, and JavaFX must be the aarch64 build.
RUN set -eu; \
    for f in mainboard.fxml rmi_or_socket.fxml images/StartingCardsFRONT.txt images/Faces/back-96.jpeg; do \
      jar tf /opt/app/lib/codexnaturalis.jar | grep -qx "$f" \
        || { echo "ERROR: '$f' missing from codexnaturalis.jar (check <resources> in pom.xml)"; exit 1; }; \
    done; \
    ls /opt/app/lib/javafx-graphics-*-linux-aarch64.jar >/dev/null

FROM scratch AS app-arm64
COPY --from=app-arm64-build /opt/app /opt/app

# ---------------------------------------------------------------- app for the target platform
FROM app-${TARGETARCH} AS app

# ---------------------------------------------------------------- hooks
# Docker-only JavaFX hook (docker/hooks): same bytecode on every platform, so it is built once,
# natively, against the JavaFX API contained in the delivered jar.
FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jdk AS hooks
COPY deliveries/jar/codexnaturalis.jar /tmp/javafx-api.jar
COPY docker/hooks/ /src/
RUN set -eux; \
    javac --release 21 -cp /tmp/javafx-api.jar -d /tmp/classes $(find /src -name '*.java'); \
    mkdir -p /opt/hooks; \
    jar --create --file /opt/hooks/codex-docker-hooks.jar -C /tmp/classes .

# ---------------------------------------------------------------- jre
# Modules computed with jdeps on app + JavaFX base/graphics/controls/fxml + Gson.
FROM eclipse-temurin:21-jdk AS jre
RUN jlink \
      --add-modules java.base,java.desktop,java.rmi,java.scripting,java.sql,jdk.jfr,jdk.unsupported \
      --strip-debug --no-header-files --no-man-pages --compress=zip-6 \
      --output /opt/jre

# ---------------------------------------------------------------- assets
FROM debian:trixie-slim AS assets
RUN apt-get update && apt-get install -y --no-install-recommends unzip && rm -rf /var/lib/apt/lists/*
ADD --checksum=sha256:b1003a11b6e6e8d8f7f5e5586daae7f8ca651d8aee0aa155ff9ac841c48f52c6 \
    https://github.com/novnc/noVNC/archive/refs/tags/v1.7.0.tar.gz /tmp/novnc.tar.gz
ADD --checksum=sha256:b6413e364efd04f3c92ec8c17747e3c4adc20157c2ef1c5d019a26d944a46df8 \
    https://github.com/novnc/websockify/archive/refs/tags/v0.13.0.tar.gz /tmp/websockify.tar.gz
ADD --checksum=sha256:3f62c51e05e3b5a1e6241cf92a371f0be2ea1183aa87b30718bbd40832a8d423 \
    https://github.com/microsoft/Selawik/releases/download/1.01/Selawik_Release.zip /tmp/selawik.zip
ADD --checksum=sha256:77b7c2506d4efb22e09c8ccf10159f4956eab3ef7c007fef95de136bcf45300c \
    https://raw.githubusercontent.com/microsoft/Selawik/1.01/LICENSE.txt /opt/fonts/selawik/LICENSE.txt
RUN set -eux; \
    mkdir -p /opt/novnc /opt/websockify /tmp/ws; \
    tar -xzf /tmp/novnc.tar.gz --strip-components=1 -C /opt/novnc; \
    cd /opt/novnc; \
    rm -rf tests docs snap po utils .github .gitignore .gitmodules .eslintrc karma.conf.js eslint.config.mjs package.json; \
    printf '%s\n' \
      '<!doctype html><meta charset="utf-8"><title>Codex Naturalis</title>' \
      '<meta http-equiv="refresh" content="0; url=vnc.html?autoconnect=true&amp;resize=scale&amp;reconnect=true&amp;reconnect_delay=1000">' \
      > index.html; \
    tar -xzf /tmp/websockify.tar.gz --strip-components=1 -C /tmp/ws; \
    mv /tmp/ws/websockify /tmp/ws/COPYING /opt/websockify/; \
    unzip -q -j /tmp/selawik.zip '*.ttf' -d /opt/fonts/selawik

# ---------------------------------------------------------------- runtime
FROM debian:trixie-slim AS runtime

ENV LANG=C.UTF-8 \
    JAVA_HOME=/opt/jre \
    PATH=/opt/jre/bin:$PATH

# Placeholder for libglx-mesa0: it is the only link that drags Mesa + LLVM (~180 MB) in via
# xvfb -> libgl1 -> libglx0. JavaFX renders in software (-Dprism.order=sw), so GL is unused.
# apt stays consistent; to get real Mesa back, delete this block.
RUN set -eux; \
    mkdir -p /tmp/glx/DEBIAN; \
    printf '%s\n' \
      'Package: libglx-mesa0' \
      'Version: 999:0+codex' \
      'Architecture: all' \
      'Multi-Arch: foreign' \
      'Maintainer: Codex Naturalis Docker <noreply@localhost>' \
      'Description: placeholder - Mesa GLX not needed (JavaFX renders in software)' \
      > /tmp/glx/DEBIAN/control; \
    dpkg-deb --build /tmp/glx /tmp/glx.deb; \
    dpkg -i /tmp/glx.deb; \
    rm -rf /tmp/glx /tmp/glx.deb; \
    apt-get update; \
    apt-get install -y --no-install-recommends \
        libgtk-3-0t64 libxtst6 fontconfig fonts-dejavu-core fonts-crosextra-caladea \
        xvfb x11vnc python3 \
        gum tini; \
    rm -rf /var/lib/apt/lists/*; \
    useradd --create-home --uid 1000 --shell /bin/bash codex; \
    install -d -o codex -g codex /data; \
    install -d -m 1777 /tmp/.X11-unix

COPY --from=jre    /opt/jre           /opt/jre
COPY --from=assets /opt/novnc         /opt/novnc
COPY --from=assets /opt/websockify    /opt/websockify
COPY --from=assets /opt/fonts/selawik /usr/local/share/fonts/selawik
COPY --from=app    /opt/app           /opt/app
COPY --from=hooks  /opt/hooks/codex-docker-hooks.jar /opt/app/lib/

# Windows look: Selawik (Segoe UI metrics) as default font, Caladea in place of Constantia
COPY docker/fonts.conf /etc/fonts/conf.d/50-codex.conf
COPY --chmod=0755 docker/entrypoint.sh /usr/local/bin/entrypoint.sh
COPY --chmod=0644 docker/start-gui.sh docker/autorespond.py /usr/local/lib/codex/
# CRLF safety net for Windows checkouts (see docker/.gitattributes), then build the font cache
RUN set -eux; \
    sed -i 's/\r$//' /usr/local/bin/entrypoint.sh /usr/local/lib/codex/*; \
    fc-cache -f

USER codex
WORKDIR /data
VOLUME ["/data"]
# 1099 RMI, 1234 Socket, 6080 noVNC
EXPOSE 1099 1234 6080

# tini: PID 1 that forwards signals to the whole group (-g) and reaps children (-s)
ENTRYPOINT ["tini", "-g", "-s", "--", "/usr/local/bin/entrypoint.sh"]
