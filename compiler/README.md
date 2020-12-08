# Tiger Compiler

## Installation

Ensure jdk 11 is installed.

Download https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz

Unzip to ~/jdks/

Set JAVA_HOME in .bashrc

```
JAVA_HOME=~/jdks/jdk-11.0.9.1+1
export JAVA_HOME
```

Close terminal and open again. Check if java is recognised

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

Build jar

```
mvn package
```

This should build a jar file into the compiler/target/ directory

Execute jar

