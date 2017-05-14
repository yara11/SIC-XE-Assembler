/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe_assembler;

/**
 *
 * @author User
 */
public class SymbolTableEntry {
    private String symbol, value;

    public String getSymbol() {
        return symbol;
    }

    public String getValue() {
        return value;
    }

    public char getFlag() {
        return flag;
    }
    private char flag;
    
    public SymbolTableEntry(String symbol, String value, char flag) {
        this.symbol = symbol;
        this.value = value;
        this.flag = flag;
    }
}
