# Quarkus Social
A system designed to create users, establish connections, responsibly send messages, and incorporate various additional features.

### prerequisites

what do you need to run the project?
* [Maven](https://gradle.org/)
* [MySQL](https://www.mysql.com/)
* [quarkus]( https://quarkus.io/)
* [JDK-17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Docker Compose](https://docs.docker.com/compose/)

### how can we download the application?
#### SSH
```
git@github.com:mateusferian/quarkus-social.git
```
#### HTTPs
```
https://github.com/mateusferian/quarkus-social.git
```

### code versioning best practices
* Using Gitflow
* Using semantic commit

## How to run the application in production?
```
java -jar ./target/quarkus-app/quarkus-run.jar
```

### how to run application with docker?

To run the application with Docker, run the following command in the terminal:
```
docker run -i -p 8080:8080 --name quarkus-social-container quarkus-social:1.1
```

## How to stop the application?
```
docker stop quarkus-social-container
```

## How to stop the application?
```
docker start quarkus-social-container
```

### how to run unit tests?
open the terminal and run the command below:

```
./mvnw test
```

### to access API documentation
Open your browser and go to the following link:

```
http://localhost:8080/q/swagger-ui/
```

### to access API documentation
how to build and package a Quarkus project, removing old artifacts, generating a package (as JAR) and skipping running the tests?

```
./mvnw clean package -DskipTests
```

### technologies used

* [Maven](https://gradle.org/) - Maven is a powerful build automation tool that streamlines dependency management and the compilation process in 
Java projects, promoting standardization and development organization..
####
* [quarkus]( https://quarkus.io/) - Quarkus redefines Java development for the cloud-native era, providing an efficient, fast, and lightweight experience,
specifically designed for building microservices and cloud-native applications with maximum efficiency.
####
* [Swagger](https://swagger.io/) - Simplify API development for users, teams, and enterprises with the open source, professional Swagger toolset.
