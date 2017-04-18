package sicxe_assembler;
import java.util.ArrayList;

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
	public String getObjectCode() {
            String opcode = InstructionSet.getInstruction(this.mnemonic).getOpcode(); // 2 hex digits
            if(format == 1) {
                return opcode;
            }
            if(format == 2) {
                
            }
            
            return null;
        }
        
        public int getFormat() {
            return this.format;
        }

    public ArrayList<Operand> getOperands() {
        return this.operands;
    }
}
