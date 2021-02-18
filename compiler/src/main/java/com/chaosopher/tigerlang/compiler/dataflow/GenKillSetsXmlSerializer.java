package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.BitSet;

import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * This class is used to generate an XML representation of
 * kill and gen sets. Currently it is only used for testing purpose.
 */
public class GenKillSetsXmlSerializer {
    private final GenKillSets genKillSets;

    /**
     * This constructor creates a new instance that can produce
     * an xml representation of the kill and gen sets.
     * @param genKillSets
     */
    public GenKillSetsXmlSerializer(GenKillSets genKillSets) {
        this.genKillSets = genKillSets;
    }

    public void serialize(PrintStream printStream) {
        printStream.println("Iterations:" + this.genKillSets.getIterations());
        for(NodeList nodeList = this.genKillSets.getCfg().nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.print("Block:");
            BasicBlock basicBlock = this.genKillSets.getCfg().get(nodeList.head);
            printStream.print(basicBlock.hashCode() + "");
            BitSet in = this.genKillSets.getIn(basicBlock);
            printStream.print("| in:");
            printStream.print(in);
            BitSet out = this.genKillSets.getOut(basicBlock);
            printStream.print(" out:");
            printStream.print(out);
            BitSet gen = this.genKillSets.getGen(basicBlock);
            printStream.print(" gen:");
            printStream.print(gen);
            BitSet kill = this.genKillSets.getKill(basicBlock);
            printStream.print(" kill:");
            printStream.print(kill);
            printStream.println();

            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.genKillSets.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                BitSet inStatement = this.genKillSets.getIn(stmList.head);
                printStream.print("| in:");
                printStream.print(inStatement);
                BitSet outStatement = this.genKillSets.getOut(stmList.head);
                printStream.print(" out:");
                printStream.print(outStatement);
                BitSet genStatement = this.genKillSets.getGen(stmList.head);
                printStream.print(" gen:");
                printStream.print(genStatement);
                BitSet killStatement = this.genKillSets.getKill(stmList.head);
                printStream.print(" kill:");
                printStream.print(killStatement);
                printStream.println();
            }
        }
    }
}