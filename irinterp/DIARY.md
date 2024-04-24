# Diary

## 3 December 2020

How to write an interpreter for the tiger language ?

Visit the IR Tree, which is a linked list of Fragments

For each fragment, traverse the the code, which maps to a function body.

Build up a scoped symbol table with values based on the program input.

Need to be able to JUMP to other parts of the tree which we may have not already seen.

Labels are unique within a fragement ( program ? ) so we should be able to indentiy a particular labe
and start processing from there.