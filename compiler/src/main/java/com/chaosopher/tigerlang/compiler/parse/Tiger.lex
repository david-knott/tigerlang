package com.chaosopher.tigerlang.compiler.parse;

%% 
%public
%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{
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

private ErrorMsg.ErrorMsg errorMsg;
private int commentDepth = 0;
private String buffer = "";

public Yylex(java.io.InputStream s, ErrorMsg.ErrorMsg e) {
  this(s);
  errorMsg=e;
}

%}

%eof{
  if(this.commentDepth != 0) {
    errorMsg.error(yychar, "unexpected end of file in a comment.");
  }
  if(this.buffer.length() != 0) {
    errorMsg.error(yychar, "unexpected end of file in a string literal.");
  }
%eof}

%eofval{
{
  return tok(sym.EOF, null);
}
%eofval}       

digits=[0-9]+

%state COMMENT
%state STRING

%%
<YYINITIAL>"while"	{return tok(sym.WHILE, null);}
<YYINITIAL>"for"	{return tok(sym.FOR, null);}
<YYINITIAL>"to"	{return tok(sym.TO, null);}
<YYINITIAL>"break"	{return tok(sym.BREAK, null);}
<YYINITIAL>"nil"	{return tok(sym.NIL, null);}
<YYINITIAL>"let"	{return tok(sym.LET, null);}
<YYINITIAL>"type"	{return tok(sym.TYPE, null);}
<YYINITIAL>"array"	{return tok(sym.ARRAY, null);}
<YYINITIAL>"do"	{return tok(sym.DO, null);}
<YYINITIAL>"of"	{return tok(sym.OF, null);}
<YYINITIAL>"in"	{return tok(sym.IN, null);}
<YYINITIAL>"if"	{return tok(sym.IF, null);}
<YYINITIAL>"then"	{return tok(sym.THEN, null);}
<YYINITIAL>"else"	{return tok(sym.ELSE, null);}
<YYINITIAL>"end"	{return tok(sym.END, null);}
<YYINITIAL>"function"	{return tok(sym.FUNCTION, null);}
<YYINITIAL>"primitive"	{return tok(sym.PRIMITIVE, null);}
<YYINITIAL>"var"	{return tok(sym.VAR, null);}
<YYINITIAL>"["	{return tok(sym.LBRACK, null);}
<YYINITIAL>"]"	{return tok(sym.RBRACK, null);}
<YYINITIAL>":="	{return tok(sym.ASSIGN, null);}
<YYINITIAL>":"	{return tok(sym.COLON, null);}
<YYINITIAL>","	{return tok(sym.COMMA, null);}
<YYINITIAL>";"	{return tok(sym.SEMICOLON, null);}
<YYINITIAL>"("	{return tok(sym.LPAREN, null);}
<YYINITIAL>")"	{return tok(sym.RPAREN, null);}
<YYINITIAL>"{"	{return tok(sym.LBRACE, null);}
<YYINITIAL>"}"	{return tok(sym.RBRACE, null);}
<YYINITIAL>"."	{return tok(sym.DOT, null);}
<YYINITIAL>"+"	{return tok(sym.PLUS, null);}
<YYINITIAL>"-"	{return tok(sym.MINUS, null);}
<YYINITIAL>"*"	{return tok(sym.TIMES, null);}
<YYINITIAL>"/"	{return tok(sym.DIVIDE, null);}
<YYINITIAL>"="	{return tok(sym.EQ, null);}
<YYINITIAL>"<>"	{return tok(sym.NEQ, null);}
<YYINITIAL>"<="	{return tok(sym.LE, null);}
<YYINITIAL>"<"	{return tok(sym.LT, null);}
<YYINITIAL>">="	{return tok(sym.GE, null);}
<YYINITIAL>">"	{return tok(sym.GT, null);}
<YYINITIAL>"&"	{return tok(sym.AND, null);}
<YYINITIAL>"|"	{return tok(sym.OR, null);}

<YYINITIAL>[a-zA-Z_]+[a-zA-Z0-9_]*	{return tok(sym.ID, yytext());}
<YYINITIAL>{digits} {return tok(sym.INT, new Integer(yytext()));}

<YYINITIAL>\"	{buffer = ""; yybegin(STRING); }
<STRING>\"	{yybegin(YYINITIAL);String ret = buffer; buffer = ""; return tok(sym.STRING, ret);}
<STRING>\n  { buffer+= "\n"; /* handle carriage return in string */ }
<STRING>\t  { buffer+= "\t"; /* handle tab in string */ }
<STRING>\\\"  { buffer+= "\\\""; /* handle escaped double quote in string */ }
<STRING>. { buffer+= yytext(); }

<YYINITIAL>"/*"	{ yybegin(COMMENT); commentDepth++; } 
<COMMENT>"/*"	{ commentDepth++; /* nested comment depth + 1 */ }
<COMMENT>"*/"   { commentDepth--; if(commentDepth == 0){ yybegin(YYINITIAL); };}
<COMMENT>\n { /* notice the new line for linux */}
<COMMENT>\r\n { /* notice the new line for linux */}
<COMMENT>. {}
<YYINITIAL>" "	{}
<YYINITIAL>\t	{}
<YYINITIAL>\n	{newline(); /* notice that the new line for linux */}
<YYINITIAL>\r\n	{newline(); /* notice that the new line for windows */}
<YYINITIAL>"," {return tok(sym.COMMA, null);}

<YYINITIAL>.	{errorMsg.error(yychar, "Lex: Illegal character '" + yytext() + "'");}