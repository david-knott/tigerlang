package com.chaosopher.tigerlang.compiler.parse;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;

public interface Parser {

    public Absyn parse();

    public boolean hasErrors();

	public void setParserTrace(boolean value);
}