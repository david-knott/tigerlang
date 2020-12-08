package com.chaosopher.tigerlang.compiler.type;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.ARRAY;
import com.chaosopher.tigerlang.compiler.types.Constants;
import com.chaosopher.tigerlang.compiler.types.INT;
import com.chaosopher.tigerlang.compiler.types.NAME;
import com.chaosopher.tigerlang.compiler.types.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.types.RECORD;
import com.chaosopher.tigerlang.compiler.types.STRING;


public class PrettyPrinterTest {

    @Test
    public void intprinter() {
        INT tint = Constants.INT;
        tint.accept(new PrettyPrinter(System.out));
    }
     
    @Test
    public void stringprinter() {
        STRING tstring = Constants.STRING;
        tstring.accept(new PrettyPrinter(System.out));
    }
     
    @Test
    public void nameprinter() {
        INT tint = Constants.INT;
        NAME tname = new NAME(Symbol.symbol("tname"));
        tname.bind(tint);
        tname.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void recordprinter() {
        STRING tstring = Constants.STRING;
        INT tint = Constants.INT;
        RECORD record = new RECORD(Symbol.symbol("sfield"), tstring, new RECORD(Symbol.symbol("ifield"), tint, null));
        record.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void arrayprinter() {
        STRING tstring = Constants.STRING;
        ARRAY tarray = new ARRAY(tstring);
        tarray.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedrecordprinter() {
        STRING tstring = Constants.STRING;
        ARRAY tarray = new ARRAY(tstring);
        INT tint = Constants.INT;
        RECORD record = new RECORD(Symbol.symbol("sfield"), tarray, new RECORD(Symbol.symbol("ifield"), tint, null));
        record.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedarrayprinter() {
        STRING tstring = Constants.STRING;
        INT tint = Constants.INT;
        RECORD record = new RECORD(Symbol.symbol("sfield"), tstring, new RECORD(Symbol.symbol("ifield"), tint, null));
        ARRAY tarray = new ARRAY(record);
        tarray.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestednameprinter() {
        STRING tstring = Constants.STRING;
        INT tint = Constants.INT;
        RECORD record = new RECORD(Symbol.symbol("sfield"), tstring, new RECORD(Symbol.symbol("ifield"), tint, null));
        NAME tname = new NAME(Symbol.symbol("tname"));
        tname.bind(record);
        tname.accept(new PrettyPrinter(System.out));
    }
}
