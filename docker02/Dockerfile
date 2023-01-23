# Uses alpine as its base image
# Add/install: C compile, python interpreter, bash shell
# Add a new user named cosc345 w/ alpine as the password
# Copy message.c to cosc345’s home directory
# Set the working directory to /home/cosc345
# Compile message.c creating an executable named message
# Run the bash shell

FROM alpine:latest
RUN apk add bash
RUN apk add gcc
RUN apk add python3
RUN apk add libc-dev
RUN adduser -D cosc345 -s /bin/bash -h /home/cosc345
RUN echo "cosc345:alpine" | chpasswd
COPY message.c /home/cosc345
WORKDIR /home/cosc345
RUN gcc message.c -o message
CMD ["/bin/bash"]