grammar Day8;

instructions
:
	instruction+
;

instruction
:
	register operation IF condition
;

register
:
	ID
	| INC
	| DEC
;

operation
:
	operator NUMBER
;

operator
:
	INC
	| DEC
;

condition
:
	register relationalOperator NUMBER
;

relationalOperator
:
	EQ
	| NEQ
	| GT
	| GTE
	| LT
	| LTE
;

IF
:
	'if'
;

INC
:
	'inc'
;

DEC
:
	'dec'
;

EQ
:
	'=='
;

NEQ
:
	'!='
;

GT
:
	'>'
;

GTE
:
	'>='
;

LT
:
	'<'
;

LTE
:
	'<='
;

ID
:
	(
		'a' .. 'z'
	)+
;

NUMBER
:
	'-'?
	(
		'0' .. '9'
	)+
;

WS
:
	[ \t\r\n]+ -> skip
;