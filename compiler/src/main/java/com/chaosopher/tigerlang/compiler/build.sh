#!/bin/bash
export PATH=~/bin/jdk-14+36/bin/:$PATH
package=compiler_build_script
while test $# -gt 0; do
  case "$1" in
    -h|--help)
      echo "$package - builds compiler java code"
      echo " "
      echo "$package [options]"
      echo " "
      echo "options:"
      echo "-h, --help                show brief help"
      echo "-l                        rebuild lexer"
      echo "-p                        rebuild parser"
      exit 0
      ;;
    -l)
      shift # shift moves positional parameters to the left to allow while loop the exit
      echo "generating lexer"
        export LEXER='set'
      ;;
    -p)
      shift
        echo "generating parser"
        export PARSER='set'
      ;;
    *)
      break
      ;;
  esac
done
# where -n tests that the argument is
# not zero length
# compile jlex and build lexer
if [ -n "$LEXER" ]
then
    cd ./JLex
    javac -d ../bin Main.java
    cd ..
    java -cp bin JLex.Main Parse/Tiger.lex
    mv Parse/Tiger.lex.java Parse/Yylex.java
    exit;
fi

# compile java cup and create grammar parser
if [ -n "$PARSER" ]
then
  rm Parse/Grm.out
  rm Parse/Grm.err
  javac -d bin java_cup/*.java java_cup/runtime/*.java
  java -cp bin java_cup.Main -parser Grm -expect 6 -dump Parse/Grm.cup >Parse/Grm.out 2>Parse/Grm.err
  if [ ! -f "Grm.java" ]; then
    echo "Error in CUP"
    exit;
  fi
  mv Grm.java Parse
  mv sym.java Parse
  exit;
fi

javac -d bin Symbol/*.java Absyn/*.java ErrorMsg/*.java Parse/*.java Types/*.java Semant/*.java Main/*.java
# jar cvfe jarexample.jar jexample.mf *.class
#java -cp bin Parse.Main ../reference/tiger/testcases/test3.tig

#cd ..
#javac -cp .:./compiler/bin:./lib/junit-4.13-beta-3.jar ./test/*.java
#java -cp .:./compiler/bin:./lib/junit-4.13-beta-3.jar:./lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.Chap5Test
cd compiler
