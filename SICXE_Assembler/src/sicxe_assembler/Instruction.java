package sicxe_assembler;
import java.util.ArrayList;
//building the instruction flags to be used for object code
//creating instruction objects
//checking indexed

public class Instruction {
    // Missing: How to set b & p
	private int format;
	private String mnemonic;
        private Boolean n, i, x, b, p, e;
        private ArrayList<Operand> operands = new ArrayList<>();
        
        // Constructor for format 1
        public Instruction(String mnemonic) {
            this.format = 1;
            this.mnemonic = mnemonic;
        }
        
        // Constructor for format 2
        public Instruction(String mnemonic, ArrayList<String>operandNames, SymbolTable symTable) {
            this.format = 2;
            this.mnemonic = mnemonic;
            this.getOperands(operandNames, symTable);
        }
        
        // Constructor for format 3, 4
        public Instruction(String mnemonic, ArrayList<String> operandNames, Boolean isFormat4, Boolean isImm, Boolean isIndir, SymbolTable symTable) {
            this.format = 3;
            if(isFormat4) {
                this.format = 4;
                this.e = true;
            }
            this.mnemonic = mnemonic;
            this.getOperands(operandNames, symTable);
            this.n = this.i = true;
            if(isImm) 
                this.n = false;
            if(isIndir) 
                this.i = false;
        }
        
        // Fills operands list and sets x flag if applicable
        private void getOperands(ArrayList<String>operandNames, SymbolTable symTable) {
            for(int i = 0; i < operandNames.size(); i++) {
                if(i == operandNames.size()-1 && operandNames.get(i).toUpperCase().equals("X")) {
                    this.x = true;
                    continue;
                }
                this.operands.add(new Operand(operandNames.get(i), symTable));
            }
        }
        
        // TODO
	public String getObjectCode(SymbolTable symbolTable) {
            String objectCode = null;
            String opcode = InstructionSet.getInstruction(this.mnemonic).getOpcode(); // 2 hex digits
            switch (format) {
                case 1:
                case 2:
                    objectCode = opcode;
                    break;
                case 3:
                case 4:
                    objectCode = hexPlusDec(opcode, toInt(n)*2 + toInt(i));
                    objectCode += Assembler.decToHex(toInt(x)*8 + toInt(b)*4 + toInt(p)*2 +toInt(e), 1);
                    break;
                default:
                    break;
            }
            for(Operand operand: this.operands) {
                objectCode += operand.getCode(symbolTable, this.format);
            }
            return null;
        }
        
        public int getFormat() {
            return this.format;
        }

    public ArrayList<Operand> getOperands() {
        return this.operands;
    }
    
    // returns a byte in hexadecimal
    private String hexPlusDec(String hexnum, int decnum) {
        int ret = Integer.parseInt(hexnum, 16) + decnum;
        return Assembler.decToHex(ret, 2);
    }
    
    private int toInt(Boolean f) {
        return f ? 1 : 0;
    }
}
