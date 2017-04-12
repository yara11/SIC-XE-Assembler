// This class describes a line of assembly code

package sicxe_assembler;
import java.util.ArrayList;
import java.util.Formatter;

public class Line {
    private int line_no;
    private Boolean isError, isComment;
    private String address, label, mnemonic, comment, error_message;
    private ArrayList<String> operands;
    
    // TODO: Any extras should be done for directives?
    public Line(String code_line, LocationCounter LOCCTR, int last_line_no) {
        if(code_line.charAt(0) == '.') {
            this.isComment = true;
            this.line_no = last_line_no + 1;
            this.address = decToHex(LOCCTR.getLocation(), 6);
            this.comment = code_line;
        }
        // TODO : validate instruction
            // if invalid -> create error line
            // else -> create an instruction line
    }
    
    public String getLine() {
        if(isComment) {
            return String.format("%3d   %6s   %66s", line_no, address, comment);
        }
        if(isError) {
            return String.format("%9s%s", "",error_message);
        }
        String operands_string = "";
        for(int i = 0; i < operands.size(); i++) {
            if(i != 0) operands_string += ',';
            operands_string += operands.get(i);
        }
        return String.format("%3d   %6s   %8s   %6s   %18s   %31s", line_no, address, label, mnemonic, operands_string, comment);
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
