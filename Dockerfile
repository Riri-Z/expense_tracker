# Start with a base image containing Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Add Maintainer Info
LABEL maintainer="rz"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8090

# Set environment variables
ENV JAVA_OPTS=""

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/expense_tracker-0.0.1-SNAPSHOT.jar app.jar

# Change ownership of the jar file
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring


# Run the jar file
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]