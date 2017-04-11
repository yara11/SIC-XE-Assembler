package sicxe_assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Assembler {
    String registers_file = "register_set.txt";
    String instruction_set_file = "instruction_set.txt";
    public static HashMap<String,InstrDetails> InstrSet = new HashMap<>();
    public static HashMap<String,String> RegisterSet = new HashMap<>();
    
    public Assembler() {
        // Read the registers file
        try (BufferedReader br = new BufferedReader(new FileReader(registers_file))) {
            String line;
            while ((line = br.readLine()) != null) {
               // add entry to the HashMap: RegisterSet
               String[] parts = line.split(" ");
               RegisterSet.put(parts[0], parts[1]);
            }
            System.out.println("Register set: ");
            System.out.println(Arrays.asList(RegisterSet));
        }catch (IOException e) {
            e.printStackTrace();
        }
        
        // Read the instruction set file
        try (BufferedReader br = new BufferedReader(new FileReader(instruction_set_file))) {
            String line;
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(" ");
               InstrSet.put(parts[0], new InstrDetails(parts[0], Integer.valueOf(parts[1]), parts[2], Integer.valueOf(parts[3]), Integer.valueOf(parts[4]), Integer.valueOf(parts[5])));
            }
            System.out.println("Instruction set: ");
            for(String key: InstrSet.keySet()){
                InstrSet.get(key).printDetails();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    public static void main(String[] args) {
        Assembler start = new Assembler();
    }
    
}
