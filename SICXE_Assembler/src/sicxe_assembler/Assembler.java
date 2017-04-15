package sicxe_assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Assembler {
    private final String registers_file = "register_set.txt";
    private final String instruction_set_file = "instruction_set.txt";
    private String assembly_file_name;
    private ArrayList<Line> linesOfCode = new ArrayList<>();
    private SymbolTable symbolTable = new SymbolTable();
    private LocationCounter LOCCTR = new LocationCounter(0);
    
    public Assembler(String filename) {
        // Load the registers file
        RegisterSet.getInstance(registers_file);
        // Load instruction set file
        InstructionSet.getInstance(instruction_set_file);
        // Perform pass one
        this.assembly_file_name = filename;
    }
    
    public void pass1() {
        // Load source code file
        int line_no = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(assembly_file_name))) {
            String line;
            while ((line = br.readLine()) != null) {
                Line cur_line = new Line(line,LOCCTR,symbolTable,line_no);
                linesOfCode.add(cur_line);
                if(cur_line.isValid()) {
                    // Update symbol table
                    if(cur_line.getLabel() != null)
                        symbolTable.addSymbol(cur_line.getLabel(), decToHex(LOCCTR.getLocation(), 6));
                    // Update location counter
                    LOCCTR.increment(cur_line.getSize());
                    line_no++;
                }
            }
        }catch (IOException e) {
        }
        
    }
    
    // converts decimal int to hex string with specified number of digits
    public static String decToHex(int n, int digits) {
        String hex_num = Integer.toHexString(n);
        int len = hex_num.length();
        for(int i = 0; i < digits - len; i++)
            hex_num = '0' + hex_num;
        return hex_num;
    }
    
    
    public static void main(String[] args) {
        Assembler assembler = new Assembler("example.txt");
        // Assembler start = new Assembler();
        // System.out.println(String.format("%3d   %6s   %8s   %6s   %18s   %31s", 1, "0003A0", "TERMPROJ", "START", "3A0", ""));
    }
    
}
