package sicxe_assembler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

// Another Singleton class for the RegisterSet

public class RegisterSet {
    private static RegisterSet regSetObj = null;
    private static HashMap<String,String> RegSet = new HashMap<>();
    
    private RegisterSet(String file_name) {
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = br.readLine()) != null) {
               // add entry to the HashMap: RegisterSet
               String[] parts = line.split(" ");
               RegSet.put(parts[0], parts[1]);
            }
            // System.out.println("Register set: ");
            // System.out.println(Arrays.asList(RegSet));
        }catch (IOException e) {
        }
    }
    
    public static RegisterSet getInstance(String file_name) {
        if(regSetObj == null) {
            regSetObj = new RegisterSet(file_name);
        }
        return regSetObj;
    }
    
    public static String getRegCode(String name) {
        return RegSet.get(name.toUpperCase());
    }
    
    public static Boolean isRegister(String name) {
        return RegSet.get(name.toUpperCase()) != null;
    }
}
