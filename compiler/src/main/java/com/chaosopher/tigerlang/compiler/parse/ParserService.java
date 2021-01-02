package com.chaosopher.tigerlang.compiler.parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.ExpList;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * A parser service that returns a configured parser. It uses
 * a factory to contruct the parser and a configuration to
 * set the parser.
 */
public class ParserService {

    private final ParserFactory parserFactory;
    private final ParserServiceConfiguration parserServiceConfiguration;

    public ParserService(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
        this.parserServiceConfiguration = new ParserServiceConfiguration();
    }

    public void configure(ParserServiceConfigurator parserServiceConfigurator) {
        parserServiceConfigurator.configure(parserServiceConfiguration);
        parserFactory.setParserTrace(parserServiceConfiguration.isParserTrace());
    }

    /**
     * Parses a string containing tiger code and returns a declaration list.
     * Internally this uses the overloaded parse(InputStream, ErrorMsg) method.
     * 
     * @param code
     * @param errorMsg
     * @return
     */
    public DecList parse(String code, ErrorMsg errorMsg) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(code.getBytes(Charset.defaultCharset()));
        return parse(byteArrayInputStream, errorMsg);
    }

    /**
     * Parse an input stream using the parser returned by the parser factory.
     * @param inputStream the input stream containing the source code.
     * @param errorMsg contains error messages.
     * @return returns a @see com.chaosopher.tigerlang.compiler.absyn.DecList
     */
    public DecList parse(InputStream inputStream, ErrorMsg errorMsg) {
        // parse the program. This is one top level function that contains user code.
        Parser parser = parserFactory.getParser(inputStream, errorMsg);
        Absyn tree = parser.parse();
        DecList program = null;
        // if passed a declist, wrap in let exp and function dec.
        if (tree instanceof DecList) {
            program = new DecList(new FunctionDec(0, Symbol.symbol("tigermain"), null, null,
                    new LetExp(0, (DecList) tree, null), null), null);
        }
        // if passed an exp, wrap in function dec, and append an empty () to tree. This is
        // ensure that the expression evaluates to a void.
        if (tree instanceof Exp) {
            SeqExp seqExp = new SeqExp(
                0, 
                new ExpList(
                    (Exp)tree, 
                    new ExpList(
                        new SeqExp(
                            0,
                            null
                        ),
                        null
                    )
                )
            );
            program = new DecList(new FunctionDec(0, Symbol.symbol("tigermain"), null, null, seqExp, null), null);
        }
        if (parserServiceConfiguration.isNoPrelude()) {
            return program;
        }
        Parser prelude;
        InputStream in = getClass().getResourceAsStream("/data/prelude.tih");
        if(null == in) {
            Assert.unreachable();
        }
        prelude = parserFactory.getParser(in, errorMsg);
        DecList preludeList = (DecList) prelude.parse();
        // append the user code to end of prelude declarations.
        DecList end = preludeList;
        if (end != null) {
            for (; end.tail != null; end = end.tail)
                ;
            end.tail = program;
        }
        return preludeList;
    }
}