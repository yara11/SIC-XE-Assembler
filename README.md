# SIC-XE-Assembler
SIC/XE Two Pass Assembler in Java

## TODO:
PASS1:
- [x] Fill symbol table
- [x] Assign locations with location counter
- [x] Validate instructions
- [x] Write source code file including address and line number
- [x] Save instruction list for pass2
- [x] Process directives (START, END, RESW, RESB, WORD, BYTE)
- [x] Write symbol table file

PASS2:
- [x] Create object code of every instruction
- [x] Create object code for BYTE and WORD directives
- [x] Print and save HTME record

MISSING STUFF:
- [x] Handle directives
- [x] Handle empty line
- [x] Handle different addressing modes: @, #, X
- [x] Handle format 4: +
- [x] Change location counter when START is found
- [x] The instruction syntax validation is unclear
- [x] Calculate the size in case of BYTE or WORD
- [x] Changing b & p flags in pass1
- [x] Displacement exceeds the limits in pc-relative and base-relative addressing mode
- [x] Test object code generators

---------------------------------------------------------------------------------------------

PART 2:
- [ ] Handle EQU and ORG directives
- [ ] Handle using CSECT directive
- [ ] Handle ' * ' (BONUS)
Handle errors:
- [ ] Forward references are not allowed in symbols
- [ ] Format 4 should be used when the operand is an external symbol in another control section
- [ ] Any attempt to refer to a symbol in another control section must be flagged as an error 
     unless the symbol is identified (via EXTREF) as an external reference.
- [ ] Illegal expressions: e.g. 100 - BUFFER , BUFFEEND+BUFFER (BONUS)
- [ ] Using expressions that are defined in another control section (BONUS)
