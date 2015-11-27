# Hashtag Compiler
Project done for Compilers phase 2. This `branch` contains the lexical, syntactic and semantic analysis, as well as some intermediate
code generation (incomplete).

Project created using IntelliJ IDEA 14 as our development environment and Java 8 (1.8) as the programming language of choice. This `branch` is not linked to any IDE, so you can build this project form source (`src` directory) using the IDE of your choice. You also need to include the necessary libraries (`*.jar`) for this project to work, which are all included in the `lib` directory. The `res` directory is also needed, it includes files that the lexer and parser read (as well as some sample `.ht` files), so be careful where you put it (just put it in the root directory of your project, if not you'll need to modify the paths where necessary). The `doc` directory contains a `.pdf` about the functionality of this project (it's in spanish, and it's not complete).

## What's missing
Intermediate code for:
* Switch statements
* Function calls
* Functions

Type checking for:
* Functions with return type must have a return statement

Grammar:
* Arithmetic expressions need to accept functions calls

Final code:
* This branch doesn't include any final code generation.

# Sample files
This project contains some `.ht` files (extension our language uses) so you can test out the functionality of the compiler. The syntax is very similar to Java as you will see. These files are found in `res/samples/code/`.