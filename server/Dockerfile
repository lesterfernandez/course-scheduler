FROM eclipse-temurin:19
RUN mkdir /opt/app
COPY target/CourseScheduler-*.jar /opt/app/App.jar
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/App.jar"]