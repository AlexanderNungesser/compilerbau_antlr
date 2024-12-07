grammar MiniLispB;


// Parser
program :  expr+ EOF ;


expr    :  literal
        |  symbol
        |  list
        |  def
        |  if
        |  fn
        |  fcall
        |  let
        ;

literal :  NUMBER
        |  STRING
        |  TRUE
        |  FALSE
        ;

symbol  :  ID ;

list    :  '(' 'list' expr* ')' ;
def     :  '(' 'def' symbol expr ')' ;

fn      :  '(' 'defn' symbol '(' symbol* ')' expr* ')' ;
fcall   :  '(' (ID | OP) expr* ')' ;

let     :  '(' 'let' '(' binding* ')' expr ')' ;
binding :  symbol expr ;

if      : '(' 'if' expr block block? ')';
block    : ('(' 'do' expr* ')') | expr ;


// Lexer
TRUE    :  'true' ;
FALSE   :  'false' ;
ID      :  [a-z][a-zA-Z0-9]* ;
NUMBER  :  [0-9]+ ;
OP      :  '+' | '-' | '*' | '/' | '=' | '>' | '<' ;
STRING  :  '"' (~[\n\r"])* '"' ;

COMMENT :  ';;' ~[\n\r]* -> skip ;
WS      :  [ ,\t\n\r]+ -> skip ;
