package com.chaosopher.tigerlang.compiler.parse;

import java.io.InputStream;

import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;


/**
 * ParserFactory creates a new configured parser instance using a specific
 * input stream and @see ErrorMsg.ErrorMsg 
 */
public class ParserFactory {
    
    private boolean parserTrace = false;

    Parser getParser(InputStream inputStream, ErrorMsg errorMsg) {
        CupParser cupParser = new CupParser(inputStream, errorMsg);
        cupParser.parserTrace = parserTrace;
        return cupParser;
    }

	public void setParserTrace(boolean b) {
        this.parserTrace = b;
	}
}