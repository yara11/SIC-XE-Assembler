package sicxe_assembler;

public class Instr1 extends Instruction {
	public Instr1(String mnemonic) {
            super(mnemonic);
            this.size = 1;
	}
        @Override
	public String getObjectCode(){
            return InstructionSet.getInstruction(this.mnemonic).getOpcode(); // 2 hex digits
	}
}
