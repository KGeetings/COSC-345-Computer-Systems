# Dockerfile

# Use a base image that runs Linux and allows Python programs to be executed.
FROM python:3.7-alpine

# Create a new directory in the image named PythonCode
RUN mkdir /PythonCode

# Copy the python program you wrote in the first step (main.py) to this new PythonCode directory
COPY main.py /PythonCode

# Execute a shell at the end of the Dockerfile with
CMD ["/bin/sh"]