class Parser {

    Lexer lexer;
    Token token;
    
    public Parser(String s) {
        lexer = new Lexer(s + "$");
        token = lexer.nextToken();
    }

    public void run () {
        query();
    }

    public void query () {
        System.out.println("<Query>");
        matchKeyword("SELECT");
        IDList();
        matchKeyword("FROM");
        IDList();
        if (token.getTokenType() == Token.TokenType.KEYWORD &&
            token.getTokenValue().equals("WHERE")) {
            matchKeyword("WHERE");
            condList();
        }
        match(Token.TokenType.EOI);
        System.out.println("</Query>");
    }

    public void IDList() {
        System.out.println("\t<IDList>");
        String v = match(Token.TokenType.ID);
        output(2, Token.TokenType.ID, v);
        while (token.getTokenType() == Token.TokenType.COMMA) {
            output(2, Token.TokenType.COMMA, ",");
            token = lexer.nextToken();
            v = match(Token.TokenType.ID);
            output(2, Token.TokenType.ID, v);
        }
        System.out.println("\t</IDList>");
    }

    public void condList() {
        System.out.println("\t<CondList>");
        condition();
        while (token.getTokenType() == Token.TokenType.KEYWORD &&
               token.getTokenValue().equals("AND")) {
            output(2, Token.TokenType.KEYWORD, "AND");
            token=lexer.nextToken();
            condition();
        }
        System.out.println("\t</CondList>");
    }

    public void condition() {
        System.out.println("\t\t<Cond>");
        String v = match(Token.TokenType.ID);
        output(3, Token.TokenType.ID, v);
        v = match(Token.TokenType.OPERATOR);
        output(3, Token.TokenType.OPERATOR, v);
        term();
        System.out.println("\t\t</Cond>");
    }

    public void term() {
        System.out.println("\t\t\t<Term>");
        if (token.getTokenType() == Token.TokenType.ID)
            output(4, Token.TokenType.ID, token.getTokenValue());
        else if (token.getTokenType() == Token.TokenType.INT)
            output(4, Token.TokenType.INT, token.getTokenValue());
        else if (token.getTokenType() == Token.TokenType.FLOAT)
            output(4, Token.TokenType.FLOAT, token.getTokenValue());
        else {
            System.err.println("Syntax error: expecting: an ID, an int, or a float"
                               + "; saw:"
                               + Token.typeToString(token.getTokenType()));
            System.exit(1);
        }
        token = lexer.nextToken();
        System.out.println("\t\t\t</Term>");
    }

    private void matchKeyword(String kw) {
        if (token.getTokenType() == Token.TokenType.KEYWORD &&
            token.getTokenValue().equals(kw)) {
            output(1, Token.TokenType.KEYWORD, kw);
            token = lexer.nextToken();
        } else error (Token.TokenType.KEYWORD);
    }

    private String match (Token.TokenType tp) {
        String value = token.getTokenValue();
        if (token.getTokenType() == tp)
            token = lexer.nextToken();
        else error(tp);
        return value;
    }

    private void output (int ident, Token.TokenType tp, String val) {
        String s = "";
        for (int i=0; i<ident; i++) s +="\t";
        String tag = Token.typeToString(tp);
        s += "<" + tag + ">" + val + "</" + tag + ">";
        System.out.println(s);
    }

    private void error (Token.TokenType tp) {
        System.err.println("Syntax error: expecting: " + Token.typeToString(tp)
                              + "; saw: "
                              + Token.typeToString(token.getTokenType()));
        System.exit(1);
    }
}
