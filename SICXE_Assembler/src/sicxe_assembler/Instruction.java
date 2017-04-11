package sicxe_assembler;

public abstract class Instruction {
	protected int size;
	protected String mnemonic;
        public Instruction(String mnemonic) {
            this.mnemonic = mnemonic;
        }
        public int getSize() {
            return this.size;
        }
	abstract public String getObjectCode();
}
