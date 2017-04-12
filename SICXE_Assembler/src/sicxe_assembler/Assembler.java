package sicxe_assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

public class Assembler {
    String registers_file = "register_set.txt";
    String instruction_set_file = "instruction_set.txt";
    // public static HashMap<String,InstrDetails> InstrSet = new HashMap<>();
    // public static HashMap<String,String> RegisterSet = new HashMap<>();
    
    public Assembler() {
        // Load the registers file
        RegisterSet.getInstance(registers_file);
        
        // Load instruction set file
        InstructionSet.getInstance(instruction_set_file);
    }
    
    
    public static void main(String[] args) {
        //Assembler start = new Assembler();
        System.out.println(String.format("%3d   %6s   %8s   %6s   %18s   %31s", 1, "0003A0", "TERMPROJ", "START", "3A0", ""));
    }
    
}
