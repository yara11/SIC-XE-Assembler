package sicxe_assembler;

// Used for:
// Validation, 
// get opcode for object code,
// get format for LOCCTR
public class InstrDetails {
    private String mnemonic, opcode;
    private int format, memOps, regOps, dirOps;
    public InstrDetails(String mnemonic, int format, String opcode, int memOps, int regOps, int dirOps) {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.format = format;
        this.memOps = memOps;
        this.regOps = regOps;
        this.dirOps = dirOps;
    }
    public int getFormat() {
        return this.format;
    }
    public String getOpcode() {
        return this.opcode;
    }
    // For testing purposes
    public void printDetails() {
        System.out.println(String.format("%s %d %s %d %d %d", mnemonic, format, opcode, memOps, regOps, dirOps));
    }
}
