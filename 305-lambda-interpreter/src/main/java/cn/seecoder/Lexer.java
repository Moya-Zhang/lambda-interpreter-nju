package cn.seecoder;


public class Lexer{

    public String source;
    public int index;
    public TokenType token;
    public String tokenvalue;

    public static void main(String[] args) {
        String TRUE = "(\\x.\\y.x)";
        String FALSE = "(\\x.\\y.y)";
        String ISZERO = "(\\n.n(\\x."+FALSE+")"+TRUE+")";
        Lexer l=new Lexer(ISZERO);
        TokenType t=l.nextToken();
        while(t!=TokenType.EOF)
            t=l.nextToken();
    }
    public Lexer(String s){
        index = 0;
        source = s;
        nextToken();
    }
    //获得下一个token，打印，然后返回
    private TokenType nextToken(){
        //write your code here
        char c=nextChar();
        while(c==' ')
            c=nextChar();
        switch (c){
            case '\0':
                token=TokenType.EOF;break;
            case '(':
                token=TokenType.LPAREN;
                tokenvalue="(";break;
            case ')':
                token=TokenType.RPAREN;
                tokenvalue=")";break;
            case '.':
                token=TokenType.DOT;
                tokenvalue=".";break;
            case '\\':
                token=TokenType.LAMBDA;
                tokenvalue="\\";break;
            default:
                token=TokenType.LCID;
                tokenvalue=String.valueOf(c);
        }
        System.out.println(token);
        return token;
    }

    // 获得下一个字符
    private char nextChar(){
        return index<source.length()?source.charAt(index++):'\0';
    }


    //check token == t
    //+ next(Token): 返回下一个 token 是否匹配 Token
    public boolean next(TokenType t){
        //write your code here
        return t==this.token;
    }

    //skip token  and move next token
    //+ skip(Token): 和 next 一样, 但如果匹配的话会跳过
    public boolean skip(TokenType t){
        //write your code here
        if(t==this.token){
            token=nextToken();
            return true;
        }
        return false;
    }

    //assert matching the token type, and move next token
    //+ match(Token): 断言 next 方法返回 true 并 skip
    public void match(TokenType t){
        assert next(t)==true;
        skip(t);
    }

    //+ token(Token): 断言 next 方法返回 true 并返回 token
    public String token(TokenType t){
        String val=tokenvalue;
        match(t);
        return val;
    }

}
