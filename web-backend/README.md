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


## Building

Ensure data directory is present in the target folder.

Build an executable jar file into the compiler/target directory

```
mvn package
```

Test if jar was built.

```
java  -jar target/gs-spring-boot-docker-0.1.0.jar

```

```
docker build -t tiger-web-backend .
docker run --rm -p 8080:8080 tiger-web-backend
```