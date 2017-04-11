package sicxe_assembler;

public class Instr2 extends Instruction {
	private String r1, r2;
	public Instr2(String mnemonic, String r1, String r2) {
            super(mnemonic);
            this.r1 = r1;
            this.r2 = r2;
            this.size = 2;
	}
        @Override
	public String getObjectCode(){
            String opcode = Assembler.InstrSet.get(this.mnemonic.toUpperCase()).getOpcode(); // 2 hex digits
            String r1_code = Assembler.RegisterSet.get(this.r1.toUpperCase()); // 1 hex digit
            String r2_code = Assembler.RegisterSet.get(this.r2.toUpperCase()); // 1 hex digit
            return opcode + r1_code + r2_code;
	}
}
