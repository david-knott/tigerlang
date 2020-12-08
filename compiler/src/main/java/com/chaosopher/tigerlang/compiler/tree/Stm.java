package com.chaosopher.tigerlang.compiler.tree;

abstract public class Stm extends IR {
	abstract public ExpList kids();

	abstract public Stm build(ExpList kids);

}