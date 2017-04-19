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
- [ ] Print and save HTME record

MISSING STUFF:
- [x] Handle directives
- [x] Handle empty line
- [x] Handle different addressing modes: @, #, X
- [x] Handle format 4: +
- [x] Change location counter when START is found
- [x] The instruction syntax validation is unclear
- [x] Calculate the size in case of BYTE or WORD
- [ ] Changing b & p flags in pass1
- [ ] Displacement exceeds the limits in pc-relative and base-relative addressing mode
- [ ] Test object code generators

ERRORS:
- [x] Printing should be left-justified for each column except line no.
//
- [x] We should go over the labels before anything,
	  they maybe declared at the end but used in preceding instructions.
- [ ] What if the label's line had error after I used it?!!
