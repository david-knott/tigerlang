# Tiger IR Interpreter

An interpreter for the IR produced by the tiger compiler translate and canon phase.
Takes a serialized IR Tree as input.

## Build

```
mvn package
```


## Run

```
java -cp target/tiger-ir-interpreter-1.0-SNAPSHOT.jar com.chaosopher.tiger.interpreter.App fragment-list.bin
```


### Links

https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html