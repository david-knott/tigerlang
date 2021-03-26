package com.chaosopher.tigerlang.web.backend.absyn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.TigerSource;

/**
 * Pretty prints the suppled source code if it is syntactically valid. If source
 * is invalid a client error is returned.
 * 
 * @return
 */
@RestController
@CrossOrigin(origins = "*")
public class PrettyPrinterController {

    @PostMapping("/prettyPrint")
    public ResponseEntity<String> prettyPrint(@RequestBody TigerSource tigerSource) {
        // instream for source code
        InputStream in = new ByteArrayInputStream(tigerSource.getCode().getBytes());
        // outstream is not used.
        ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(backingErrorStream);
        // the file name.
        ErrorMsg errorMsg = new ErrorMsg(tigerSource.getFileName(), err);
        ParserService parserService = new ParserService(new ParserFactory());
        parserService.configure(f -> f.setNoPrelude(true));
        // attempt to parse the file.
        DecList decList = parserService.parse(in, errorMsg);
        if (errorMsg.anyErrors) {
            return ResponseEntity.badRequest().build();
        }
        if (errorMsg.anyErrors) {
            return ResponseEntity.badRequest().build();
        }
        // all is well, pretty print the source.
        ByteArrayOutputStream backingOutputStream = new ByteArrayOutputStream();
        PrettyPrinter prettyPrinter = new PrettyPrinter(new PrintStream(backingOutputStream),
                tigerSource.isEscapesDisplay(), tigerSource.isBindingsDisplay());
        decList.accept(prettyPrinter);
        return ResponseEntity.ok(backingOutputStream.toString());
    }
}