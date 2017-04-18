package sicxe_assembler;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String,String> SymTable = new HashMap<>();
    
    public void addSymbol(String symbol, String location) {
        SymTable.put(symbol, location);
    }
    
    // Will return null if object is not found
    public String getLocation(String symbol) {
        return SymTable.get(symbol);
    }
    
    public Boolean isLabel(String symbol) {
        return SymTable.containsKey(symbol);
    }
    
    public void removeSymbol(String symbol) {
        SymTable.remove(symbol);
    }
    
    /*public Boolean isUsedLabel(String symbol) {
        return SymTable.get(symbol) != null;
    }*/
}
