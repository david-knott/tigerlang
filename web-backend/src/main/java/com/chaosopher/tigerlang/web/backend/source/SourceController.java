package com.chaosopher.tigerlang.web.backend.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import com.chaosopher.tigerlang.web.backend.services.CompilerService;
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

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
public class SourceController {
    
    @GetMapping("/source/{fileName}")
    public ResponseEntity<String> load(@PathVariable String fileName) {

        return ResponseEntity.ok("some program");
    }

    @PutMapping("/source/{fileName}")
    public ResponseEntity<String> save(@PathVariable String fileName, @RequestBody String source) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/source/{fileName}")
    public ResponseEntity<String> remove(@PathVariable String fileName) {

        return ResponseEntity.ok().build();
    }
}
