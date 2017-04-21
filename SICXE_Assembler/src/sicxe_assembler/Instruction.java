package sicxe_assembler;

import java.util.ArrayList;
//building the instruction flags to be used for object code
//creating instruction objects
//checking indexed

public class Instruction {

    // Missing: How to set b & p
    private int format;
    private String mnemonic;
    private Boolean n = false, i = false, b = false, p = false;
    private Boolean x = false;
    private Boolean e = false;

    public Boolean getB() {
        return b;
    }

    public void setB(Boolean b) {
        this.b = b;
    }

    public Boolean getP() {
        return p;
    }

    public void setP(Boolean p) {
        this.p = p;
    }
    private ArrayList<Operand> operands = new ArrayList<>();

    // Constructor for format 1
    public Instruction(String mnemonic) {
        this.format = 1;
        this.mnemonic = mnemonic;
    }

    // Constructor for format 2
    public Instruction(String mnemonic, ArrayList<String> operandNames, SymbolTable symTable) {
        this.format = 2;
        this.mnemonic = mnemonic;
        this.getOperands(operandNames, symTable);
    }

    // Constructor for format 3, 4
    public Instruction(String mnemonic, ArrayList<String> operandNames, Boolean isFormat4, Boolean isImm, Boolean isIndir, SymbolTable symTable) {
        this.format = 3;
        if (isFormat4) {
            this.format = 4;
            this.e = true;
        }
        this.mnemonic = mnemonic;
        this.getOperands(operandNames, symTable);
        this.n = this.i = true;
        if (isImm) {
            this.b = this.p = this.n = false;
        }
        if (isIndir) {
            this.i = false;
        }
    }

    // Fills operands list and sets x flag if applicable
    private void getOperands(ArrayList<String> operandNames, SymbolTable symTable) {
        for (int i = 0; i < operandNames.size(); i++) {
            if (i == operandNames.size() - 1 && operandNames.get(i).toUpperCase().equals("X")) {
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
                objectCode = opcode;
                break;
            case 2:
                objectCode = opcode;
                objectCode += this.operands.get(0).getCode(symbolTable, 2);
                if (this.operands.size() == 1) {
                    objectCode += "0";
                    break;
                }
                objectCode += this.operands.get(1).getCode(symbolTable, 2);

                break;
            case 3:
                //System.out.println("test1");
                objectCode = hexPlusDec(opcode, toInt(n) * 2 + toInt(i));
                //System.out.println("test1");
                Boolean a = true;
                //objectCode += this.toint(a);
                objectCode += decToHex(toint(x), toint(Assembler.getB()), toint(Assembler.getP()), toint(e));
                // System.out.println("test1");
                for (Operand operand : this.operands) {
                    objectCode += operand.getCode(symbolTable, this.format).substring(3);

                }
                break;
            case 4:
                //Assembler.setB(false);
                //Assembler.setP(false);
                objectCode = hexPlusDec(opcode, toInt(n) * 2 + toInt(i));
                objectCode += decToHex(toint(x), toint(Assembler.getB()), toint(Assembler.getP()), toint(e));
                //objectCode += Assembler.decToHex(toInt(x)*8 + toInt(b)*4 + toInt(p)*2 +toInt(e), 1);
                for (Operand operand : this.operands) {
                    objectCode += "op";
                    objectCode += operand.getCode(symbolTable, 4).substring(1);

                }
                break;
            default:
                break;
        }
        /*for(Operand operand: this.operands) {
                objectCode += operand.getCode(symbolTable, this.format);
            }*/
        return objectCode;
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

    private char toint(Boolean f) {
        if (f == null) {
            return '2';
        }
        if (f == true) {
            return '1';
        } else {
            return '0';
        }
    }

    private String decToHex(char a, char b, char c, char d) {
        int A = (int) a - '0';
        // System.out.print(A);
        int B = b - '0';
        //   System.out.print(B);

        int C = c - '0';
        //   System.out.print(C);

        int D = d - '0';
        //  System.out.print(D);

        int res = 8 * A + 4 * B + 2 * C + D;
        //   System.out.println(" result here "+res);

        String str;
        switch (res) {
            case 10:
                str = "A";
                break;
            case 11:
                str = "B";
                break;
            case 12:
                str = "C";
                break;
            case 13:
                str = "D";
                break;
            case 14:
                str = "E";
                break;
            case 15:
                str = "F";
                break;
            default:
                str = String.valueOf(res);
        }
        return str;

    }

    public String getMnemonic() {
        return this.mnemonic;
    }
    
    public Boolean getI() {
        return this.i;
    }
}
