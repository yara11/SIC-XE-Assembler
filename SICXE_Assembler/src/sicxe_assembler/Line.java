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

public class Line {
    // Line stuff
    private int line_no;
    private String address, label, comment;
    private Instruction instr;
    // Other stuff
    private int size = 0;
    private Boolean isComment = false, isError = false;
    private String error_message = null;
    // just keep it for printing
    private String code_line;
    
    // TODO: Any extras should be done for directives?
    // **** UNHANDELED: Directives, literals, etc...
    public Line(String code_line, LocationCounter LOCCTR, SymbolTable symbolTable, int last_line_no) {
        this.code_line = code_line;
        this.line_no = last_line_no + 1;
        this.address = Assembler.decToHex(LOCCTR.getLocation(), 6);
        if(code_line.charAt(0) == '.') {
            this.isComment = true;
            this.comment = code_line;
            this.size = 0;
        }
        else {
            /***     Get label       ***/
            this.label = hamada(code_line, 0, 7);
            if(this.label.length() == 0) {
                this.label = null;
            }
            /***     Get instruction       ***/
            // Things that may be given in an instruction
            String mnemonic = null;
            ArrayList<String>operands = new ArrayList<>();
            Boolean isFormat4 = false;
            Boolean isImm = false, isIndir = false;
            
            // Get the mnemonic
            mnemonic = hamada(code_line, 9, 14);
            // check format 4
            if(mnemonic.length() > 0 && mnemonic.charAt(0) == '+') {
                mnemonic = mnemonic.substring(1);
                isFormat4 = true;
            }
            
            // Get the operands list
            String operands_str = hamada(code_line, 17, 34);
            if(operands_str.length() > 0) {
                if(operands_str.charAt(0) == '#'){
                    isImm = true;
                    operands_str = operands_str.substring(1);
                } else if(operands_str.charAt(0) == '@'){
                    isIndir = true;
                    operands_str = operands_str.substring(1);
                }
            }
            if(operands_str != null) {
                operands = new ArrayList(Arrays.asList(operands_str.split(",")));
                
            }
            
            /***     Get comment       ***/
            this.comment = hamada(code_line, 35, 65);
            
            // System.out.println(label + " " + mnemonic + " " + operands + " " + comment);
            
            /***     Validate line       ***/
            // Validate label
            if(!validateNewLabel(this.label, symbolTable)) {
                this.isError = true;
                this.error_message = String.format("***Error: Symbol %s is already defined.", this.label);
            }
            // Validate mnemonic
            else if(InstructionSet.getInstruction(mnemonic) == null) {
                this.isError = true;
                this.error_message = String.format("***Error: mnemonic %s is undefined", mnemonic);
            }
            // Validate syntax and label operands
            else if(!validateInstruction(mnemonic, operands, isFormat4, isImm, isIndir, symbolTable)){
                if(this.error_message == null)
                    this.error_message = "***Error: Invalid syntax";
                this.isError = true;
            }
            /***   Create instruction   ***/
            else {
                int instrFormat = InstructionSet.getInstruction(mnemonic).getFormat();
                if(instrFormat == 1)
                    this.instr = new Instruction(mnemonic);
                else if(instrFormat == 2)
                    this.instr = new Instruction(mnemonic, operands, symbolTable);
                else
                    this.instr = new Instruction(mnemonic, operands, isFormat4, isImm, isIndir, symbolTable);
                this.size = this.instr.getFormat();
            }
        }
    }
    
    private Boolean validateInstruction(String mnemonic, ArrayList<String>operands, Boolean isFormat4, Boolean isImm, Boolean isIndir, SymbolTable symbolTable) {
        InstrDetails instrDetails = InstructionSet.getInstruction(mnemonic);
        if(instrDetails.getFormat() < 3 && (isFormat4 || isImm || isIndir)){
            return false;
        }
        int rNum = 0, lNum = 0, vNum = 0;
        for(int i = 0; i < operands.size(); i++) {
            if(!Operand.isValid(operands.get(i), symbolTable)) {
                this.error_message = String.format("***Error: Undefined operand %s", operands.get(i));
                return false;
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
        if(rNum != instrDetails.getRegOps() || vNum < instrDetails.getDirOps()) {
            return false;
        }
        int rem = lNum + vNum - instrDetails.getDirOps();
        if(rem != instrDetails.getMemOps()) {
            return false;
        }
        return true;
    }
    
    private Boolean validateNewLabel(String lbl, SymbolTable symbolTable) {
        if(lbl == null)
            return true;
        return !(Character.isDigit(lbl.charAt(0)) || symbolTable.isUsedLabel(lbl));
    }
    
    private String hamada(String str, int stt, int end) {
        if(stt >= str.length()){
            return "";
        }
        String ret = str.substring(stt, Math.min(end + 1, str.length()));
        ret = ret.replaceAll("\\s",""); // remove all whitespaces
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
    
    @Override
    public String toString() {
        if(isComment) {
            return String.format("%3d   %6s   %-66s", line_no, address, comment);
        }
        if(isError) {
            return String.format("%9s%s", "", error_message);
        }
        return String.format("%3d   %6s   %s", line_no, address, code_line);
    }
}
