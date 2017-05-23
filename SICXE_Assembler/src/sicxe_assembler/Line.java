// This class describes a line of assembly code
//The source program to be assembled must be in fixed format as follows:
//1. bytes 1–8 label
//2. 9 blank
//3. 10–15 operation code
//4. 16–17 blank
//5. 18–35 operand
//6. 36–66 comment
package sicxe_assembler;

import java.util.ArrayList;
import java.util.Arrays;

//dividing the line into label mnemonic operands and comment
// setting i and n flags
//dividing the list of operands
//first checking for directives if not validating instruction
//validating label and operands
public class Line {
    boolean isLit = false;
    public boolean literalError = false;
    public Instruction getInstr() {
        return instr;
    }
    // Line stuff
    private int line_no;
    public String text;
    public int getLine_no() {
        return line_no;
    }

    public void setLine_no(int line_no) {
        this.line_no = line_no;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCode_line() {
        return code_line;
    }

    private String address, label, comment;

    public String getAddress() {
        return address;
    }
    private Instruction instr;
    public Directive dir;

    public Directive getDir() {
        return dir;
    }

    // Other stuff
    private int size = 0;
    private Boolean isComment = false, isError = false;
    private String error_message = null;
    // just keep it for printing
    private String code_line;
    // **** UNHANDELED: BASE, literals
    public Line(String code_line, LocationCounter LOCCTR, SymbolTable symbolTable, int last_line_no) {
        this.code_line = code_line;
        this.line_no = last_line_no + 1;
        this.address = Assembler.decToHex(LOCCTR.getLocation(), 6).toUpperCase();
        if (code_line.charAt(0) == '.') {
            this.isComment = true;
            this.comment = code_line;
            this.size = 0;
        } else {
            /**
             * * Get label       **
             */
            this.label = hamada(code_line, 0, 7);
            if (this.label.length() == 0) {
                this.label = null;
            }
            /**
             * * Get instruction       **
             */
            // Things that may be given in an instruction
            String mnemonic = null;
            ArrayList<String> operands = new ArrayList<>();
            Boolean isFormat4 = false;
            Boolean isImm = false, isIndir = false;

            // Get the mnemonic
            mnemonic = hamada(code_line, 9, 14);
            // check format 4
            if (mnemonic.length() > 0 && mnemonic.charAt(0) == '+') {
                mnemonic = mnemonic.substring(1);
                isFormat4 = true;
            }

            // Get the operands list
            String operands_str = hamada(code_line, 17, 34);
            if (operands_str.length() > 0) {
                if (operands_str.charAt(0) == '#') {
                    Assembler.setB(false);
                    Assembler.setP(false);
                    isImm = true;
                    operands_str = operands_str.substring(1);
                } else if (operands_str.charAt(0) == '@') {
                    isIndir = true;
                    operands_str = operands_str.substring(1);
                } else if(operands_str.charAt(0) == '='){
                    if((operands_str.charAt(1) == 'X' || operands_str.charAt(1) == 'C') && operands_str.charAt(2) =='\'' && operands_str.charAt(operands_str.length()-1) == '\'')
                    {
                        Lit l = new Lit();
                        isLit = true;
                        int length = operands_str.length()-4;
                        String value = operands_str.substring(3, operands_str.length()-1);
                        if(operands_str.charAt(1) == 'X'){
                            l.setIsHex(true);
                            l.setLength(length/2);
                            l.setValue(value);
                        }
                        if(operands_str.charAt(1) == 'C'){
                            l.setIsHex(false);
                            l.setLength(length);
                            l.setValue(value);
                        }
                        operands_str = operands_str.substring(3, operands_str.length()-1);
                        
                        Assembler.literals.add(l);
                       
                    }
                    else
                    {
                        System.out.println("literal format error");
                        literalError = true;
                    }
                }
                
            }
            if (operands_str != null && operands_str.length() > 0) {
                if(mnemonic.equals("EQU")) {
                    // TODO: Split string over '+', '-', '*', '/'
                    operands = new ArrayList(Arrays.asList(operands_str.split("[*]")));
                }
                else
                    operands = new ArrayList(Arrays.asList(operands_str.split(",")));
            }

            /**
             * * Get comment       **
             */
            this.comment = hamada(code_line, 35, 65);

            // System.out.println(label + " " + mnemonic + " " + operands + " " + comment);
            /**
             * * Validate line       **
             */
            // Validate label
            if (!validateNewLabel(this.label, symbolTable)) {
                this.isError = true;
                this.error_message = String.format("***Error: Symbol %s is already defined.", this.label);
            }

            /**
             * * Check for directive      **
             */
            if (Directive.isDirective(mnemonic, operands_str)) {
                this.dir = new Directive(mnemonic, operands_str);
                this.size = this.dir.getSize();
                // System.out.println("ok");
                return;
            } // Validate mnemonic
            else if (InstructionSet.getInstruction(mnemonic) == null) {
                this.isError = true;
                this.error_message = String.format("***Error: mnemonic %s is undefined", mnemonic);
            } // Validate syntax and label operands
            else if (!validateInstruction(mnemonic, operands, isFormat4, isImm, isIndir, symbolTable)) {
                if (this.error_message == null) {
                    this.error_message = "***Error: Invalid syntax";
                }
                this.isError = true;
            } /**
             * * Create instruction   **
             */
            else {
                int instrFormat = InstructionSet.getInstruction(mnemonic).getFormat();
                if (instrFormat == 1) {
                    this.instr = new Instruction(mnemonic);
                } else if (instrFormat == 2) {
                    this.instr = new Instruction(mnemonic, operands, symbolTable);
                } else {
                    this.instr = new Instruction(mnemonic, operands, isFormat4, isImm, isIndir, symbolTable);
                }
                this.size = this.instr.getFormat();
            }
        }
    }

    private Boolean validateInstruction(String mnemonic, ArrayList<String> operands, Boolean isFormat4, Boolean isImm, Boolean isIndir, SymbolTable symbolTable) {
        InstrDetails instrDetails = InstructionSet.getInstruction(mnemonic);
        if (instrDetails.getFormat() < 3 && (isFormat4 || isImm || isIndir)) {
            System.out.println("* " + mnemonic);
            return false;
        }
        this.size = instrDetails.getFormat();
        int rNum = 0, lNum = 0, vNum = 0;
        for (int i = 0; i < operands.size(); i++) {
            /*
            if(!Operand.isValid(operands.get(i), symbolTable)) {
                this.error_message = String.format("***Error: Undefined operand %s", operands.get(i));
                return false;
            } 
             */
            if (instrDetails.getFormat() != 2 && i == operands.size() - 1 && operands.get(i).toUpperCase().equals("X")) {
                continue;
            }
            char opType = (new Operand(operands.get(i), symbolTable)).getType();
            switch (opType) {
                case 'r': // register
                    rNum++;
                    break;
                case 'l': // label
                    lNum++;
                    break;
                default:  // value (hex or dec)
                    vNum++;
                    break;
            }
        }
        if (rNum != instrDetails.getRegOps() || vNum < instrDetails.getDirOps()) {
            return false;
        }
        int rem = lNum + vNum - instrDetails.getDirOps();
        if (rem != instrDetails.getMemOps()) {
            return false;
        }
        return true;
    }

    private Boolean validateNewLabel(String lbl, SymbolTable symbolTable) {
        if (lbl == null) {
            return true;
        }
        return !(Character.isDigit(lbl.charAt(0)) || symbolTable.isLabel(lbl));
    }

    public static String hamada(String str, int stt, int end) {
        if (stt >= str.length()) {
            return "";
        }
        String ret = str.substring(stt, Math.min(end + 1, str.length()));
        ret = ret.trim(); // remove all whitespaces
        return ret;
    }

    public Boolean isValid() {
        return !this.isError;
    }

    public String getLabel() {
        return this.label;
    }

    public int getSize() {
        return this.size;
    }

    public Boolean validateOperands(SymbolTable symbolTable) {
        if(this.dir != null || this.isComment)
            return true;
        ArrayList<Operand> operands = this.instr.getOperands();
        for (int i = 0; i < operands.size(); i++) {
            if (!Operand.isValid(operands.get(i).getName(), symbolTable) && isLit == false) {
                
                this.isError = true;
                this.error_message = String.format("***Error: Undefined operand %s", operands.get(i).getName());
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (isComment) {
            return String.format("%3d   %6s   %-66s", line_no, address, comment);
        }
        String ret = String.format("%3d   %6s   %s", line_no, address, code_line);
        if (isError) {
            ret += '\n' + String.format("%9s%s", "", error_message);
        }
        return ret;
    }

    public String baseString() {
        String ret = String.format("%3d            %s", line_no, code_line);
        return ret;
    }

    public void unError() {
        this.isError = false;
    }

    public void setAddress(String addr) {
        this.address = addr.toUpperCase();
    }

    public String getObjectCode(SymbolTable symbolTable) {
        if (this.isError || this.isComment) {
            return " ";
        }
        if (this.dir != null) {  // if isDirective()
            // System.out.println("testline");
            return dir.getObjectCode(symbolTable);
        }
        if (this.instr.getI() == true) {
            //Assembler.setB(false);
            //Assembler.setP(false);
        }
        //System.out.println("testline");
        String str = instr.getObjectCode(symbolTable);
        return str;
    }

    public Boolean getIsComment() {
        return isComment;
    }

    public Boolean getIsError() {
        return isError;
    }

    public Boolean getIsDirective() {
        return this.dir != null;
    }

}
