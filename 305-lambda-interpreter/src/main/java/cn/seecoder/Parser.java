package cn.seecoder;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;

    public static void main(String[] args) {
        String TRUE = "(\\x.\\y.x)";
        String FALSE = "(\\x.\\y.y)";
        String ISZERO = "(\\n.n(\\x."+FALSE+")"+TRUE+")";
        String s="(\\n.\\f.\\x.(f((n f)x)))(\\f.\\x.x)";
        String x="\\x.x";
        Lexer l=new Lexer(x);
        Parser p=new Parser(l);
        p.parse();
    }
    public Parser(Lexer l){
        lexer = l;
    }

    //构造并返回语法树
    public AST parse(){

        AST ast = term(new ArrayList<>());
//        System.out.println(lexer.match(TokenType.EOF));
        return ast;
    }
    
    //Term ::= Application| LAMBDA LCID DOT Term
    private AST term(ArrayList<String> ctx){
        String param,paramValue;
        //后者：LAMBDA LCID DOT Term，也就是一个抽象
        if(lexer.skip(TokenType.LAMBDA)){
            if(lexer.next(TokenType.LCID)){
                param=lexer.tokenvalue;
                lexer.match(TokenType.LCID);
                if(lexer.skip(TokenType.DOT)){
                    ctx.add(0,param);
                    paramValue=String.valueOf(ctx.indexOf(param));
                    AST aTerm=term(ctx);
                    ctx.remove(ctx.indexOf(param));
                    return new Abstraction(new Identifier(param,paramValue),aTerm);
                }
            }
        }
        //前者：Application
        else
            return application(ctx);
        return null;
    }

    //Application ::= Application Atom| Atom
    //换言之，Application=单个atom，或者若干个atom组成的application加上最后一个atom
    private AST application(ArrayList<String> ctx){
        AST left=atom(ctx);//第一个atom，后面可能还会加若干个atom
        AST right=atom(ctx);//最后一个atom，或者也可能为null
        while(right!=null){
            left=new Application(left,right);
            right=atom(ctx);
        }
        return left;
    }

    //Atom ::= LPAREN Term RPAREN| LCID
    private AST atom(ArrayList<String> ctx){
        // 前者：LPAREN Term RPAREN
        if(lexer.skip(TokenType.LPAREN)){
            AST t=term(ctx);
            lexer.match(TokenType.RPAREN);
            return t;
        }
        //后者：LCID
        else if(lexer.next(TokenType.LCID)){
            String name=lexer.token(TokenType.LCID);
            int idx=ctx.indexOf(name);
            String value=String.valueOf(idx==-1?ctx.size():idx);
            return(new Identifier(name,value));
        }
//        System.out.println("wrong in atom of Parser!");
        return null;
    }
}
