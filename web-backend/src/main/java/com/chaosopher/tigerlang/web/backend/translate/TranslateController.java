package com.chaosopher.tigerlang.web.backend.translate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.TigerSource;

/**
 * Translates the supplied tiger source code into its LIR representation.
 */
@RestController
@CrossOrigin(origins = "*")
public class TranslateController {

    @PostMapping("/translate")
    public ResponseEntity<String> translate(@RequestBody TigerSource tigerSource) {
        // instream for source code
        InputStream in = new ByteArrayInputStream(tigerSource.getCode().getBytes());
        // outstream is not used.
        ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(backingErrorStream);
        // the file name.
        ErrorMsg errorMsg = new ErrorMsg(tigerSource.getFileName(), err);
        ParserService parserService = new ParserService(new ParserFactory());
        // attempt to parse the file and check for errors.
        DecList decList = parserService.parse(in, errorMsg);
        if (errorMsg.anyErrors) {
            return ResponseEntity.badRequest().build();
        }
        // run type checker on the file.
        decList.accept(new TypeChecker(errorMsg));
        if (errorMsg.anyErrors) {
            return ResponseEntity.badRequest().build();
        }
        // desugar the ast prior to translation
        // might like to see this ?
        if(tigerSource.isDesugar()) {

        }
        // compute variables that can be stored in temporaries
        if(tigerSource.isEscapesCompute()) {

        }
        // inline functions
        // might like to see this ?
        if(tigerSource.isInline()) {

        }
        // prune unused functions
        // might like to see this ?
        if(tigerSource.isPrune()) {

        }
        // compute static links that must be stored in memory ( escapes )
        if(tigerSource.isStaticLinkEscapes()) {

        }
        // compute functions that do not need a static link
        if(tigerSource.isStaticLinks()) {

        }
        TranslatorVisitor translatorVisitor = new TranslatorVisitor();
        decList.accept(translatorVisitor);
        FragList fragList = translatorVisitor.getFragList();
        // for each proc frag we need to get the corresponding ast node and relate the
        // the line number to the translated fragment.
        fragList.accept(new FragmentVisitor(){

            @Override
            public void visit(ProcFrag procFrag) {
                // iterate through each statement and get its corresponding ast node.
                
            }

            @Override
            public void visit(DataFrag dataFrag) {
                // TODO Auto-generated method stub
                
            }
            
        });
        // iterate through the fraglist generating json
        

        return ResponseEntity.ok().build();
    }
}
