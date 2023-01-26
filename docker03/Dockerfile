# Get the latest Alpine base image
FROM alpine:latest

# install python, gcc and the bash shell
RUN apk add python3
RUN apk add build-base
RUN apk add --no-cache bash
RUN apk add sudo

#Create a user and copy the C program to their directory
RUN adduser -D cosc345
RUN echo 'cosc345:alpine' | chpasswd
COPY . /home/cosc345
WORKDIR /home/cosc345
#RUN cd /home/cosc345
RUN gcc -o message message.c


#USER cosc345
#WORKDIR /home/cosc345

# Run the shell
# CMD [ "/bin/sh" ]
CMD [ "bash" ]