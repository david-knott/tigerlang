package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.util.BoolList;

/**
 * A new level is created for each function definition we encounter during
 * the type check phase. The level represents the static nesting level of the
 * function within the source code. Function nesting is represented as a tree
 * with the main entry method as the root of this tree. Each function will
 * have a level instance.
 * The frame member is the activate record layout for the associated function
 * The parent member is the parent level which contains this level.
 * The formals member is the formal argument list for this function. It is
 * used to indicate whether the arguments should be placed in memory or registers
 */
public class Level {
   
    Frame.Frame frame;
    Level parent;
    public AccessList formals;

    /**
     * Creates the root Level using frame reference f.
     * @param f
     */
    public Level(Frame.Frame f) {
        frame = f;
    }

    /**
     * Creates a new static nesting level, using the supplied level as it parent.
     * The symbol name relates to the function that is linked to this level. 
     * Note that we create a the formal list with the first argument as the static 
     * link.
     * Internally a new frame is created and referenced by this instance. The new
     * frame may create @see Frame.Access for the functions formal arguments.
     * @param prnt the parent level and associated frame.
     * @param name the label of the function
     * @param fmls the list of formal arguments
     * @param sl should a static link be created in the frame
     */
    public Level(Level prnt, Label name, BoolList fmls, boolean sl) {
        parent = prnt;
        frame = prnt.frame.newFrame(name, sl ? new BoolList(true, fmls) : fmls);
        Frame.AccessList frameFormals = frame.formals;
        for(; frameFormals != null; frameFormals = frameFormals.tail) {
            if(formals == null){
                formals = new AccessList(new Access(this, frameFormals.head));
            } else {
                formals = formals.append(new Access(this, frameFormals.head));
            }
        }
    }

    /**
     * Creates a local access within the frame for this level
     * @param escape should the variable escape or not
     * @return the new access
     */
    public Access allocLocal(boolean escape) {
        return new Access(this, frame.allocLocal(escape));
    }
}