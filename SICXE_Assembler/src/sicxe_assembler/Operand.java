package sicxe_assembler;

// validating operands
public class Operand {
    private char type; // 'r' = register, 'l' = label, 'v' = value
    private String name;
    
    public Operand(String name, SymbolTable symbolTable) {
        this.name = name;
        if(isNumber(name)) {
            this.type = 'v';
        }
        else if(RegisterSet.isRegister(name)) {
            this.type = 'r';
        }
        else /*if(symbolTable.isLabel(name))*/ {
            this.type = 'l';
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public char getType() {
        return this.type;
    }
    
    public static Boolean isValid(String str, SymbolTable symbolTable) {
        return isDecimal(str) || RegisterSet.isRegister(str) || symbolTable.isLabel(str);
    }
    
    private Boolean isNumber(String str) {
        if(str == null || str.length() == 0)
            return false;
        if(!Character.isDigit(str.charAt(0)))
            return false;
        for(int i = 1; i < str.length(); i++) {
            char c = Character.toUpperCase(str.charAt(i));
            if(!Character.isDigit(c) && c != 'A' && c != 'B' && c !='C' && c != 'D' && c != 'E' && c != 'F')
                return false;
        }
        return true;
    }
    
    private static Boolean isDecimal(String str) {
        for(int i = 0; i < str.length(); i++)
            if(!Character.isDigit(str.charAt(i)))
                return false;
        return true;
    }
    
    public String getCode(SymbolTable symbolTable, int format) {
        switch (this.type) {
            case 'r':
                return RegisterSet.getRegCode(this.name);
            case ' ':
                return "0000";
            case 'v':
                return Assembler.decToHex(Integer.parseInt(this.name), format*2-3);
            default:
                String loc = symbolTable.getLocation(name);
                //System.out.print("target  "+Assembler.target);
                //System.out.print(loc);
                return loc;
                /*if(loc == null) // TODO: return null instead to mark error in pass2?
                    loc = "000000";
                if(format == 3)
                    return loc.substring(3);
                return loc.substring(1);*/
        }
    }
}
