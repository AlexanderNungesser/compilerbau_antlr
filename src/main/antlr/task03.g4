grammar task03;

start : stmt* ;

stmt : assign
    | while
    | if
    ;

assign  : ID ':=' expr ;

while: 'while' cond 'do' stmt* 'end' ;

if: 'if' cond 'do' stmt* 'end' | 'if' cond 'do' stmt* 'else do' 'end' ;

cond : expr '==' expr
     |  expr '!=' expr
     |  expr '>' expr
     |  expr '<' expr
     ;

expr : exprNUM | exprSTR | ID;

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
STR   :  '"' WRD* '"' ;
WRD   : (~[\n\r"]) ;
COMMENt : '#' WRD* -> skip ;
WS    : [ \t\n\r]+ -> skip ;