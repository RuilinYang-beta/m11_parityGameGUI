grammar PG;

root : header line*;
header : 'parity' count;
count : INT;
line : id priority owner edge (',' edge)* label;
id : INT;
priority : INT;
owner : INT;
edge : INT;
label : STR?;

STR  : '"' (~["\\] | '\\' .)* '"';
INT  : [0-9]+ ;
WS   : [ \t\r\n;]+ -> skip ;