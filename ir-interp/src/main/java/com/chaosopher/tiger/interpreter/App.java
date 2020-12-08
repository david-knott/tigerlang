package com.chaosopher.tiger.interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import Translate.FragList;
import Tree.IR;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Missing serialized IR file path");
            System.exit(0);
        }
        try(FileInputStream fileIn = new FileInputStream(args[0]) ) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileIn);
            FragList fragList = (FragList)objectInputStream.readObject();
            ExecutionOrderVisitor interpreterVisitor = new ExecutionOrderVisitor(System.out, System.err);
            fragList.accept(interpreterVisitor);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
}