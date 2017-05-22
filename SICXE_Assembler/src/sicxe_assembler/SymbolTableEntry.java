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
    private char flag; // 'R' or 'A'

    public SymbolTableEntry(String symbol, String value, char flag) {
        this.symbol = symbol;
        this.value = value;
        this.flag = flag;
    }
    
    public String getSymbol() {
        return symbol;
    }

    public String getValue() {
        return value;
    }
    
    public int getDecimalValue() {
        return Integer.parseInt(value, 16);
    }

    public char getFlag() {
        return flag;
    }
    
    @Override
    public String toString() {
        return String.format("%-16s %-5c  %-10s\n", symbol, flag, value);
    }
}
