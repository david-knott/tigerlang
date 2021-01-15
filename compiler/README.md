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

## Testing

### E2E Testing

Create assembly for runtime c file.

```
gcc -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar -S -o runtime.s runtime.c
```

## Building

Ensure data directory is present in the target folder.

Build an executable jar file into the compiler/target directory

```
mvn package
```

Testing.

For executable jar

```
java -jar compiler-1.0-SNAPSHOT.jar ../src/test/java/com/chaosopher/tigerlang/compiler/fixtures/merge_simple.tig

```
For library jar
```
java -cp target/compiler-1.0-SNAPSHOT.jar com.chaosopher.tigerlang.compiler.main.Main
```

on the classes directly
```
java -cp target/classes/ com.chaosopher.tigerlang.compiler.main.Main for.tig
```

Test compilation works using runtime assembly.
```
gcc -x assembler-with-cpp -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar src/main/resources/runtimes/runtime.s as 

```

