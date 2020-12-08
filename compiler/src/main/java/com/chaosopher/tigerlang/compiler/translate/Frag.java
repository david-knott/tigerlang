package com.chaosopher.tigerlang.compiler.translate;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.core.Component;
import com.chaosopher.tigerlang.compiler.frame.Frame;

/**
 * An abstract code fragment. This contains either a function body IR tree or a
 * string literal. This class also functions as a singly linked list.
 */
public abstract class Frag extends Component {

    public Frag next;

    public abstract Frame getFrame();

    public abstract void accept(FragmentVisitor fragmentVisitor);

}