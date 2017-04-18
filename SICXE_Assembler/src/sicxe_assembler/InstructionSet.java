package sicxe_assembler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

// Singleton class InstructionSet to ensure we only have one object :D
//reading instruction set and putting them in a hashmap
public class InstructionSet {
    private static InstructionSet instrSetObj = null;
    private static HashMap<String,InstrDetails> InstrSet = new HashMap<>();
    
    private InstructionSet(String file_name) {
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(" ");
               InstrSet.put(parts[0], new InstrDetails(parts[0], Integer.valueOf(parts[1]), parts[2], Integer.valueOf(parts[3]), Integer.valueOf(parts[4]), Integer.valueOf(parts[5])));
            }
            /* System.out.println("Instruction set: ");
            for(String key: InstrSet.keySet()){
                InstrSet.get(key).printDetails();
            } */
        }catch (IOException e) {
        }
    }
    
    public static InstructionSet getInstance(String file_name) {
        if(instrSetObj == null) {
            instrSetObj = new InstructionSet(file_name);
        }
        return instrSetObj;
    }
    
    // Note: will return null if not found
    public static InstrDetails getInstruction(String mnemonic) {
        return InstrSet.get(mnemonic.toUpperCase());
    }
}
