rm CodeGen.java
java -classpath ../../jburg/lib/antlr-3.4-complete.jar:../../jburg/lib/jburg.jar jburg.burg.JBurgMain Tree.jburg -logInfo -logWarning -debug -outputDir . -outputFile CodeGen.java

