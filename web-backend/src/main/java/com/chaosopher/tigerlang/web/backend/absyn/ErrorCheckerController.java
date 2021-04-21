package com.chaosopher.tigerlang.web.backend.absyn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMessage;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;
import com.chaosopher.tigerlang.web.backend.test.TigerSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ErrorCheckerController {

    /**
     * Preforms type and syntax checking on the supplied source code.
     * Returns a json array of errors with their associated line numbers.
     * @return
     */
	@PostMapping("/errorCheck")
    public ResponseEntity<List<ErrorMessage>> errorCheck(@RequestBody TigerSource tigerSource) {
        // instream for source code
        InputStream in = new ByteArrayInputStream(tigerSource.getCode().getBytes());
        // outstream is not used.
		ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
		PrintStream err = new PrintStream(backingErrorStream);
        // the file name.
		ErrorMsg errorMsg = new ErrorMsg(tigerSource.getFileName(), err);
		ParserService parserService = new ParserService(new ParserFactory());
        // attempt to parse the file.
        DecList decList = parserService.parse(in, errorMsg);
        if(errorMsg.anyErrors) {
            return ResponseEntity.ok(errorMsg.getErrors());
        }
        // populate symbol tables.
        decList.accept(new Binder(errorMsg));
        // run type checker on the file.
        decList.accept(new TypeChecker(errorMsg));
        if(errorMsg.anyErrors) {
            return ResponseEntity.ok(errorMsg.getErrors());
        }
        // all is well, return a no content ok message.
        return ResponseEntity.ok(Collections.emptyList());
    }
}