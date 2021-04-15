# Diary

## Thursday 15th April 2021

I am working on quad deatomization. There is a problem with the final
dead code elimination phase where is is removing statements like this

```
move(
  temp(rax)
  temp(t17)
)
```

This is because the destination temp rax is not live after control leaves
this statement. It is actually live as we need to put the result into rax
its just the data flow doesn't recognize this. In the original liveness analysis the rax register is precoloured meaning its

I also need to fix the fragment visitors so that the order of the fragments
doesn't change.

## Friday 9th April 2021

I tried using the gen kill class for reaching expressions in a foward data flow implementation, similar to the other data flow
implementations. This worked for all simple test cases except for recomputation of the expression. I initializes all the sets
to all the definitions in the program.

Question is, how to test for recomputation ?

I created a hashset with exp -> set of definition ids at initialization. I used this to calculate statements that recompute the same expression.


http://www.eecs.umich.edu/courses/eecs583/slides/Lecture6.pdf

It appears the reaching applies to the union operator and available to the intersection operator.

A definition d reaches a point p if there is a path from the
point immediately following d to p such that d is not
“killed” along that path 

A definition d is available at a point p if along all paths
from d to p, d is not killed 

I think there is a bug in the initialize function in REDataFlow and AEDataFlow.


## Thursday 8th April 2021

Completed kill and gen for reaching expressions.

## Wednesday 7th April 2021

https://www.cs.colostate.edu/~mstrout/CS553Fall06/slides/lecture11-dataflowopts.pdf

https://www.itu.dk/people/wasowski/teach/dsp-compiler-07/episode-6/episode06-handout.pdf


## Wednesday 31 March 2020

Refactoring GenKill so that there is a common base class.


## Friday 26th March 2021

Refactored Labels so they are created by a LabelFactory.

## Friday 5th March 2021

Converting quadruples back into tree's requires data flow analysis, in particular
reaching definitions and available expressions. This makes sense as merging move src's
into subsequent moves changes the order of execution, which could change the result
of the program. We need to check first that the original move expression is available at
the point we want to merge it.

## Tuesday 2nd of March 2021

Parser and lexer for LIR/HIR is complete.

Refactor kill and gen / reachable definitions.

Refactor dataflow package into sub packages.

KillGen needs proper unit tests

Reachable Definitions needs proper unit tests.

Copy Propagation is not working correctly. I have commented out the 
actual rewrite so that the tests all pass. I need to investigate this.
The problem is caused by propagation of temporaries created in the atomize
phase. When deatomise is called, these are recombined.

## Thursday 25th February 2021

Working on lexer for tree. At the moment I am using a simple buffering technique
that will fail if the lexeme being parsed is longer that the buffer.

Multiple calls to the scan function are not working correctly, we skip the last non
matching character from the previous call to scan.


There is a dataflow constant propagation bug for this function. It seems
to be missing the a in the addition ((a + b) + c)

```
function add():int = (let var a:= 3 var b:= 4 var c:= 5 in a + b + c end)
```


Another approach would be to prefill a buffer


## Thursday 19th February 2021

Task Register updated to handle disjunctive tasks. I am not happy with the implementation
as the cli flags must be provided in the correct order and disjunctive tasks ( optimize )
are messy.

Constant propagation has been integrated however I am not sure if it works yet.


## Thursday 18th February 2021

Rough version of constant propagation finished.

## Thursday 11th February 2021
Added call graph and data flow graph renderers to the front end.

## Wednesday 10th February 2021
Worked on the CFG Graphiz Renderer.

Next up, display cfg using angular graphiz renderer.

https://github.com/magjac/d3-graphviz-angular

Then do the same for the function call graph.


Ideas: 

Load all the test programs into web app.

Find program -> autosuggest with preloaded programs.

Allow a program to be saved, this stores the program in a mongodb instance.

Allow a program to be assembled and linked, checks if program can be compiled and then sends a request
to a target docker instance that compiles the assembly, zips the executable and sends it to the output
stream.

Write interpreter.

## Tuesday 9th February 2021

Fixed compiler so that no-prelude hides the primitive function references.

## Tuesday 2nd February 2021

Started refactoring so that additional cpu targets can be introduced.
The frame procedure entry exit call has been moved out of the translate phase
into the assembly generator phase.

## Thursday 28th January 2021

Added error inside new JUMP/JUMP remove function in Canon. I want to 
catch cases where the JUMP/JUMP is at the end of the statement list.
## Wednesday 27th January 2021

Remove second jump where there are two side by side.

I am not sure if this will handle 3 side by side.

Next step is to get the flow diagram into the angular application.

## Tuesday 26th January 2021

Need to tidy up the CFG generator. It appears correct for if's and basic inline program.
While loops dont look correct. This appears to be due to having the break as the then
condition of an ifthenelse. These expressions use a join label which both conditions jump
to after the body. In the case of the break, it inserts a new jump inside branch body,
which is directly before the join jump. This results in two consequtive jumps, where
the second one is never hit.  Maybe there is no work around for this ?

When we apply the canonicalisation to this hir tree, it adds an extra label before the join jump
so that the tree makes sense. This is why the while loop looks strange when visualised. This
problem is also in the LRDE implementation. See the break sample at the end of this page
https://assignments.lrde.epita.fr/compiler_stages/tc_5/primitive_samples.html

Possible solutions would be to remove the second jump after canonicalisation as it never used.

## Monday 25 January 2021

Working on the dataflow. So far atomisation and recombination work.

java -cp ./target/classes/ com.chaosopher.tigerlang.compiler.main.Main -A ./src/test/java/com/chaosopher/tigerlang/compiler/fixtures/tbi.tig


java -cp ./target/classes/ com.chaosopher.tigerlang.compiler.main.Main --cfg  ./src/test/java/com/chaosopher/tigerlang/compiler/fixtures/div.tig > dot;dot -Tpng -O dot


## Thursday 21st January 2021

It looks like the issue is related to the break statement inside the while loop.
I modified tbi-desugar so that the test condition would terminate, this worked.

I reenabled the condition optimisation, which I had previously thought was causing 
problems due to it creating 2 disconnected graphs. This doesn't appear to be causing
problems now. I will keep it in mind for any future bugs.

## Tuesday 19th January 2021

Upon further testing the issue discovered on the 19th is evident in more basic code.
The issue appears to be related to the 1 predicate. When I change while 1 to while i < 10,
the code compiles correctly. 
```
/*
caused a bug where rsp we used for register allocation
*/
        let
                var i:int := 1
            function printb() =
                (
                            while 1 do
                            (
                                        i := i + 5
                        )
                )
        in
            (
                printb()
            )
        end
```

The code that generates the conditional translation for a constant is to blame. If the
conditional expression is a constant we replace a cjump with a jump as there is nothing
to evaluate. I suspect the problem is in the construction of the flow graph.

I generated a graphiz flow graph from the assembly and as I suspected there are 2 disconnected
graphs. This is certainly the cause of the bug.  Either the generated translated IR code is
wrong, or the flow graph generation is wrong. 

Looking at the generated IR I can see the graph is indeed correct. We always jump to the true
branch which means the false branch is never executed. The flow graph analysis recognises this
as a seperate graph.

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
