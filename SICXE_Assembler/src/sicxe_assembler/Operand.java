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
        return isNumber(str) || RegisterSet.isRegister(str) || symbolTable.isLabel(str);
    }
    
    private static Boolean isNumber(String str) {
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
}
