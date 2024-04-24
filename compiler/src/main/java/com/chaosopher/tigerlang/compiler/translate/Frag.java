package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.frame.Frame;

/**
 * An abstract code fragment. This contains either a function body IR tree or a
 * string literal. This class also functions as a singly linked list.
 */
public abstract class Frag {

    public Frag next;

    public abstract Frame getFrame();

    public abstract void accept(FragmentVisitor fragmentVisitor);

}