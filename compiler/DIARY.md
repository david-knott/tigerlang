# Diary

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