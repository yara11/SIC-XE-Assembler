package sicxe_assembler;

public class Directive {
    private String name;
    private String operand;
    private int size;
    
    public Directive(String name, String operand) {
        this.name = name.toUpperCase();
        this.operand = operand.toUpperCase();
        switch (name) {
            case "BYTE":
                int n = this.operand.length();
                if(operand.charAt(0) == 'C')
                    this.size = n - 3;
                else if(operand.charAt(0) == 'X')
                    this.size = n/2 - 1;
                break;
            case "WORD":
                this.size = 3;
                break;
            case "RESB":
                this.size = 1 * Integer.parseInt(operand.trim());
                break;
            case "RESW":
                this.size = 3 * Integer.parseInt(operand.trim());
                break;
            default:
                break;
        }
    }
    
    static Boolean isDirective(String name, String operand) {
        //String str = name.toUpperCase();
        name = name.toUpperCase();
        operand = operand.toUpperCase();
        if(name.equals("RESW") || name.equals("RESB") || name.equals("WORD")) {
            return isDecimal(operand);
        }
        else if(name.equals("BYTE")) {
            if(operand.length() < 3 ||  operand.charAt(1) != '\'' ||operand.charAt(operand.length()-1) != '\'')
                return false;
            
            switch (operand.charAt(0)) {
                case 'C':
                    return true;
                case 'X':
                    return isNumber(operand.substring(2, operand.length() - 1));
                default:
                    return false;
            }
        }
        return false;
    }
    
    static Boolean isDecimal(String str) {
        for(int i = 0; i < str.length(); i++)
            if(!Character.isDigit(str.charAt(i)))
                return false;
        return true;
    }
    
    public int getSize() {
        return this.size;
    }
    
    private static Boolean isNumber(String str) {
    if(str == null || str.length() == 0)
        return false;
    for(int i = 0; i < str.length(); i++) {
        char c = Character.toUpperCase(str.charAt(i));
        if(!Character.isDigit(c) && c != 'A' && c != 'B' && c !='C' && c != 'D' && c != 'E' && c != 'F')
            return false;
    }
    return true;
}
}
