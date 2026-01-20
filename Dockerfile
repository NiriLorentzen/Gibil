FROM ubuntu:latest
LABEL authors="niril"

ENTRYPOINT ["top", "-b"]