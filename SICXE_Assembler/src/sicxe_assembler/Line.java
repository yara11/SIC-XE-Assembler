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
import java.util.Formatter;

public class Line {
    private int line_no, size = 0;
    private Boolean isError, isComment, isDirective;
    private String address, label, mnemonic, comment, error_message;
    private ArrayList<String> operands = new ArrayList();
    
    // TODO: Any extras should be done for directives?
    // **** UNHANDELED: format 4, X, literals, etc...
    public Line(String code_line, LocationCounter LOCCTR, SymbolTable symbolTable, int last_line_no) {
        // TODO: validate an empty line
        if(code_line.charAt(0) == '.') {
            this.isComment = true;
            this.line_no = last_line_no + 1;
            this.address = Assembler.decToHex(LOCCTR.getLocation(), 6);
            this.comment = code_line;
            this.size = 0;
        }
        else {
            // Split the line into diff parts of the instruction
            this.label = hamada(code_line, 0, 7);
            this.mnemonic = hamada(code_line, 9, 14);
            String operands_str = hamada(code_line, 17, 34);
            if(operands_str != null)
                this.operands = new ArrayList(Arrays.asList(operands_str.split(",")));
            this.comment = hamada(code_line, 35, 65);
            // Instruction validation:
            // 1. Check for repeated label
            if(this.label.length() > 0 && symbolTable.isLabel(this.label)) {
                this.isError = true;
                this.error_message = String.format("***Error: Symbol %s is already defined.", this.label);
            }
            // 2. Check for wrong mnemonic
            if(InstructionSet.getInstruction(this.mnemonic) == null) {
                this.isError = true;
                this.error_message = String.format("***Error: mnemonic %s is undefined", this.mnemonic);
            }
            // 3. Check for wrong instruction syntax or undefined label
            if(!this.isError) {
                InstrDetails instrDetails = InstructionSet.getInstruction(this.mnemonic);
                int regs = 0, labels = 0, imms =0;
                for(String operand: this.operands) {
                    // does not handle literals here
                    if(operand.toUpperCase().equals("X"))   // could X register be used explicitly? 
                                                            // as a forced error, perhaps?
                        continue;
                    if(RegisterSet.isRegister(operand)) {
                        regs++;
                    }
                    else if(isNumber(operand)) {
                        imms++;
                    }
                    else if(symbolTable.isLabel(operand)){
                        labels++;
                    }
                    else {
                        this.isError = true;
                        this.error_message = String.format("***Error: label %s is undefined", operand);
                        break;
                    }
                }
                if(!this.isError && (regs != instrDetails.getRegOps() || labels+imms != instrDetails.getDirOps()+instrDetails.getMemOps())) {
                    this.isError = true;
                    this.error_message = "***Error: invalid syntax";
                }
                else {
                    this.line_no = last_line_no + 1;
                    this.size = InstructionSet.getInstruction(this.mnemonic).getFormat();
                }
            }
        }
    }
    
    // Takes the substring from code line that is expected to contain a certain 
    // part of the instruction, removes all whitespaces and returns it.
    // TODO : rename :D
    private String hamada(String str, int stt, int end) {
        if(stt >= str.length()){
            return "";
        }
        String ret = str.substring(stt, Math.min(end, str.length()-1));
        ret = ret.replaceAll("\\s",""); // remove all whitespaces
        return ret;
    }
    
    private Boolean isNumber(String str) {
        if(str == null)
            return false;
        return Character.isDigit(str.charAt(0));
    }
    
    /*public Instruction getInstruction() {
        // TODO
    }*/
    
    public String getLine() {
        if(isComment) {
            return String.format("%3d   %6s   %66s", line_no, address, comment);
        }
        if(isError) {
            return String.format("%9s%s", "",error_message);
        }
        String operands_string = "";
        for(int i = 0; i < operands.size(); i++) {
            if(i != 0) operands_string += ',';
            operands_string += operands.get(i);
        }
        return String.format("%3d   %6s   %8s   %6s   %18s   %31s", line_no, address, label, mnemonic, operands_string, comment);
    }
    
    public String getLabel() {
        if(this.label.length() == 0)
            return null;
        return this.label;
    }
    
    // todo: move to Instruction.java
    public int getSize() {
        return this.size;
    }
    
    public Boolean isValid() {
        return !this.isError;
    }
    
    
}
