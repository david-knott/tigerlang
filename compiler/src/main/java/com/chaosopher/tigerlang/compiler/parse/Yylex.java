package com.chaosopher.tigerlang.compiler.parse;


public class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

private void newline() {
  errorMsg.newline(yychar);
}
private void err(int pos, String s) {
  errorMsg.error(pos,s);
}
private void err(String s) {
  err(yychar,s);
}
private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}
private com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg errorMsg;
private int commentDepth = 0;
private String buffer = "";
public Yylex(java.io.InputStream s, com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg e) {
  this(s);
  errorMsg=e;
}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private void yy_do_eof () {
		if (false == yy_eof_done) {

  if(this.commentDepth != 0) {
    errorMsg.error(yychar, "unexpected end of file in a comment.");
  }
  if(this.buffer.length() != 0) {
    errorMsg.error(yychar, "unexpected end of file in a string literal.");
  }
		}
		yy_eof_done = true;
	}
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		66,
		71
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NOT_ACCEPT,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"47:9,45,44,47:2,48,47:18,49,47,43,47:3,39,47,28,29,35,33,26,34,32,36,42:10," +
"24,27,37,25,38,47:2,41:26,22,46,23,47,41,47,11,10,19,16,5,6,41,2,3,41,12,4," +
"20,13,7,15,41,8,17,9,18,21,1,41,14,41,30,40,31,47:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,116,
"0,1,2,1:2,3,1:11,4,5,6,1:2,7,1:5,8:5,1:6,8:13,1:10,9,10,11,12,13,14,15,16,1" +
"7,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,4" +
"2,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,8,59,60,61")[0];

	private int yy_nxt[][] = unpackFromString(62,50,
"1,2,112,63,86,87,88,67,112,70,113,114,112,89,112,115,72,112:4,90,3,4,5,6,7," +
"8,9,10,11,12,13,14,15,16,17,18,19,20,21,112,22,23,24,25,26:2,62,27,-1:51,11" +
"2,99,112:19,-1:19,112,100,-1:32,33,-1:59,34,-1:39,35,-1:12,36,-1:36,37,-1:6" +
"6,22,-1:8,112:21,-1:19,112,100,-1:51,38,-1:6,112:5,28,112:6,29,112:8,-1:19," +
"112,100,-1:43,54,-1:56,61,-1:6,1,52:34,64,68,52:7,53,52:3,69,52,-1,112:5,30" +
",112:15,-1:19,112,100,-1:42,55,-1:58,56,-1:6,112,92,112:4,31,112:6,93,112:7" +
",-1:19,112,100,-1:7,1,57:42,58,59,60,65,57,-1,57,-1,112:6,32,112:14,-1:19,1" +
"12,100,-1:8,112:8,39,112:12,-1:19,112,100,-1:8,112:15,40,112:5,-1:19,112,10" +
"0,-1:8,112:7,41,112:13,-1:19,112,100,-1:8,112:3,42,112:17,-1:19,112,100,-1:" +
"8,112:7,43,112:13,-1:19,112,100,-1:8,112:4,44,112:16,-1:19,112,100,-1:8,112" +
":12,45,112:8,-1:19,112,100,-1:8,112:4,46,112:16,-1:19,112,100,-1:8,112:4,47" +
",112:16,-1:19,112,100,-1:8,112:11,48,112:9,-1:19,112,100,-1:8,112:13,49,112" +
":7,-1:19,112,100,-1:8,112:12,50,112:8,-1:19,112,100,-1:8,112:4,51,112:16,-1" +
":19,112,100,-1:8,112:4,73,112:16,-1:19,112,100,-1:8,112:3,91,112:8,74,112:8" +
",-1:19,112,100,-1:8,112:6,75,112:10,101,112:3,-1:19,112,100,-1:8,112:2,76,1" +
"12:18,-1:19,112,100,-1:8,112:10,77,112:10,-1:19,112,100,-1:8,112:16,78,112:" +
"4,-1:19,112,100,-1:8,112:4,79,112:16,-1:19,112,100,-1:8,112:14,80,112:6,-1:" +
"19,112,100,-1:8,112:3,81,112:17,-1:19,112,100,-1:8,112:10,82,112:10,-1:19,1" +
"12,100,-1:8,112:10,83,112:10,-1:19,112,100,-1:8,112:6,84,112:14,-1:19,112,1" +
"00,-1:8,112:20,85,-1:19,112,100,-1:8,112:2,94,112:18,-1:19,112,100,-1:8,100" +
":21,-1:19,100:2,-1:8,112:12,105,112:8,-1:19,112,100,-1:8,112:4,95,112:16,-1" +
":19,112,100,-1:8,112:7,96,112:13,-1:19,112,100,-1:8,112:2,106,112:18,-1:19," +
"112,100,-1:8,112:18,107,112:2,-1:19,112,100,-1:8,112:19,108,112,-1:19,112,1" +
"00,-1:8,112:8,109,112:12,-1:19,112,100,-1:8,112:2,110,112:18,-1:19,112,100," +
"-1:8,112:2,97,112:18,-1:19,112,100,-1:8,112:8,111,112:12,-1:19,112,100,-1:8" +
",112:2,98,112:18,-1:19,112,100,-1:8,112:7,102,112:13,-1:19,112,100,-1:8,112" +
":7,103,112:13,-1:19,112,100,-1:8,112:7,104,112:13,-1:19,112,100,-1:7");

	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				yy_do_eof();

{
  return tok(sym.EOF, null);
}
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return tok(sym.ID, yytext());}
					case -3:
						break;
					case 3:
						{return tok(sym.LBRACK, null);}
					case -4:
						break;
					case 4:
						{return tok(sym.RBRACK, null);}
					case -5:
						break;
					case 5:
						{return tok(sym.COLON, null);}
					case -6:
						break;
					case 6:
						{return tok(sym.EQ, null);}
					case -7:
						break;
					case 7:
						{return tok(sym.COMMA, null);}
					case -8:
						break;
					case 8:
						{return tok(sym.SEMICOLON, null);}
					case -9:
						break;
					case 9:
						{return tok(sym.LPAREN, null);}
					case -10:
						break;
					case 10:
						{return tok(sym.RPAREN, null);}
					case -11:
						break;
					case 11:
						{return tok(sym.LBRACE, null);}
					case -12:
						break;
					case 12:
						{return tok(sym.RBRACE, null);}
					case -13:
						break;
					case 13:
						{return tok(sym.DOT, null);}
					case -14:
						break;
					case 14:
						{return tok(sym.PLUS, null);}
					case -15:
						break;
					case 15:
						{return tok(sym.MINUS, null);}
					case -16:
						break;
					case 16:
						{return tok(sym.TIMES, null);}
					case -17:
						break;
					case 17:
						{return tok(sym.DIVIDE, null);}
					case -18:
						break;
					case 18:
						{return tok(sym.LT, null);}
					case -19:
						break;
					case 19:
						{return tok(sym.GT, null);}
					case -20:
						break;
					case 20:
						{return tok(sym.AND, null);}
					case -21:
						break;
					case 21:
						{return tok(sym.OR, null);}
					case -22:
						break;
					case 22:
						{return tok(sym.INT, Integer.parseInt(yytext()));}
					case -23:
						break;
					case 23:
						{buffer = ""; yybegin(STRING); }
					case -24:
						break;
					case 24:
						{newline(); /* notice that the new line for linux */}
					case -25:
						break;
					case 25:
						{}
					case -26:
						break;
					case 26:
						{errorMsg.error(yychar, "Lex: Illegal character '" + yytext() + "'");}
					case -27:
						break;
					case 27:
						{}
					case -28:
						break;
					case 28:
						{return tok(sym.IF, null);}
					case -29:
						break;
					case 29:
						{return tok(sym.IN, null);}
					case -30:
						break;
					case 30:
						{return tok(sym.OF, null);}
					case -31:
						break;
					case 31:
						{return tok(sym.TO, null);}
					case -32:
						break;
					case 32:
						{return tok(sym.DO, null);}
					case -33:
						break;
					case 33:
						{return tok(sym.ASSIGN, null);}
					case -34:
						break;
					case 34:
						{ yybegin(COMMENT); commentDepth++; }
					case -35:
						break;
					case 35:
						{return tok(sym.LE, null);}
					case -36:
						break;
					case 36:
						{return tok(sym.NEQ, null);}
					case -37:
						break;
					case 37:
						{return tok(sym.GE, null);}
					case -38:
						break;
					case 38:
						{newline(); /* notice that the new line for windows */}
					case -39:
						break;
					case 39:
						{return tok(sym.LET, null);}
					case -40:
						break;
					case 40:
						{return tok(sym.END, null);}
					case -41:
						break;
					case 41:
						{return tok(sym.FOR, null);}
					case -42:
						break;
					case 42:
						{return tok(sym.NIL, null);}
					case -43:
						break;
					case 43:
						{return tok(sym.VAR, null);}
					case -44:
						break;
					case 44:
						{return tok(sym.ELSE, null);}
					case -45:
						break;
					case 45:
						{return tok(sym.THEN, null);}
					case -46:
						break;
					case 46:
						{return tok(sym.TYPE, null);}
					case -47:
						break;
					case 47:
						{return tok(sym.WHILE, null);}
					case -48:
						break;
					case 48:
						{return tok(sym.BREAK, null);}
					case -49:
						break;
					case 49:
						{return tok(sym.ARRAY, null);}
					case -50:
						break;
					case 50:
						{return tok(sym.FUNCTION, null);}
					case -51:
						break;
					case 51:
						{return tok(sym.PRIMITIVE, null);}
					case -52:
						break;
					case 52:
						{}
					case -53:
						break;
					case 53:
						{ /* notice the new line for linux */}
					case -54:
						break;
					case 54:
						{ commentDepth--; if(commentDepth == 0){ yybegin(YYINITIAL); };}
					case -55:
						break;
					case 55:
						{ commentDepth++; /* nested comment depth + 1 */ }
					case -56:
						break;
					case 56:
						{ /* notice the new line for linux */}
					case -57:
						break;
					case 57:
						{ buffer+= yytext(); }
					case -58:
						break;
					case 58:
						{yybegin(YYINITIAL);String ret = buffer; buffer = ""; return tok(sym.STRING, ret);}
					case -59:
						break;
					case 59:
						{ buffer+= "\n"; /* handle carriage return in string */ }
					case -60:
						break;
					case 60:
						{ buffer+= "\t"; /* handle tab in string */ }
					case -61:
						break;
					case 61:
						{ buffer+= "\\\""; /* handle escaped double quote in string */ }
					case -62:
						break;
					case 63:
						{return tok(sym.ID, yytext());}
					case -63:
						break;
					case 64:
						{}
					case -64:
						break;
					case 65:
						{ buffer+= yytext(); }
					case -65:
						break;
					case 67:
						{return tok(sym.ID, yytext());}
					case -66:
						break;
					case 68:
						{}
					case -67:
						break;
					case 70:
						{return tok(sym.ID, yytext());}
					case -68:
						break;
					case 72:
						{return tok(sym.ID, yytext());}
					case -69:
						break;
					case 73:
						{return tok(sym.ID, yytext());}
					case -70:
						break;
					case 74:
						{return tok(sym.ID, yytext());}
					case -71:
						break;
					case 75:
						{return tok(sym.ID, yytext());}
					case -72:
						break;
					case 76:
						{return tok(sym.ID, yytext());}
					case -73:
						break;
					case 77:
						{return tok(sym.ID, yytext());}
					case -74:
						break;
					case 78:
						{return tok(sym.ID, yytext());}
					case -75:
						break;
					case 79:
						{return tok(sym.ID, yytext());}
					case -76:
						break;
					case 80:
						{return tok(sym.ID, yytext());}
					case -77:
						break;
					case 81:
						{return tok(sym.ID, yytext());}
					case -78:
						break;
					case 82:
						{return tok(sym.ID, yytext());}
					case -79:
						break;
					case 83:
						{return tok(sym.ID, yytext());}
					case -80:
						break;
					case 84:
						{return tok(sym.ID, yytext());}
					case -81:
						break;
					case 85:
						{return tok(sym.ID, yytext());}
					case -82:
						break;
					case 86:
						{return tok(sym.ID, yytext());}
					case -83:
						break;
					case 87:
						{return tok(sym.ID, yytext());}
					case -84:
						break;
					case 88:
						{return tok(sym.ID, yytext());}
					case -85:
						break;
					case 89:
						{return tok(sym.ID, yytext());}
					case -86:
						break;
					case 90:
						{return tok(sym.ID, yytext());}
					case -87:
						break;
					case 91:
						{return tok(sym.ID, yytext());}
					case -88:
						break;
					case 92:
						{return tok(sym.ID, yytext());}
					case -89:
						break;
					case 93:
						{return tok(sym.ID, yytext());}
					case -90:
						break;
					case 94:
						{return tok(sym.ID, yytext());}
					case -91:
						break;
					case 95:
						{return tok(sym.ID, yytext());}
					case -92:
						break;
					case 96:
						{return tok(sym.ID, yytext());}
					case -93:
						break;
					case 97:
						{return tok(sym.ID, yytext());}
					case -94:
						break;
					case 98:
						{return tok(sym.ID, yytext());}
					case -95:
						break;
					case 99:
						{return tok(sym.ID, yytext());}
					case -96:
						break;
					case 100:
						{return tok(sym.ID, yytext());}
					case -97:
						break;
					case 101:
						{return tok(sym.ID, yytext());}
					case -98:
						break;
					case 102:
						{return tok(sym.ID, yytext());}
					case -99:
						break;
					case 103:
						{return tok(sym.ID, yytext());}
					case -100:
						break;
					case 104:
						{return tok(sym.ID, yytext());}
					case -101:
						break;
					case 105:
						{return tok(sym.ID, yytext());}
					case -102:
						break;
					case 106:
						{return tok(sym.ID, yytext());}
					case -103:
						break;
					case 107:
						{return tok(sym.ID, yytext());}
					case -104:
						break;
					case 108:
						{return tok(sym.ID, yytext());}
					case -105:
						break;
					case 109:
						{return tok(sym.ID, yytext());}
					case -106:
						break;
					case 110:
						{return tok(sym.ID, yytext());}
					case -107:
						break;
					case 111:
						{return tok(sym.ID, yytext());}
					case -108:
						break;
					case 112:
						{return tok(sym.ID, yytext());}
					case -109:
						break;
					case 113:
						{return tok(sym.ID, yytext());}
					case -110:
						break;
					case 114:
						{return tok(sym.ID, yytext());}
					case -111:
						break;
					case 115:
						{return tok(sym.ID, yytext());}
					case -112:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
