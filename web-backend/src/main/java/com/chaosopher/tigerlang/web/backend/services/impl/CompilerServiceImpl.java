package com.chaosopher.tigerlang.web.backend.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMessage;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.staticlink.FunctionStaticLinkVisitor;
import com.chaosopher.tigerlang.compiler.staticlink.StaticLinkEscapeVisitor;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;
import com.chaosopher.tigerlang.web.backend.CompilerRequest;
import com.chaosopher.tigerlang.web.backend.services.CompilerService;

import org.springframework.stereotype.Service;

@Service
class CompilerServiceImpl implements CompilerService {

    private DecList decList;
    private ErrorMsg errorMsg;
    private FragList fragList;
    private Map<IR, Absyn> sourceMap;
    
    public void parse(CompilerRequest translateRequest) {
        // instream for source code
        InputStream in = new ByteArrayInputStream(translateRequest.getCode().getBytes());
        // outstream is not used.
        ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(backingErrorStream);
        // the file name.
        this.errorMsg = new ErrorMsg(translateRequest.getFileName(), err);
        ParserService parserService = new ParserService(new ParserFactory());
        // attempt to parse the file and check for errors.
        this.decList = parserService.parse(in, this.errorMsg);
        if (errorMsg.anyErrors) {
            return;
        }
        // compute variables that can be stored in temporaries
        if(translateRequest.isEscapesCompute()) {
            EscapeVisitor.apply(errorMsg, decList);
        }
        // compute static links that must be stored in memory ( escapes )
        if(translateRequest.isStaticLinkEscapes()) {
            StaticLinkEscapeVisitor.apply(decList);
        }
        // compute functions that do not need a static link
        if(translateRequest.isStaticLinks()) {
            decList.accept(new FunctionStaticLinkVisitor());
        }
        // desugar the ast prior to translation
        if(translateRequest.isDesugar()) {

        }
        // inline functions
        if(translateRequest.isInline()) {

        }
        // prune unused functions
        if(translateRequest.isPrune()) {

        }
    }

    public Map<IR, Absyn> getSourceMap() {
        return sourceMap;
    }

    private void setSourceMap(Map<IR, Absyn> sourceMap) {
        this.sourceMap = sourceMap;
    }

    public FragList getFragList() {
        return fragList;
    }

    private void setFragList(FragList fragList) {
        this.fragList = fragList;
    }

    public List<ErrorMessage> getErrors() {
        return this.errorMsg.getErrors();
    }

    public void bindAndTypeCheck() {
        // populate symbol tables.
        decList.accept(new Binder(this.errorMsg));
        // run type checker on the file.
        TypeChecker.create(decList, this.errorMsg);
       // decList.accept(new TypeChecker(this.errorMsg));
    }

    public void hirTranslate() {
        TranslatorVisitor translatorVisitor = TranslatorVisitor.apply(this.decList);
        this.setFragList(translatorVisitor.getFragList());
        this.setSourceMap(translatorVisitor.getSourceMap());
    }

    @Override
    public boolean hasErrors() {
        return this.getErrors().size() != 0;
    }
}