grammar Day9;

group
:
	GROUP_START groupContent GROUP_END
;

groupContent
:
	groupElement?
	(
		GROUP_SEPARATOR groupElement
	)*
;

groupElement
:
	group
	| garbage
;

garbage
:
	GARBAGE_START garbageContent GARBAGE_END
;

garbageContent
:
	(
		~GARBAGE_END
	)*
;

CANCEL
:
	'!' . -> skip
;

GROUP_START
:
	'{'
;

GROUP_END
:
	'}'
;

GARBAGE_START
:
	'<'
;

GARBAGE_END
:
	'>'
;

GROUP_SEPARATOR
:
	','
;

LETTER
:
	.
;

WS
:
	[ \t\r\n]+ -> skip
;