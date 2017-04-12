package sicxe_assembler;

public class Instr3 extends Instruction {
        private String operand;
	private Boolean n, i, x, b, p, e;
	public Instr3(String mnemonic, String operand, Boolean e) {
            super(mnemonic);
            this.n = this.i = true;     // Default is 'simple'
            this.x = this.b = this.p = false;
            this.operand = operand;
            if(this.e) {
                    this.size = 4;
            } else {
                    this.size = 3;
            }
	}
        // n, i, x, b, p
        public void setIndirect(){
            this.n = true;
            this.i = false;
        }
        public void setImmediate(){
            this.n = false;
            this.i = true;
        }
        public void setIndexed(){
            this.x = true;
        }
	public void setBaseRelative() {
            this.b = true;
            this.p = false;
	}
	public void setPCRelative() {
            this.b = false;
            this.p = true;
	}

    // TODO : Convert displacement before you get object code !!
    @Override
    public String getObjectCode() {
        String first_byte, flags, disp;
        // first byte:
        String opcode = InstructionSet.getInstruction(this.mnemonic).getOpcode();
        int ni = toInt(this.b)*2 + toInt(this.p);
        first_byte = decToHex(Integer.parseInt(opcode, 16) + ni, 2);
        // flags:
        int fs = toInt(x)*8 + toInt(b)*4 + toInt(p)*2 + toInt(e);
        flags = decToHex(fs, 1);
        disp = getDisplacement();
        return first_byte + flags + disp;
    }
    
    // TODO : Implement this !!
    // Function : from the operand it gets the address as a string hexadecimal,
    // this will be 5 digits if format 4, and 3 digits if format 2.
    private String getDisplacement() {
        return "";
    }
    
    
    private int toInt(Boolean f) {
        return f ? 1 : 0;
    }
    
    // converts decimal int to hex string with specified number of digits
    private String decToHex(int n, int digits) {
        String hex_num = Integer.toHexString(n);
        int len = hex_num.length();
        for(int i = 0; i < digits - len; i++)
            hex_num = '0' + hex_num;
        return hex_num;
    }
    
}
