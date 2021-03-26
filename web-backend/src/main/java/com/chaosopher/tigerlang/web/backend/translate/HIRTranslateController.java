package com.chaosopher.tigerlang.web.backend.translate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.bind.Binder;
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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Translates the supplied tiger source code into its HIR representation.
 */
@RestController
@CrossOrigin(origins = "*")
public class HIRTranslateController {

    @PostMapping("/translate")
    public ResponseEntity<TranslateResponse> translate(@RequestBody TranslateRequest translateRequest) {
        // instream for source code
        InputStream in = new ByteArrayInputStream(translateRequest.getCode().getBytes());
        // outstream is not used.
        ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(backingErrorStream);
        // the file name.
        ErrorMsg errorMsg = new ErrorMsg(translateRequest.getFileName(), err);
        ParserService parserService = new ParserService(new ParserFactory());
        // attempt to parse the file and check for errors.
        DecList decList = parserService.parse(in, errorMsg);
        if (errorMsg.anyErrors) {
            return ResponseEntity.badRequest().build();
        }
        // populate symbol tables.
        decList.accept(new Binder(errorMsg));
        // run type checker on the file.
        decList.accept(new TypeChecker(errorMsg));
        if (errorMsg.anyErrors) {
            return ResponseEntity.badRequest().build();
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
        
        TranslatorVisitor translatorVisitor = TranslatorVisitor.apply(decList);
        FragList fragList = translatorVisitor.getFragList();
        //get the source map and generate response.
        Map<IR, Absyn> sourceMap = translatorVisitor.getSourceMap();
        TranslateResponse jsonIR = new TranslateResponse(sourceMap);
        fragList.accept(jsonIR);
        // return response.
        return ResponseEntity.ok(jsonIR);
    }
}
