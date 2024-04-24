# Tiger Compiler Web Backend

## Installation

Ensure jdk 11 is installed.

Download https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz

Unzip to ~/jdks/

Set JAVA_HOME in .bashrc

```
JAVA_HOME=~/jdks/jdk-11.0.9.1+1
export JAVA_HOME
```

Close terminal and open again so new java home is loaded. 

Check if java version is correct.

```
java --version
```

We would expect to see this

```
openjdk 11.0.9.1 2020-11-04
OpenJDK Runtime Environment (build 11.0.9.1+1-Ubuntu-0ubuntu1.18.04)
OpenJDK 64-Bit Server VM (build 11.0.9.1+1-Ubuntu-0ubuntu1.18.04, mixed mode, sharing)

```

## Development

Start the backend development server using spring boot tools.

./mvnw spring-boot:run


Start the front end angular application.

## Building

Ensure data directory is present in the target folder.

Build an executable jar file into the compiler/target directory

```
mvn clean package
```

Test if jar was built.

```
java  -jar target/web-backend-0.1.0.jar

```

Test simple compilation

```
curl -X POST  localhost:8080/compile -H 'Content-type:application/json' -d '{"code" : "var a:int := 1"}'
```

```
docker image rm tiger-web-backend
docker build -t tiger-web-backend .
docker run --rm -p 10000:8080 tiger-web-backend
```