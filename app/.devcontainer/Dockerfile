FROM maven:3.9.11-eclipse-temurin-17

RUN useradd -m vscode && \
    apt-get update && apt-get install -y git curl && \
    rm -rf /var/lib/apt/lists/*

USER vscode
WORKDIR /workspace
