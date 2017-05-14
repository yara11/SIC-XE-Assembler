package sicxe_assembler;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    //private HashMap<String,String> SymTable = new HashMap<>();
    private ArrayList<SymbolTableEntry> SymTable = new ArrayList<>();
    
    public void addSymbol(String symbol, String value, char flag) {
        SymTable.add(new SymbolTableEntry(symbol, value, flag));
        //SymTable.put(symbol, location);
    }
    
    // Will return null if object is not found
    public String getLocation(String symbol) {
        for(SymbolTableEntry e: SymTable) {
            if(e.getSymbol().equals(symbol))
                return e.getValue();
        }
        return null;
    }
    
    public Boolean isLabel(String symbol) {
        for(SymbolTableEntry e: SymTable) {
            if(e.getSymbol().equals(symbol))
                return true;
        }
        return false;
    }
    //
    //elmfrood msh hn7tagha delwa2ty
    /*
    public void removeSymbol(String symbol) {
        SymTable.remove(symbol);
    }
    */
    /*public Boolean isUsedLabel(String symbol) {
        return SymTable.get(symbol) != null;
    }*/
}
