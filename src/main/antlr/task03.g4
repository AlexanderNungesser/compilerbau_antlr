grammar task03;

start : stmt* EOF ;

stmt : assign
    | while
    | if
    ;

assign  : ID ':=' expr ;

while: 'while' cond 'do' stmt* 'end' ;

if: 'if' cond 'do' stmt* 'end'
    | 'if' cond 'do' stmt* 'else do' stmt* 'end' ;

cond : expr '==' expr
     |  expr '!=' expr
     |  expr '>' expr
     |  expr '<' expr
     ;

expr : ID | exprNUM | exprSTR;

exprNUM  : exprNUM '*' exprNUM
      | exprNUM '/' exprNUM
      | exprNUM '+' exprNUM
      | exprNUM '-' exprNUM
      | NUM
      ;

exprSTR  : exprSTR '+' exprSTR
      | STR
      ;


// Lexer
ID    : [a-zA-Z_][a-zA-Z_0-9]* ;
NUM   : [0-9]+ ;
STR  :  '"' (~[\n\r"])* '"' ;
COMMENT :  '#' ~[\n\r]* -> skip ;
WS    : [ \t\n\r]+ -> skip ;