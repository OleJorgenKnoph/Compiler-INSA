grammar CompilerGrammar;

/*
    Parser Rules
*/

prog: MAIN_FUNCTION O_PAREN MAIN_PARAMETER C_PAREN O_BRACK body C_BRACK;

//In main. Declaration before instruction as instructed
// but also possible for declaration again after instruction
body: declaration+ (instructions | declaration)*;


declaration:    INT VAR_NAME '=' NUM SEMI_COL //Assign 1 variabel
            |   INT VAR_NAME '=' expression+ SEMI_COL //For math in new declaration
            |   INT? VAR_NAME',' (declaration | finalDeclaration) //Possible to assign multiple
            ;
            finalDeclaration: VAR_NAME '='? NUM? SEMI_COL; //To make sure instructions isnt allowed before declaration

instructions:   VAR_NAME '=' expression SEMI_COL #EXPRESSIONS //Instructions of math
            |   statements SEMI_COL #PRINT //Creating statements
            |   IF_WHILE O_PAREN conditions C_PAREN O_BRACK func_body C_BRACK else? #IF_WHILE //IF and WHILE
            ;

            func_body: (instructions | declaration)+;
            else: ELSE O_BRACK func_body C_BRACK;


    statements: PRINT O_PAREN expression C_PAREN; //Printing 1 variabel, either a number, variabel or expression

    //All mathematical operations instructed to recognize
    expression:     left=expression MUL right=expression #MUL
              |     left=expression DIV right=expression #DIV
              |     left=expression ADD right=expression #ADD
              |     left=expression SUB right=expression #SUB
              |     NUM #integer
              |     VAR_NAME #variableName
              |     O_PAREN expression C_PAREN #parenthesis
              ;

    conditions: (NUM | VAR_NAME) BOOLEAN_OPERATOR (NUM | VAR_NAME);

/*
    Lexer Rules
*/
MAIN_FUNCTION: 'public static void main'; //Declaring the main function in java
MAIN_PARAMETER: 'String[] args'; //Parameter belonging to main
O_PAREN: '('; //Open parenthesis
C_PAREN: ')'; //Close parenthesis
O_BRACK: '{'; //Open brackets
C_BRACK: '}'; //Close brackets
SEMI_COL: ';'; //Semi-colon
PRINT: 'System.out.println';
INT: 'int'; //datatype integer
IF_WHILE: 'if' | 'while'; //If or While function
ELSE: 'else';

//MATHEMATICAL SIGNS:
ADD:'+';
SUB:'-';
MUL:'*';
DIV:'/';
    //Parenthesis already defined
BOOLEAN_OPERATOR: '!=' | '==' | '===' | '<' | '>' | '<=' | '>=' | '=<' | '=>';

VAR_NAME: ["']?[a-zA-Z]+[a-zA-Z0-9_]*['"]?; //Variable name. Must start with letter but can contain numbers and underscore
NUM: [0-9]+([.,^][0-9]+)*; //Numbers. Recognize decimals and exponents

WS: [ \r\n\t] + -> skip; //Skipping whitespaces