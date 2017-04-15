# SIC-XE-Assembler
SIC/XE Two Pass Assembler in Java

## TODO:
PASS1:
- [x] Fill symbol table
- [x] Assign locations with location counter
- [x] Validate instructions
- [ ] Print and save source code including address and line number
- [ ] Save instruction list for pass2

PASS2:
- [ ] Create object code of every instruction
- [ ] Create object code for BYTE and WORD directives
- [ ] Print and save HTME record

MISSING STUFF
- [ ] Handle directives in validation
- [ ] Handle empty line in validation
- [ ] Handle different addressing modes: @, #, X
- [ ] Handle format 4: +
- [ ] Change location counter when START is found
- [ ] The instruction syntax validation is unclear
- [ ] Calculate the size in case of BYTE or WORD
