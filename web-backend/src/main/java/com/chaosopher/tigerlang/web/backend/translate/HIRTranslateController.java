package com.chaosopher.tigerlang.web.backend.translate;

import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.web.backend.services.CompilerService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    CompilerService compilerService;

    /**
     * Test Request: curl -X POST  localhost:8080/hirTtranslate -H 'Content-type:application/json' -d '{"escapesCompute" : true, "code" : "var a:string := \"David\"\nvar b:int := 2"}' 
     * @param translateRequest
     * @return a json response containing IR along with a basic source map.
     */
    @PostMapping("/hirTranslate")
    public ResponseEntity<TranslateResponse> translate(@RequestBody TranslateRequest translateRequest) {
        this.compilerService.parse(translateRequest);
        if(this.compilerService.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        this.compilerService.bindAndTypeCheck();
        if(this.compilerService.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        this.compilerService.hirTranslate();
        TranslateResponse jsonIR = new TranslateResponse(this.compilerService.getSourceMap());
        FragList fragList = this.compilerService.getFragList();
        fragList.accept(jsonIR);
        // return the populated response
        return ResponseEntity.ok(jsonIR);
    }
}
