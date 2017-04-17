# SIC-XE-Assembler
SIC/XE Two Pass Assembler in Java

## TODO:
PASS1:
- [x] Fill symbol table
- [x] Assign locations with location counter
- [x] Validate instructions
- [x] Write source code file including address and line number
- [x] Save instruction list for pass2
- [ ] Process directives (START, END, RESW, RESB, WORD, BYTE)
- [x] Write symbol table file

PASS2:
- [ ] Create object code of every instruction
- [ ] Create object code for BYTE and WORD directives
- [ ] Print and save HTME record

MISSING STUFF:
- [ ] Handle directives
- [x] Handle empty line
- [x] Handle different addressing modes: @, #, X
- [x] Handle format 4: +
- [ ] Change location counter when START is found
- [x] The instruction syntax validation is unclear
- [ ] Calculate the size in case of BYTE or WORD
- [ ] Changing b & p flags in pass1
- [ ] Displacement exceeds the limits in pc-relative and base-relative addressing mode

ERRORS:
- [x] We should go over the labels before anything,
	  they maybe declared at the end but used in preceding instructions.
- [x] Printing should be left-justified for each column except line no.