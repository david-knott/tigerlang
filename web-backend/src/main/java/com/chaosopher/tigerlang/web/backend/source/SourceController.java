package com.chaosopher.tigerlang.web.backend.source;

import java.util.List;

import com.chaosopher.tigerlang.web.backend.services.SourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class SourceController {

    @Autowired
    private SourceRepository sourceRepository;

    @GetMapping("/source")
    public ResponseEntity<List<Source>> all() {
        List<Source> sources = this.sourceRepository.findAll();
        return ResponseEntity.ok(sources);
    }

    @GetMapping("/source/{fileName}")
    public ResponseEntity<Source> load(@PathVariable String fileName) {
        Source source = this.sourceRepository.findByName(fileName);
        return source != null ? ResponseEntity.ok(sourceRepository.findByName(fileName))
                : ResponseEntity.notFound().build();
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
