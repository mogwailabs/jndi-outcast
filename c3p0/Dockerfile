FROM tomcat:10.0.0-jdk15-openjdk-buster

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# This reactivates the built-in webapps.
#RUN cp -avT $CATALINA_HOME/webapps.dist/manager $CATALINA_HOME/webapps/manager

# Don't forget to run 'maven clean package' first.
COPY target/c3p0-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

# This is only a means for documentation. The port needs to be actually *mapped*, too.
EXPOSE 8080

# Start Tomcat when the container launches.
CMD ["catalina.sh", "run"]