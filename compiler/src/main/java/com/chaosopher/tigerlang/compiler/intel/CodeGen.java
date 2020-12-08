/*  Generated Fri Aug 28 07:12:26 IST 2020 by JBurg version 2.0.2  */

package com.chaosopher.tigerlang.compiler.intel;
	import com.chaosopher.tigerlang.compiler.tree.*;
	import static Tree.TreeKind.*;
public class CodeGen  {
    java.util.Stack __reducedValues = new java.util.Stack();
	private Reducer reducer;
	public void setReducer(Reducer reducer) {
		this.reducer = reducer;
	}

	private Emitter emitter;
	public void setEmitter(Emitter emitter) {
		this.emitter = emitter;
	}
final public static int __move_NT = 1;
final public static int __temp_NT = 2;
final public static int __indirect_NT = 3;
final public static int __sxp_NT = 4;
final public static int __label_NT = 5;
final public static int __integerExpression_NT = 6;
final public static int __call_NT = 7;
final public static int __moveCall_NT = 8;
final public static int __indirectWithDisplacementAndScale_NT = 9;
final public static int __cjump_NT = 10;
final public static int __mem_NT = 11;
final public static int __indirectWithDisplacement_NT = 12;
final public static int __binopOffsetExpression_NT = 13;
final public static int __name_NT = 14;
final public static int __integerConstant_NT = 15;
final public static int __exp_NT = 16;
final public static int __stmt_NT = 17;
final public static int __binop_NT = 18;
final public static int __expCall_NT = 19;
final public static int __jump_NT = 20;
final public static int nStates = 20;
public  JBurgAnnotation label(Tree.IR to_be_labelled) {
    int             arity   = to_be_labelled.getArity();
    JBurgAnnotation result  = this.getJBurgAnnotation(to_be_labelled);

    for(int i = 0; i < arity; i++) {
        result.addChild(this.label(to_be_labelled.getNthChild(i)));
    }

    return result;
}

/* binopOffsetExpression */

private  BinopOffsetExpression action_1(Tree.IR __p) throws java.lang.Exception {
    Integer arg1 = (Integer)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.binopOffsetExpression(__p,arg0,arg1));
}

/* binopOffsetExpression */

private  BinopOffsetExpression action_2(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg1 = (Temp.Temp)__reducedValues.pop();Integer arg0 = (Integer)__reducedValues.pop();
    		return (reducer.binopOffsetExpression(__p,arg1,arg0));
}

/* indirect */

private  IndirectExpression action_3(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg = (Temp.Temp)__reducedValues.pop();
    		return (reducer.indirect(__p,arg));
}

/* indirectWithDisplacement */

private  IndirectWithDisplacementExpression action_4(Tree.IR __p) throws java.lang.Exception {
    BinopOffsetExpression arg0 = (BinopOffsetExpression)__reducedValues.pop();
    		return (reducer.indirectWithDisplacement(__p,arg0));
}

/* indirectWithDisplacementAndScale */

private  IndirectWithDisplacementAndScaleExpression action_5(Tree.IR __p) throws java.lang.Exception {
    BinopOffsetExpression arg1 = (BinopOffsetExpression)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.indirectWithDisplacementAndScale(__p,arg0,arg1));
}

/* move */

private  Tree.IR action_6(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg1 = (Temp.Temp)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.storeMemToMem(__p,arg0,arg1));
}

/* move */

private  Tree.IR action_7(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg1 = (Temp.Temp)__reducedValues.pop();IndirectExpression arg0 = (IndirectExpression)__reducedValues.pop();
    		return (reducer.storeIndirect(__p,arg0,arg1));
}

/* move */

private  Tree.IR action_8(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg1 = (Temp.Temp)__reducedValues.pop();IndirectWithDisplacementExpression arg0 = (IndirectWithDisplacementExpression)__reducedValues.pop();
    		return (reducer.storeIndirectWithDisplacement(__p,arg0,arg1));
}

/* move */

private  Tree.IR action_9(Tree.IR __p) throws java.lang.Exception {
    Integer arg1 = (Integer)__reducedValues.pop();IndirectWithDisplacementExpression arg0 = (IndirectWithDisplacementExpression)__reducedValues.pop();
    		return (reducer.storeIndirectWithDisplacement(__p,arg0,arg1));
}

/* move */

private  Tree.IR action_10(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg1 = (Temp.Temp)__reducedValues.pop();IndirectWithDisplacementAndScaleExpression arg0 = (IndirectWithDisplacementAndScaleExpression)__reducedValues.pop();
    		return (reducer.storeIndirectWithDisplacementAndScale(__p,arg0,arg1));
}

/* move */

private  Tree.IR action_11(Tree.IR __p) throws java.lang.Exception {
    Integer src = (Integer)__reducedValues.pop();Temp.Temp dst = (Temp.Temp)__reducedValues.pop();
    		return (reducer.move(__p,dst,src));
}

/* move */

private  Tree.IR action_12(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp src = (Temp.Temp)__reducedValues.pop();Temp.Temp dst = (Temp.Temp)__reducedValues.pop();
    		return (reducer.move(__p,dst,src));
}

/* cjump */

private  Tree.IR action_13(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp right = (Temp.Temp)__reducedValues.pop();Temp.Temp left = (Temp.Temp)__reducedValues.pop();
    		return (reducer.cjumpStatement(__p,left,right));
}

/* sxp */

private  Tree.IR action_14(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg = (Temp.Temp)__reducedValues.pop();
    		return (reducer.sxpStatement(__p,arg));
}

/* label */

private  Tree.IR action_15(Tree.IR __p) throws java.lang.Exception {

    		return (reducer.labelStatement(__p));
}

/* stmt */

private  Object action_16(Tree.IR __p) throws java.lang.Exception {
    Object right = (Object)__reducedValues.pop();Object left = (Object)__reducedValues.pop();
    		return (reducer.seqStatement(__p,left,right));
}

/* jump */

private  Tree.IR action_17(Tree.IR __p) throws java.lang.Exception {

    	 Tree.IR arg0 = __p.getNthChild(0);;
    		return (reducer.jumpStatement(__p,arg0));
}

/* temp */

private  Temp.Temp action_18(Tree.IR __p) throws java.lang.Exception {

    		return (reducer.temp(__p));
}

/* integerConstant */

private  Integer action_19(Tree.IR __p) throws java.lang.Exception {

    		return (reducer.integerConstant(__p));
}

/* integerExpression */

private  Temp.Temp action_20(Tree.IR __p) throws java.lang.Exception {
    Integer integerConstant = (Integer)__reducedValues.pop();
    		return (reducer.integerExpression(integerConstant));
}

/* name */

private  Temp.Temp action_21(Tree.IR __p) throws java.lang.Exception {

    		return (reducer.nameExpression(__p));
}

/* binop */

private  Temp.Temp action_22(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp right = (Temp.Temp)__reducedValues.pop();Temp.Temp left = (Temp.Temp)__reducedValues.pop();
    		return (reducer.binopExpression(__p,left,right));
}

/* binop */

private  Temp.Temp action_23(Tree.IR __p) throws java.lang.Exception {
    Integer right = (Integer)__reducedValues.pop();Temp.Temp left = (Temp.Temp)__reducedValues.pop();
    		return (reducer.binopExpression(__p,left,right));
}

/* binop */

private  Temp.Temp action_24(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp right = (Temp.Temp)__reducedValues.pop();Integer left = (Integer)__reducedValues.pop();
    		return (reducer.binopExpression(__p,left,right));
}

/* binop */

private  Temp.Temp action_25(Tree.IR __p) throws java.lang.Exception {
    Integer right = (Integer)__reducedValues.pop();Integer left = (Integer)__reducedValues.pop();
    		return (reducer.binopExpression(__p,left,right));
}

/* mem */

private  Temp.Temp action_26(Tree.IR __p) throws java.lang.Exception {
    BinopOffsetExpression boe = (BinopOffsetExpression)__reducedValues.pop();
    		return (reducer.mem(boe));
}

/* mem */

private  Temp.Temp action_27(Tree.IR __p) throws java.lang.Exception {
    Integer ic = (Integer)__reducedValues.pop();
    		return (reducer.mem(ic));
}

/* mem */

private  Temp.Temp action_28(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp e = (Temp.Temp)__reducedValues.pop();
    		return (reducer.mem(e));
}

/* call */

private  Object action_29(Tree.IR __p) throws java.lang.Exception {
    java.util.Vector<Temp.Temp> args = (java.util.Vector<Temp.Temp>)__reducedValues.pop();
    		return (reducer.call(__p,args));
}

/* expCall */

private  Object action_30(Tree.IR __p) throws java.lang.Exception {
    Object c = (Object)__reducedValues.pop();
    		return (reducer.expCall(c));
}

/* moveCall */

private  Object action_31(Tree.IR __p) throws java.lang.Exception {
    Object c = (Object)__reducedValues.pop();Temp.Temp e = (Temp.Temp)__reducedValues.pop();
    		return (reducer.moveCall(e,c));
}

/* stmt */

private  Object action_32(Tree.IR __p) throws java.lang.Exception {

    		return ((Object)__reducedValues.pop());
}

/* stmt */

private  Object action_33(Tree.IR __p) throws java.lang.Exception {

    		return ((Object)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_34(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_35(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* stmt */

private  Object action_36(Tree.IR __p) throws java.lang.Exception {

    		return ((Object)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_37(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* stmt */

private  Object action_38(Tree.IR __p) throws java.lang.Exception {

    		return ((Object)__reducedValues.pop());
}

/* stmt */

private  Object action_39(Tree.IR __p) throws java.lang.Exception {

    		return ((Object)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_40(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_41(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* stmt */

private  Object action_42(Tree.IR __p) throws java.lang.Exception {

    		return ((Object)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_43(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

private  void dispatchAction(JBurgAnnotation ___node,int iRule) throws java.lang.Exception {
     Tree.IR __p = ___node.getNode();
    switch(iRule) {
        case 1: {
            __reducedValues.push(this.action_1(__p));
        }
        break;
        case 2: {
            __reducedValues.push(this.action_2(__p));
        }
        break;
        case 3: {
            __reducedValues.push(this.action_3(__p));
        }
        break;
        case 4: {
            __reducedValues.push(this.action_4(__p));
        }
        break;
        case 5: {
            __reducedValues.push(this.action_5(__p));
        }
        break;
        case 6: {
            __reducedValues.push(this.action_6(__p));
        }
        break;
        case 7: {
            __reducedValues.push(this.action_7(__p));
        }
        break;
        case 8: {
            __reducedValues.push(this.action_8(__p));
        }
        break;
        case 9: {
            __reducedValues.push(this.action_9(__p));
        }
        break;
        case 10: {
            __reducedValues.push(this.action_10(__p));
        }
        break;
        case 11: {
            __reducedValues.push(this.action_11(__p));
        }
        break;
        case 12: {
            __reducedValues.push(this.action_12(__p));
        }
        break;
        case 13: {
            __reducedValues.push(this.action_13(__p));
        }
        break;
        case 14: {
            __reducedValues.push(this.action_14(__p));
        }
        break;
        case 15: {
            __reducedValues.push(this.action_15(__p));
        }
        break;
        case 16: {
            __reducedValues.push(this.action_16(__p));
        }
        break;
        case 17: {
            __reducedValues.push(this.action_17(__p));
        }
        break;
        case 18: {
            __reducedValues.push(this.action_18(__p));
        }
        break;
        case 19: {
            __reducedValues.push(this.action_19(__p));
        }
        break;
        case 20: {
            this.reduceAntecedent(___node,__integerConstant_NT);
            __reducedValues.push(this.action_20(__p));
        }
        break;
        case 21: {
            __reducedValues.push(this.action_21(__p));
        }
        break;
        case 22: {
            __reducedValues.push(this.action_22(__p));
        }
        break;
        case 23: {
            __reducedValues.push(this.action_23(__p));
        }
        break;
        case 24: {
            __reducedValues.push(this.action_24(__p));
        }
        break;
        case 25: {
            __reducedValues.push(this.action_25(__p));
        }
        break;
        case 26: {
            __reducedValues.push(this.action_26(__p));
        }
        break;
        case 27: {
            __reducedValues.push(this.action_27(__p));
        }
        break;
        case 28: {
            __reducedValues.push(this.action_28(__p));
        }
        break;
        case 29: {
            __reducedValues.push(this.action_29(__p));
        }
        break;
        case 30: {
            __reducedValues.push(this.action_30(__p));
        }
        break;
        case 31: {
            __reducedValues.push(this.action_31(__p));
        }
        break;
        case 32: {
            this.reduceAntecedent(___node,__cjump_NT);
            __reducedValues.push(this.action_32(__p));
        }
        break;
        case 33: {
            this.reduceAntecedent(___node,__label_NT);
            __reducedValues.push(this.action_33(__p));
        }
        break;
        case 34: {
            this.reduceAntecedent(___node,__temp_NT);
            __reducedValues.push(this.action_34(__p));
        }
        break;
        case 35: {
            this.reduceAntecedent(___node,__mem_NT);
            __reducedValues.push(this.action_35(__p));
        }
        break;
        case 36: {
            this.reduceAntecedent(___node,__move_NT);
            __reducedValues.push(this.action_36(__p));
        }
        break;
        case 37: {
            this.reduceAntecedent(___node,__name_NT);
            __reducedValues.push(this.action_37(__p));
        }
        break;
        case 38: {
            this.reduceAntecedent(___node,__moveCall_NT);
            __reducedValues.push(this.action_38(__p));
        }
        break;
        case 39: {
            this.reduceAntecedent(___node,__sxp_NT);
            __reducedValues.push(this.action_39(__p));
        }
        break;
        case 40: {
            this.reduceAntecedent(___node,__binop_NT);
            __reducedValues.push(this.action_40(__p));
        }
        break;
        case 41: {
            this.reduceAntecedent(___node,__integerExpression_NT);
            __reducedValues.push(this.action_41(__p));
        }
        break;
        case 42: {
            this.reduceAntecedent(___node,__jump_NT);
            __reducedValues.push(this.action_42(__p));
        }
        break;
        case 43: {
            this.reduceAntecedent(___node,__expCall_NT);
            __reducedValues.push(this.action_43(__p));
        }
        break;
        default: {
            throw new IllegalStateException("Unmatched reduce action " + iRule);
        }
    }
}


class JBurgAnnotation_BINOP_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_75bd9247(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__integerConstant_NT), this.getNthChild(0).getCost(__exp_NT))));
    }
    private  int getCostForRule_7d417077(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__integerConstant_NT))));
    }
    private  int getCostForRule_7dc36524(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__exp_NT))));
    }
    private  int getCostForRule_35bbe5e8(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__integerConstant_NT), this.getNthChild(0).getCost(__exp_NT))));
    }
    private  int getCostForRule_2c8d66b2(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__integerConstant_NT))));
    }
    private  int getCostForRule_5a39699c(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__integerConstant_NT), this.getNthChild(0).getCost(__integerConstant_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_binopOffsetExpression = -1;private  int cachedCostFor_binop = -1;
    public   JBurgAnnotation_BINOP_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __binop_NT: {
                result = getCostForRule_7dc36524(goalState);
                result = Math.min(result,getCostForRule_35bbe5e8(goalState));
                result = Math.min(result,getCostForRule_2c8d66b2(goalState));
                result = Math.min(result,getCostForRule_5a39699c(goalState));
            }
            break;
            case __binopOffsetExpression_NT: {
                result = getCostForRule_75bd9247(goalState);
                result = Math.min(result,getCostForRule_7d417077(goalState));
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_binop = this.getPatternMatchCost(__binop_NT);
                 int cost_exp = cost_binop;
                result = cost_exp;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __binop_NT: {
                if ((cachedCostFor_binop == -1)) {
                    cachedCostFor_binop = getPatternMatchCost(__binop_NT);
                } 
                result = cachedCostFor_binop;
            }
            break;
            case __binopOffsetExpression_NT: {
                if ((cachedCostFor_binopOffsetExpression == -1)) {
                    cachedCostFor_binopOffsetExpression = getPatternMatchCost(__binopOffsetExpression_NT);
                } 
                result = cachedCostFor_binopOffsetExpression;
            }
            break;
            case __exp_NT: {
                result = getClosureCost(__exp_NT);
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __binop_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_7dc36524(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 22;
                } 
                currentCost = getCostForRule_35bbe5e8(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 23;
                } 
                currentCost = getCostForRule_2c8d66b2(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 24;
                } 
                currentCost = getCostForRule_5a39699c(goalState);
                if ((bestCost > currentCost)) {
                    rule = 25;
                } 
            }
            break;
            case __binopOffsetExpression_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_75bd9247(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 1;
                } 
                currentCost = getCostForRule_7d417077(goalState);
                if ((bestCost > currentCost)) {
                    rule = 2;
                } 
            }
            break;
            case __exp_NT: {
                if ((Integer.MAX_VALUE > getCost(__binop_NT))) {
                    rule = 40;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (2);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            case 1: {
                result = subtree1;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else if ( subtree1 == null ) {
            subtree1 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (BINOP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_BINOP_2");
    }
}



class JBurgAnnotation_CALL_0_n  extends JBurgSpecializedAnnotation 
{
    private  int cachedCostFor_call = -1;private  java.util.Vector<JBurgAnnotation> narySubtrees = new java.util.Vector<JBurgAnnotation>();
    public   JBurgAnnotation_CALL_0_n(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __call_NT: {
                result = normalizedAdd(100, getNaryCost(this,__exp_NT,0));
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __call_NT: {
                if ((cachedCostFor_call == -1)) {
                    cachedCostFor_call = getPatternMatchCost(__call_NT);
                } 
                result = cachedCostFor_call;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __call_NT: {
                if ((Integer.MAX_VALUE > normalizedAdd(100, getNaryCost(this,__exp_NT,0)))) {
                    rule = 29;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (narySubtrees.size());
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            default: {
                result = narySubtrees.get(index);
            }
            break;
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        narySubtrees.add(child);
    }
    public  int getOperator()  {
        return (CALL);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_CALL_0_n");
    }
}



class JBurgAnnotation_CJUMP_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_56cbfb61(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__exp_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_cjump = -1;
    public   JBurgAnnotation_CJUMP_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __cjump_NT: {
                result = getCostForRule_56cbfb61(goalState);
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                 int cost_cjump = this.getPatternMatchCost(__cjump_NT);
                 int cost_stmt = cost_cjump;
                result = cost_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __cjump_NT: {
                if ((cachedCostFor_cjump == -1)) {
                    cachedCostFor_cjump = getPatternMatchCost(__cjump_NT);
                } 
                result = cachedCostFor_cjump;
            }
            break;
            case __stmt_NT: {
                result = getClosureCost(__stmt_NT);
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __cjump_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_56cbfb61(goalState))) {
                    rule = 13;
                } 
            }
            break;
            case __stmt_NT: {
                if ((Integer.MAX_VALUE > getCost(__cjump_NT))) {
                    rule = 32;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (2);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            case 1: {
                result = subtree1;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else if ( subtree1 == null ) {
            subtree1 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (CJUMP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_CJUMP_2");
    }
}



class JBurgAnnotation_CONST_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_CONST_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __integerConstant_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_integerConstant = this.getPatternMatchCost(__integerConstant_NT);
                 int cost_integerExpression = normalizedAdd(1, cost_integerConstant);
                 int cost_exp = cost_integerExpression;
                result = cost_exp;
            }
            break;
            case __integerExpression_NT: {
                 int cost_integerConstant = this.getPatternMatchCost(__integerConstant_NT);
                 int cost_integerExpression = normalizedAdd(1, cost_integerConstant);
                result = cost_integerExpression;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = 2;
            }
            break;
            case __integerConstant_NT: {
                result = 1;
            }
            break;
            case __integerExpression_NT: {
                result = 2;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                rule = 41;
            }
            break;
            case __integerConstant_NT: {
                rule = 19;
            }
            break;
            case __integerExpression_NT: {
                rule = 20;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (CONST);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_CONST_0");
    }
}



class JBurgAnnotation_JUMP_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_1b0375b3(int goalState)  {
        if ((this.getNthChild(0).getArity() == 0) && (this.getNthChild(0).getOperator() == NAME)) {
            return (1);
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_jump = -1;
    public   JBurgAnnotation_JUMP_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __jump_NT: {
                result = getCostForRule_1b0375b3(goalState);
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                 int cost_jump = this.getPatternMatchCost(__jump_NT);
                 int cost_stmt = cost_jump;
                result = cost_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __jump_NT: {
                if ((cachedCostFor_jump == -1)) {
                    cachedCostFor_jump = getPatternMatchCost(__jump_NT);
                } 
                result = cachedCostFor_jump;
            }
            break;
            case __stmt_NT: {
                result = getClosureCost(__stmt_NT);
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __jump_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_1b0375b3(goalState))) {
                    rule = 17;
                } 
            }
            break;
            case __stmt_NT: {
                if ((Integer.MAX_VALUE > getCost(__jump_NT))) {
                    rule = 42;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (1);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (JUMP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_JUMP_1");
    }
}



class JBurgAnnotation_LABEL_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_LABEL_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __label_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                 int cost_label = this.getPatternMatchCost(__label_NT);
                 int cost_stmt = cost_label;
                result = cost_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __label_NT: {
                result = 1;
            }
            break;
            case __stmt_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __label_NT: {
                rule = 15;
            }
            break;
            case __stmt_NT: {
                rule = 33;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (LABEL);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_LABEL_0");
    }
}



class JBurgAnnotation_MEM_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_6bdf28bb(int goalState)  {
        return (normalizedAdd(5, this.getNthChild(0).getCost(__exp_NT)));
    }
    private  int getCostForRule_6b71769e(int goalState)  {
        return (normalizedAdd(4, this.getNthChild(0).getCost(__binopOffsetExpression_NT)));
    }
    private  int getCostForRule_2752f6e2(int goalState)  {
         JBurgAnnotation factoredPath_0 = this.getNthChild(0).getArity() > 0? this.getNthChild(0).getNthChild(0): errorAnnotation;
         JBurgAnnotation factoredPath_1 = this.getNthChild(0).getArity() > 1? this.getNthChild(0).getNthChild(1): errorAnnotation;
        if ((this.getNthChild(0).getArity() == 2) && (this.getNthChild(0).getOperator() == BINOP)) {
            return (normalizedAdd(3, normalizedAdd(factoredPath_1.getCost(__binopOffsetExpression_NT), factoredPath_0.getCost(__exp_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }
    private  int getCostForRule_e580929(int goalState)  {
        return (normalizedAdd(4, this.getNthChild(0).getCost(__binopOffsetExpression_NT)));
    }
    private  int getCostForRule_1cd072a9(int goalState)  {
        return (normalizedAdd(4, this.getNthChild(0).getCost(__integerConstant_NT)));
    }
    private  int getCostForRule_7c75222b(int goalState)  {
        return (normalizedAdd(5, this.getNthChild(0).getCost(__exp_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_indirectWithDisplacementAndScale = -1;private  int cachedCostFor_indirect = -1;private  int cachedCostFor_mem = -1;private  int cachedCostFor_indirectWithDisplacement = -1;
    public   JBurgAnnotation_MEM_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __indirect_NT: {
                result = getCostForRule_6bdf28bb(goalState);
            }
            break;
            case __indirectWithDisplacement_NT: {
                result = getCostForRule_6b71769e(goalState);
            }
            break;
            case __indirectWithDisplacementAndScale_NT: {
                result = getCostForRule_2752f6e2(goalState);
            }
            break;
            case __mem_NT: {
                result = getCostForRule_e580929(goalState);
                result = Math.min(result,getCostForRule_1cd072a9(goalState));
                result = Math.min(result,getCostForRule_7c75222b(goalState));
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_mem = this.getPatternMatchCost(__mem_NT);
                 int cost_exp = cost_mem;
                result = cost_exp;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = getClosureCost(__exp_NT);
            }
            break;
            case __indirect_NT: {
                if ((cachedCostFor_indirect == -1)) {
                    cachedCostFor_indirect = getPatternMatchCost(__indirect_NT);
                } 
                result = cachedCostFor_indirect;
            }
            break;
            case __indirectWithDisplacement_NT: {
                if ((cachedCostFor_indirectWithDisplacement == -1)) {
                    cachedCostFor_indirectWithDisplacement = getPatternMatchCost(__indirectWithDisplacement_NT);
                } 
                result = cachedCostFor_indirectWithDisplacement;
            }
            break;
            case __indirectWithDisplacementAndScale_NT: {
                if ((cachedCostFor_indirectWithDisplacementAndScale == -1)) {
                    cachedCostFor_indirectWithDisplacementAndScale = getPatternMatchCost(__indirectWithDisplacementAndScale_NT);
                } 
                result = cachedCostFor_indirectWithDisplacementAndScale;
            }
            break;
            case __mem_NT: {
                if ((cachedCostFor_mem == -1)) {
                    cachedCostFor_mem = getPatternMatchCost(__mem_NT);
                } 
                result = cachedCostFor_mem;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                if ((Integer.MAX_VALUE > getCost(__mem_NT))) {
                    rule = 35;
                } 
            }
            break;
            case __indirect_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_6bdf28bb(goalState))) {
                    rule = 3;
                } 
            }
            break;
            case __indirectWithDisplacement_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_6b71769e(goalState))) {
                    rule = 4;
                } 
            }
            break;
            case __indirectWithDisplacementAndScale_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_2752f6e2(goalState))) {
                    rule = 5;
                } 
            }
            break;
            case __mem_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_e580929(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 26;
                } 
                currentCost = getCostForRule_1cd072a9(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 27;
                } 
                currentCost = getCostForRule_7c75222b(goalState);
                if ((bestCost > currentCost)) {
                    rule = 28;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (1);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (MEM);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_MEM_1");
    }
}



class JBurgAnnotation_MOVE_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_27f674d(int goalState)  {
         JBurgAnnotation factoredPath_0 = this.getNthChild(0).getArity() > 0? this.getNthChild(0).getNthChild(0): errorAnnotation;
         JBurgAnnotation factoredPath_1 = this.getNthChild(1).getArity() > 0? this.getNthChild(1).getNthChild(0): errorAnnotation;
        if ((this.getNthChild(0).getArity() == 1) && (this.getNthChild(0).getOperator() == MEM) && (this.getNthChild(1).getArity() == 1) && (this.getNthChild(1).getOperator() == MEM)) {
            return (normalizedAdd(3, normalizedAdd(factoredPath_1.getCost(__exp_NT), factoredPath_0.getCost(__exp_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }
    private  int getCostForRule_1d251891(int goalState)  {
        return (normalizedAdd(5, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__indirect_NT))));
    }
    private  int getCostForRule_48140564(int goalState)  {
        return (normalizedAdd(4, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__indirectWithDisplacement_NT))));
    }
    private  int getCostForRule_58ceff1(int goalState)  {
        return (normalizedAdd(4, normalizedAdd(this.getNthChild(1).getCost(__integerConstant_NT), this.getNthChild(0).getCost(__indirectWithDisplacement_NT))));
    }
    private  int getCostForRule_7c30a502(int goalState)  {
        return (normalizedAdd(3, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__indirectWithDisplacementAndScale_NT))));
    }
    private  int getCostForRule_49e4cb85(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__integerConstant_NT), this.getNthChild(0).getCost(__temp_NT))));
    }
    private  int getCostForRule_2133c8f8(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__exp_NT), this.getNthChild(0).getCost(__temp_NT))));
    }
    private  int getCostForRule_43a25848(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__call_NT), this.getNthChild(0).getCost(__exp_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_moveCall = -1;private  int cachedCostFor_move = -1;
    public   JBurgAnnotation_MOVE_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __move_NT: {
                result = getCostForRule_27f674d(goalState);
                result = Math.min(result,getCostForRule_1d251891(goalState));
                result = Math.min(result,getCostForRule_48140564(goalState));
                result = Math.min(result,getCostForRule_58ceff1(goalState));
                result = Math.min(result,getCostForRule_7c30a502(goalState));
                result = Math.min(result,getCostForRule_49e4cb85(goalState));
                result = Math.min(result,getCostForRule_2133c8f8(goalState));
            }
            break;
            case __moveCall_NT: {
                result = getCostForRule_43a25848(goalState);
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                 int cost_move = this.getPatternMatchCost(__move_NT);
                 int cost_moveCall = this.getPatternMatchCost(__moveCall_NT);
                 int cost_stmt = Math.min(cost_move,cost_moveCall);
                result = cost_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __move_NT: {
                if ((cachedCostFor_move == -1)) {
                    cachedCostFor_move = getPatternMatchCost(__move_NT);
                } 
                result = cachedCostFor_move;
            }
            break;
            case __moveCall_NT: {
                if ((cachedCostFor_moveCall == -1)) {
                    cachedCostFor_moveCall = getPatternMatchCost(__moveCall_NT);
                } 
                result = cachedCostFor_moveCall;
            }
            break;
            case __stmt_NT: {
                result = getClosureCost(__stmt_NT);
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __move_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_27f674d(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 6;
                } 
                currentCost = getCostForRule_1d251891(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 7;
                } 
                currentCost = getCostForRule_48140564(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 8;
                } 
                currentCost = getCostForRule_58ceff1(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 9;
                } 
                currentCost = getCostForRule_7c30a502(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 10;
                } 
                currentCost = getCostForRule_49e4cb85(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 11;
                } 
                currentCost = getCostForRule_2133c8f8(goalState);
                if ((bestCost > currentCost)) {
                    rule = 12;
                } 
            }
            break;
            case __moveCall_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_43a25848(goalState))) {
                    rule = 31;
                } 
            }
            break;
            case __stmt_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCost(__move_NT);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 36;
                } 
                currentCost = getCost(__moveCall_NT);
                if ((bestCost > currentCost)) {
                    rule = 38;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (2);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            case 1: {
                result = subtree1;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else if ( subtree1 == null ) {
            subtree1 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (MOVE);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_MOVE_2");
    }
}



class JBurgAnnotation_NAME_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_NAME_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __name_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_name = this.getPatternMatchCost(__name_NT);
                 int cost_exp = cost_name;
                result = cost_exp;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = 1;
            }
            break;
            case __name_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                rule = 37;
            }
            break;
            case __name_NT: {
                rule = 21;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (NAME);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_NAME_0");
    }
}



class JBurgAnnotation_SEQ_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_6a5fc7f7(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__stmt_NT), this.getNthChild(0).getCost(__stmt_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_stmt = -1;
    public   JBurgAnnotation_SEQ_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                result = getCostForRule_6a5fc7f7(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                if ((cachedCostFor_stmt == -1)) {
                    cachedCostFor_stmt = getPatternMatchCost(__stmt_NT);
                } 
                result = cachedCostFor_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __stmt_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_6a5fc7f7(goalState))) {
                    rule = 16;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (2);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            case 1: {
                result = subtree1;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else if ( subtree1 == null ) {
            subtree1 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (SEQ);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_SEQ_2");
    }
}



class JBurgAnnotation_SXP_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_3b6eb2ec(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__exp_NT)));
    }
    private  int getCostForRule_1e643faf(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__call_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_sxp = -1;private  int cachedCostFor_expCall = -1;
    public   JBurgAnnotation_SXP_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expCall_NT: {
                result = getCostForRule_1e643faf(goalState);
            }
            break;
            case __sxp_NT: {
                result = getCostForRule_3b6eb2ec(goalState);
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_expCall = this.getPatternMatchCost(__expCall_NT);
                 int cost_exp = cost_expCall;
                result = cost_exp;
            }
            break;
            case __stmt_NT: {
                 int cost_sxp = this.getPatternMatchCost(__sxp_NT);
                 int cost_stmt = cost_sxp;
                result = cost_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = getClosureCost(__exp_NT);
            }
            break;
            case __expCall_NT: {
                if ((cachedCostFor_expCall == -1)) {
                    cachedCostFor_expCall = getPatternMatchCost(__expCall_NT);
                } 
                result = cachedCostFor_expCall;
            }
            break;
            case __stmt_NT: {
                result = getClosureCost(__stmt_NT);
            }
            break;
            case __sxp_NT: {
                if ((cachedCostFor_sxp == -1)) {
                    cachedCostFor_sxp = getPatternMatchCost(__sxp_NT);
                } 
                result = cachedCostFor_sxp;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                if ((Integer.MAX_VALUE > getCost(__expCall_NT))) {
                    rule = 43;
                } 
            }
            break;
            case __expCall_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_1e643faf(goalState))) {
                    rule = 30;
                } 
            }
            break;
            case __stmt_NT: {
                if ((Integer.MAX_VALUE > getCost(__sxp_NT))) {
                    rule = 39;
                } 
            }
            break;
            case __sxp_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_3b6eb2ec(goalState))) {
                    rule = 14;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (1);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (SXP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_SXP_1");
    }
}



class JBurgAnnotation_TEMP_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_TEMP_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __temp_NT: {
                result = 10;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_temp = this.getPatternMatchCost(__temp_NT);
                 int cost_exp = cost_temp;
                result = cost_exp;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = 10;
            }
            break;
            case __temp_NT: {
                result = 10;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                rule = 34;
            }
            break;
            case __temp_NT: {
                rule = 18;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (TEMP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_TEMP_0");
    }
}


public  JBurgAnnotation getJBurgAnnotation(Tree.IR node)  {
    switch(node.getOperator()) {
        case BINOP: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_BINOP_2(node));
            } 
        }
        break;
        case CALL: {
            if ((node.getArity() >= 0)) {
                return (new JBurgAnnotation_CALL_0_n(node));
            } 
        }
        break;
        case CJUMP: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_CJUMP_2(node));
            } 
        }
        break;
        case CONST: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_CONST_0(node));
            } 
        }
        break;
        case JUMP: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_JUMP_1(node));
            } 
        }
        break;
        case LABEL: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_LABEL_0(node));
            } 
        }
        break;
        case MEM: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_MEM_1(node));
            } 
        }
        break;
        case MOVE: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_MOVE_2(node));
            } 
        }
        break;
        case NAME: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_NAME_0(node));
            } 
        }
        break;
        case SEQ: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_SEQ_2(node));
            } 
        }
        break;
        case SXP: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_SXP_1(node));
            } 
        }
        break;
        case TEMP: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_TEMP_0(node));
            } 
        }
        break;
        default: {
        }
        break;
    }
    return (new PlaceholderAnnotation(node,node.getArity()));
}

private static final JBurgSubgoal[][] ___subgoals_by_rule = 
{
    null,
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__integerConstant_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__integerConstant_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__binopOffsetExpression_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0,0),
        new JBurgSubgoal(__binopOffsetExpression_NT,false, 0,0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1,0)
    },
    {
        new JBurgSubgoal(__indirect_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__indirectWithDisplacement_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__indirectWithDisplacement_NT,false, 0,0),
        new JBurgSubgoal(__integerConstant_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__indirectWithDisplacementAndScale_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__temp_NT,false, 0,0),
        new JBurgSubgoal(__integerConstant_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__temp_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0)
    },
    null,
    {
        new JBurgSubgoal(__stmt_NT,false, 0,0),
        new JBurgSubgoal(__stmt_NT,false, 0,1)
    },
    null,
    null,
    null,
    null,
    null,
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__integerConstant_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__integerConstant_NT,false, 0,0),
        new JBurgSubgoal(__exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__integerConstant_NT,false, 0,0),
        new JBurgSubgoal(__integerConstant_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__binopOffsetExpression_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__integerConstant_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__exp_NT,true, 0)
    },
    {
        new JBurgSubgoal(__call_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__call_NT,false, 0,1)
    },
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
};
final public static int ERROR_TRAP = 268435456;

    /**
     * Reduce a labeled subtree.
     * @param p the annotation describing the root of the subtree.
     * @param goalState the required goal state.
     */
    public void reduce(JBurgAnnotation p , int goalState) throws java.lang.Exception
	{
		reduceAntecedent(p,goalState);
		// Release the annotation's data.
		p.release();
	}

    public void reduceAntecedent(JBurgAnnotation p , int goalState) throws java.lang.Exception
	{
        int iRule  = -1;

		if ( ( goalState != 0 )  ) {
            iRule = p.getRule(goalState);
		} else {
			// Find the minimum-cost path.
            int minCost  = Integer.MAX_VALUE;

            for(int i = 0; i <= nStates; i++)
			{
				if ( ( minCost > p.getCost(i) )  ) {
                    iRule = p.getRule(i);
                    minCost = p.getCost(i);
                    goalState = i;
				}
			}
		}

		if ( ( iRule > 0 )  ) {
            dispatchPrologue(p.getNode(), iRule);
			reduceSubgoals(p, iRule);
			dispatchAction(p, iRule );
		} else {
			throw new IllegalStateException ( String.format("Unable to find a rule to process \"%s\", operator=%s, goal=%s", p, p.getOperator(), goalState) );
		}
	}


    private void dispatchPrologue(Tree.IR p, int iRule)
    {
        switch(iRule)
        {
            case 29: {
                reducer.callPrologue();
            }
            break;
        }
    }
    private void reduceSubgoals(JBurgAnnotation p ,int rule_num) throws java.lang.Exception
	{
		if ( ___subgoals_by_rule[rule_num] != null )
		{
			for ( JBurgSubgoal sg : ___subgoals_by_rule[rule_num] )
			{
				if ( !sg.isNary() )
				{
					reduce ( sg.getNode(p), sg.getGoalState());
				}
				else
				{
					// Aggregate the operands of an n-ary operator into a single container.
					JBurgAnnotation sub_parent = sg.getNode(p);
                    int arity = sub_parent.getArity();
					java.util.Vector<Object> variadic_result = new java.util.Vector<Object>(arity - sg.startIndex);

					for ( int i = sg.startIndex; i < arity; i++ )
					{
						reduce(sub_parent.getNthChild(i), sg.getGoalState());
						variadic_result.add(__reducedValues.pop());
					}
					__reducedValues.push(variadic_result);
				}
			}
		}
	}

    /**
     * Get the cost of an n-ary tail of candidate reductions.
     * @param Node the parent of the candidates.
     * @param goalState the desired goal state.
     * @param startIndex the starting position of
     * the n-ary tail in the parent's children.
     * @return the sum of the n-ary children's costs
     * for the desired goal state, capped at 
     * Integer.MAX_VALUE - 1 if all child costs
     * were strictly less than Integer.MAX_VALUE.
     */
    private int getNaryCost(JBurgAnnotation p , int goalState, int startIndex)
	{
		long accumCost = 0;

		for ( int i = startIndex; accumCost < Integer.MAX_VALUE && i < p.getArity(); i++ )
        {
            // Don't allow the cost of a series of feasible child reductions
            // to rise above MAX_VALUE; the concept of "feasability" and the
            // concept of "cost" need to be disconnected.
            int  childCost = p.getNthChild(i).getCost(goalState);

            if ( childCost >= Integer.MAX_VALUE )
                return Integer.MAX_VALUE;
            else
                accumCost += childCost;
        }

		return accumCost < Integer.MAX_VALUE? (int)accumCost: Integer.MAX_VALUE - 1;
	}
	
    /**
     * Reduce a subtree to the least-cost solution available.
     * @param root the root of the subtree.
     */
    public void burm(Tree.IR root) throws java.lang.Exception
	{
		/* Use the least-cost goal state available. */
		burm(root, 0);
	}
	
    /**
     * Reduce a subtree to a specific goal state.
     * @param root the root of the subtree.
     * @param goal_state the desired goal.
     */
    public void burm(Tree.IR root, int goal_state) throws java.lang.Exception
	{
		JBurgAnnotation annotatedTree = label(root);

        try
        {
            reduce(annotatedTree, goal_state);
        }
        catch ( Exception cant_reduce )
        {
            this.__problemTree = annotatedTree;
            throw cant_reduce;
        }

	}
	
    /**
     * Get the (presumably only) final
     * result of a series of reductions.
     */
    public Object getResult()
	{
		return __reducedValues.pop();
	}

    /**
     * @return the sum of the input costs, normalized as follows:
     * <li>If either parameter equals Integer.MAX_VALUE, return Integer.MAX_VALUE.
     * <li>If the sum is less than Integer.MAX_VALUE, return the sum.
     * <li>Otherwise return Integer.MAX_VALUE-1.
     */
    public int normalizedAdd(int c1, int c2)
    {
        int result = Integer.MAX_VALUE;

        if ( c1 < Integer.MAX_VALUE && c2 < Integer.MAX_VALUE )
        {
            long accum = (long)c1 + (long)c2;
            result = accum < Integer.MAX_VALUE?
                (int)accum:
                Integer.MAX_VALUE-1;
        }

        return result;
    }

	private JBurgAnnotation __problemTree;
	
    public void dump(java.io.PrintWriter debug_output)
	{
		if ( null == __problemTree )
		{
			debug_output.print("<bailed reason=\"no problem tree\"/>");
			
			return;
		}
		debug_output.print("<jburg>");
		dumpSubgoals(debug_output);
		debug_output.print("<label>");
		describeNode(__problemTree,debug_output);
		debug_output.print("</label></jburg>");
	}
	
    public void dump(Tree.IR node,java.io.PrintWriter debug_output)
	{
		JBurgAnnotation anno = label(node);
		debug_output.println("<?xml version=\"1.0\"?>");
		debug_output.println("<BurmDump date=\"" + new java.util.Date() + "\">");
		debug_output.println("<jburg>");
		dumpSubgoals(debug_output);
		debug_output.println("<label>");
		describeNode(anno,debug_output);
		debug_output.println("</label>\n</jburg>\n</BurmDump>");
		debug_output.flush();
	
	}

    public void describeNode(JBurgAnnotation node,java.io.PrintWriter debug_output)
	{
		if ( node == null )
            return;

		String self_description = node.getSelfDescription();
		try {
			self_description = java.net.URLEncoder.encode(self_description,"UTF-8");
		
		} catch ( Exception cant_encode ) {
            // Just use the description as given.
		}

		debug_output.printf("<node operator=\"%d\" selfDescription=\"%s\"/>", node.getOperator(), self_description);

		for(int i = 0; i <= nStates; i++) {

			if ( node.getCost(i) < Integer.MAX_VALUE ) {

				debug_output.print ( "<goal");
				debug_output.print ( " name=\"" + stateName[i] + "\"");
				debug_output.print ( " rule=\"" + node.getRule(i) + "\"");
				debug_output.print ( " cost=\"" + node.getCost(i) + "\"");
				debug_output.println ( "/>" );
			}
		}

		for (int i = 0; i < node.getArity(); i++ )
            describeNode ( node.getNthChild(i),debug_output);
		debug_output.println ( "</node>" );
	
	}

    private void dumpSubgoals(java.io.PrintWriter debug_output)
	{
		for ( int rule_num = 0; rule_num < ___subgoals_by_rule.length; rule_num++ )
        {
            if ( ___subgoals_by_rule[rule_num] != null )
            {
                debug_output.printf("<subgoals rule=\"%d\">\n", rule_num);

                for ( JBurgSubgoal sg : ___subgoals_by_rule[rule_num] )
                {
                    debug_output.printf("<subgoal goal=\"%s\" nary=\"%s\" startIndex=\"%s\"", stateName[sg.getGoalState()], sg.isNary(), sg.startIndex);

                    if ( sg.accessPath.length > 0 )
                    {
                        debug_output.println(">");
                        for ( int idx: sg.accessPath )
                        debug_output.printf("<accessPath index=\"%d\"/>\n", idx);
                        debug_output.printf("</subgoal>\n");
                    }
                    else
                    {
                        debug_output.println("/>");
                    }
                }
                debug_output.printf("</subgoals>\n");
            }
		}
	}

    static final String[] stateName = new String[] { null, "move","temp","indirect","sxp","label","integerExpression","call","moveCall","indirectWithDisplacementAndScale","cjump","mem","indirectWithDisplacement","binopOffsetExpression","name","integerConstant","exp","stmt","binop","expCall","jump" };

    	/** 
         *  JBurgAnnotation is a data structure internal to the
    	 *  JBurg-generated BURM that annotates a Tree.IR with
    	 *  information used for dynamic programming and reduction.
    	 */
    	public abstract static class JBurgAnnotation
    	{
    		/**  The Tree.IR this JBurgAnnotation describes.  */
    		Tree.IR m_node; 
    		JBurgAnnotation ( Tree.IR newNode)
    		{
    			m_node = newNode;
    		}

    		/** @return this node's wrapped Tree.IR. */ 
    		public Tree.IR getNode()  
    		{
    			return m_node; 
    		}

    		/** @return this node's operator. */
    		public int getOperator() 
    		{
    			return m_node.getOperator();
    		}

            public String getSelfDescription()
            {
                return "?";
            }

    		/** @return the nth child of this node.  */
    		public abstract JBurgAnnotation getNthChild(int idx);

    		/** @return this node's child count.  */
    		public abstract int getArity();

    		/** Add a new child to this node.  */
    		public abstract void addChild(JBurgAnnotation new_child);

    		/** Release this node's data.  */
    		public abstract void release();

    		/** @return the wrapped node's toString().  */
    		public String toString() 
    		{
    			return m_node.toString(); 
    		}

    		/** @return the current best cost to reach a goal state.  */
    		public abstract int getCost( int goalState ) ;

    		/** * @return the rule to fire for a specific goal state. */
    		public abstract int getRule ( int goalState ) ;

    		/**
    		 *  A closure's transformation rule succeeded.
    		 *  If this path is selected for reduction, then all the actions  must be run in sequence, beginning with the original;
    		 *  so the order of the rules matters.  We disallow transformation rules with  cycles (a node should never 
    		 *  transition back to a goal state that has already been reduced).*/
    		public abstract void recordAntecedent ( int iGoalState, int newAntecedentState );
    	
    	}

    /**
     * Common superclass of node-specific annotations.
     */
	abstract static class JBurgSpecializedAnnotation extends JBurgAnnotation
	{
		JBurgSpecializedAnnotation(Tree.IR node)
		{
			super(node);
		}

		public JBurgAnnotation getNthChild(int idx)
		{
			throw new IllegalStateException(this.getClass().getName() + " has no children.");
		}

		public void addChild(JBurgAnnotation new_child)
		{
			throw new IllegalStateException(this.getClass().getName() + " cannot have children.");
		}

		public void release ()
		{
		}

		public void recordAntecedent ( int iGoalState, int newAntecedentState )
		{
			throw new IllegalStateException(this.getClass().getName() + " cannot record antecedents.");
		}
	}

    /**
     * An annotation that denotes an unused node in the tree.
     */
	private static class PlaceholderAnnotation extends JBurgSpecializedAnnotation
	{
		PlaceholderAnnotation(Tree.IR node, int capacity)
		{
			super(node);
            this.children = new JBurgAnnotation[capacity];
		}

        JBurgAnnotation [] children;
        int actualChildCount = 0;

		public int getCost(int state)     { return Integer.MAX_VALUE; }
		public int getRule(int state)     { return -1; }
		public int getArity()                           { return actualChildCount; }

		public JBurgAnnotation getNthChild(int idx)     { return children[idx]; }
		public void addChild(JBurgAnnotation newChild)  { children[actualChildCount++] = newChild; }
	}

    /**
     * An annotation that describes a pattern-match failure.
     */
    private final static JBurgAnnotation errorAnnotation = new JBurgSpecializedAnnotation(null)
    {
		public int getRule(int state) { return -1; }
		public int getCost(int state) { return Integer.MAX_VALUE; }
		public int getArity()                       { return 0; }
    };

    /**
     * JBurgSubgoal describes a possible reduction of a subtree.
     */
	static class JBurgSubgoal
	{
        /** The goal state that this subgoal produces. */
		private int goalState;
        /** Is this subgoal an n-ary tail? */
		private boolean isNary;
        /** If this is an n-ary subgoal, where does it start? */
		private int startIndex ;
        /** Path from the ancestor node to the root of the subtree. */
		private int[] accessPath ;

        public  JBurgSubgoal(int goal_state,boolean is_nary,int start_index,int... access_path)
		{
			this.goalState = goal_state;
			this.isNary = is_nary;
			this.startIndex = start_index;
			this.accessPath = access_path;
		}
		public int getGoalState() { return this.goalState; }
		public boolean isNary() { return this.isNary; }
        /**
         * Traverse the access path from the ancestor subtree
         * to the root of this subgoal's subtree.
         * @param ancestor the ancestor node.
         */
		public JBurgAnnotation getNode(JBurgAnnotation ancestor)
		{
			JBurgAnnotation result = ancestor;
			for ( int idx: this.accessPath )
				result = result.getNthChild(idx);
			return result;
		}
	
	}
}
