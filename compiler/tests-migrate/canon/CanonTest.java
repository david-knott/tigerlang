package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Before;


public class CanonTest {

    private ParserService parserService;
    private CanonVisitor canonVisitor;

    public CanonTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Before
    public void setup() {
        this.canonVisitor = new CanonVisitor(new CanonicalizationImpl());
    }
}