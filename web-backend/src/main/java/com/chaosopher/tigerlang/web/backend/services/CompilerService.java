package com.chaosopher.tigerlang.web.backend.services;

import java.util.Map;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.web.backend.CompilerRequest;

public interface CompilerService {

    void parse(CompilerRequest translateRequest);

    boolean hasErrors();

    void bindAndTypeCheck();

    void hirTranslate();

    Map<IR, Absyn> getSourceMap();

    FragList getFragList();
}
