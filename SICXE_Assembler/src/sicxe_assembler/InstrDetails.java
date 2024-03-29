package sicxe_assembler;
//these are the details read fron instruction file

// Used for:
// Validation, 
// get opcode for object code,
// get format for LOCCTR
//printing instruction details for debugging
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
    public int getMemOps() {
        return this.memOps;
    }
    public int getDirOps() {
        return this.dirOps;
    }
    public int getRegOps() {
        return this.regOps;
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
