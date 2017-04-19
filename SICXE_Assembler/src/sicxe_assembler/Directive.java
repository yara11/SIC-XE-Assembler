package sicxe_assembler;
//validates directives
//sets the size which changes the location counter by a certain value
// validates the operand of the directive
public class Directive {
    private String name;
    private String operand;
    private int size;
    
    public Directive(String name, String operand) {
        this.name = name.toUpperCase();
        this.operand = operand.toUpperCase();
        if(name.equals("BYTE")) {
            int n = this.operand.length();
            if(operand.charAt(0) == 'C')
                this.size = n - 3;
            else if(operand.charAt(0) == 'X')
                this.size = n/2 - 1;
            
        } else if(name.equals("WORD")) {
            this.size = 3;
        } else if(name.equals("RESB")) {
            this.size = 1 * Integer.parseInt(operand.trim());
        } else if(name.equals("RESW")) {
            this.size = 3 * Integer.parseInt(operand.trim());
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
    
    public String getObjectCode() {
        if(this.name.equals("RESW") || this.name.equals("RESB"))
            return "";
        if(this.name.equals("WORD"))
            return Assembler.decToHex(Integer.parseInt(this.operand), size*2);
        if(this.name.equals("BYTE")) {
            if(this.operand.charAt(0) == 'X') {
                return operand.substring(2, operand.length() - 1);
            }
            else {
                String obj = "";
                for(int i = 2; i < this.operand.length()-1; i++){
                    int ascii = (int)this.operand.charAt(i);
                    obj += Assembler.decToHex(ascii, 2);
                }
                return obj;
            }
        }
        return null;
    }
}
