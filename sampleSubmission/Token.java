public class Token{
    public enum TokenType {INT, FLOAT, ID, KEYWORD, OPERATOR, COMMA, EOI, INVALID}

    private TokenType type;
    private String val;

    Token (TokenType t, String v) {
        type = t; val = v;
    }

    TokenType getTokenType() {return type;}
    String getTokenValue() {return val;}

    void print () {
        String s = "";
        switch (type) {
        case INT: case FLOAT: case ID: case KEYWORD: case OPERATOR:
            s = val; break;
        case COMMA: s = ","; break;
        case EOI: s = ""; break;
        case INVALID: s = "invalid"; break;
        }
        System.out.print(s);
    }

    public static String typeToString (TokenType tp) {
        String s = "";
        switch (tp) {
        case INT: s = "Int"; break;
        case FLOAT: s = "Float"; break;
        case ID: s = "ID"; break;
        case KEYWORD: s = "Keyword"; break;
        case OPERATOR: s = "Operator"; break;
        case COMMA: s = "Comma"; break;
        case EOI: s = "EOI"; break;
        case INVALID: s="Invalid"; break;
        }
        return s;
    }
    
}
