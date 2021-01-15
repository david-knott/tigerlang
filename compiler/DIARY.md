# Diary

## Friday 15th January 2020
I think the colouring bug is due to a problem in the generated IR for nested whiles.
I will need to generate the flow graph for the IR so I can see the structure.
The following demonstrates the error

```
/*
caused a bug where rsp we used for register allocation
*/
        let
                var i:int := 1
                var j:int := 1
            function printb() =
                (
                            while 1 do
                            (
                                        while 1 do
                                        (
                                                j := j + 3
                                        );
                                        i := i + 5
                        )
                )
        in
            (
                printb()
            )
        end
```

## Thursday 14th January 2020
Weird bug in generated assemnly when we desugar for loops into while. 
It seems that values are being assigned into the rsp register ! This should
not happen as rsp conflicts with every other register in the graph colour phase.
I have no idea why this is happening.

## Monday 31st December 2020

Renamer broken ( RecordExp and ArrayExp ).

## Monday 21st December 2020
Refactoring in progress. I need to figure out how named types translate into their
type representation in the compiler. At present the following throws an error
var a:string := 1. the type checker knows that 1 ( IntExp ) has a type of INT,
but it doesn't know that a:string ( NameTy (string) ) is of type STRING. Previously
using a symbol table which mapped string -> STRING this worked fine. But, I removed
the extra information in the symbol table to make binding just concerned with finding
uses and definitions in the AST. 

So the question is, if we see a AST type, how do we map that to an actual compiler type ?

Whats the difference between a ConstructorType and a Typable ?

## Sunday 20th December 2020
I have started the refactoring of the binder. I want to remove the TYPE references
so that we are concened only with bind uses of syntax to their definitions. Given that
symbol tables are used for this, and they rely on Types, I am not sure how we do this.

Currently the symbol tables uses actual types for binding, say we just used the symbol and 
nohing else. Perhaps removing type from the SymbolTable is what we need to do ?
1) How to handle native types, INT and STRING, they do not have a definining AST. Perhaps they
can be assigned to the root AST node.

All tests pass, however I am not happy about the treatment of the native types, we install
them into the type symbol table with a null reference to a definition.

Dataflow analysis in on hold until I have completed this refactoring.

## Saturday 19th December 2020
Bugs in type checking method formal arguments versus actual arguments.
Bug for invalid expressions like 'print("Hello World")dd'
Both items were fixed, first due to an illegal cast of ExpList to Absyn. The parser
expects that all the classes extend from the same base class.
The second item was due to the type visitor not processing the function bodies. The
original unit tests didn't catch this because we were using the parser directory.
The parser service wraps the code in a declist if there isn't one present.

I need to revist the type checking before any further work.


## Thursday 17th December 2020
Started dataflow analysis, splitting tree IR into quadruples and the recombining them.
Search for a tiger colour palette for the design.


## Monday 14th Decemeber 2020
Calculating static links and whether they can escape. Not complete. Doesn't work in
the sister example in the test suite.

## Tuesday 8th December 2020
Moved the code base from the old project into a new maven based project.
Refactored all the package names to be inline with java conventions.
The old project at https://github.com/david-knott/moderncompilerinjava has
been archived.
