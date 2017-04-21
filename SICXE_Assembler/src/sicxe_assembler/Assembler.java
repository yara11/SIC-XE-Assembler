package sicxe_assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Assembler {

    private final String regFileName = "register_set.txt";
    private final String instrSetFileName = "instruction_set.txt";
    private static Boolean b, p;
    public static int target;

    public static Boolean getB() {
        //System.out.print("oide");
        return b;
    }

    public static void setB(Boolean b) {
        Assembler.b = b;
    }

    public static void setP(Boolean p) {
        Assembler.p = p;
    }

    public static Boolean getP() {
        return p;
    }
    // private String asmFileName;
    private ArrayList<Line> linesOfCode = new ArrayList<>();
    private static SymbolTable symbolTable = new SymbolTable();
    private LocationCounter LOCCTR = new LocationCounter(0);
    private static boolean enableBase = false;
    private static int base;
    //private static String baseLine;
    private static boolean baseError = false;
    private static ArrayList<Line> baseLines = new ArrayList<>();
    private static int baseCounter = 0;

    public Assembler() {
        // Load the registers file
        RegisterSet.getInstance(regFileName);
        // Load instruction set file
        InstructionSet.getInstance(instrSetFileName);

    }

    public Boolean pass1(String asmFileName, String outputSrcFileName) {
        //this.collectLabels(asmFileName);
        // Load source code file
        int line_no = 0;
        Boolean genPass2 = true;
        try (BufferedReader br = new BufferedReader(new FileReader(asmFileName))) {
            FileWriter fw = new FileWriter(outputSrcFileName);
            FileWriter symW = new FileWriter("SymbolTable.txt");
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Line cur_line = new Line(line, LOCCTR, symbolTable, line_no);
                linesOfCode.add(cur_line);
                if (line_no == 0 && Line.hamada(line, 9, 14).toUpperCase().equals("START")) {
                    int adrs = Integer.parseInt(Line.hamada(line, 17, 34).trim(), 16);
                    cur_line.unError();
                    LOCCTR = new LocationCounter(adrs);
                    cur_line.setAddress(decToHex(LOCCTR.getLocation(), 6));
                    //System.out.print(cur_line.getObjectCode(symbolTable));
                }
                // TODO: VALIDATE "END OPERAND" OR "LABEL"
                if (Line.hamada(line, 9, 14).toUpperCase().equals("END")) {
                    cur_line.unError();
                    fw.write(cur_line.toString() + '\n');
                    System.out.println(cur_line.toString());
                    break;
                }
                // TODO: add nobase
                if (Line.hamada(line, 9, 14).toUpperCase().equals("BASE")) {
                    cur_line.unError();
                    baseLines.add(cur_line);
                    //enableBase = true;
                    fw.write(cur_line.baseString() + '\n');
                    System.out.println(cur_line.baseString());
                    line_no++;
                    continue;
                }
                if (!cur_line.isValid()) {
                    genPass2 = false;
                }
                // Update symbol table
                if (cur_line.getLabel() != null) {
                    symbolTable.addSymbol(cur_line.getLabel(), decToHex(LOCCTR.getLocation(), 6));
                    symW.write(String.format("%-16s       %s\n", cur_line.getLabel(), decToHex(LOCCTR.getLocation(), 6)));
                }
                // Update location counter
                LOCCTR.increment(cur_line.getSize());
                line_no++;
                fw.write(cur_line.toString() + '\n');
                System.out.println(cur_line.toString());
            }
            br.close();
            fw.close();
            symW.close();
        } catch (IOException e) {
        }
        return true;
    }

    public void validateLabelswKeda() {
        for (Line curLine : linesOfCode) {
            if (!curLine.validateOperands(this.symbolTable)) {
                //  this.symbolTable.removeSymbol(curLine.getLabel());
            } else if (curLine.getLabel() != null) {

            }
        }
    }

    // Because labels may be declared after they are used,
    // we need a way to save them before sgkzdjngldsin
    /*private void collectLabels(String asmFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(asmFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty())
                    continue;
                String label = line.substring(0, Math.min(8, line.length()));
                symbolTable.addSymbol(label, null);
            }
            br.close();
        }catch (IOException e) {
        }
    }*/
    // converts decimal int to hex string with specified number of digits
    public static String decToHex(int n, int digits) {
        String hex_num = Integer.toHexString(n);
        int len = hex_num.length();
        for (int i = 0; i < digits - len; i++) {
            hex_num = '0' + hex_num;
        }
        return hex_num;
    }

    public static int getBaseValue(int baseCounter) {
        //System.out.println("test");
        String str = baseLines.get(baseCounter).getDir().getOperand();
        //System.out.print(str);
        if (symbolTable.isLabel(str) == null) {
            baseError = true;
        } else {
            return Integer.parseInt(symbolTable.getLocation(str));
        }

        return 0;
    }

    public void pass2(String asmFileName, String outputSrcFileName, SymbolTable symbolTable) {
        
        System.out.println("\n\n~~~~~~~PASS 2~~~~~~~~\n\n");
        int i = 0;
        while (i < linesOfCode.size()) {

            Line line = linesOfCode.get(i);
            if(!line.validateOperands(symbolTable)) {
                System.out.println(line.toString());
                break;
            }
            if (baseCounter < baseLines.size() && line.getLine_no() == baseLines.get(baseCounter).getLine_no()) {
                //System.out.println("test");
                enableBase = true;
                base = getBaseValue(baseCounter);
                //System.out.println(base);
                baseCounter++;
            }
            ArrayList<Operand> op = new ArrayList<>();
            if (line.getIsError() == false && line.getIsComment() == false && line.getInstr() != null && line.getIsDirective() == false && line.getInstr().getOperands().size() > 0) {
                op = line.getInstr().getOperands();
                if (op.get(0).getType() == 'l') {
                    //String l = op.get(0).getName();
                    // System.out.print(" operand "+l);
                    String n = op.get(0).getCode(symbolTable, line.getInstr().getFormat());
                    if(n == null)
                        System.out.println(String.format("BLAME THIS: %s %s", line.getInstr().getMnemonic(), op.get(0).getName()));
                    //           System.out.println(" location "+n);
                    int TA = hex2dec(n);
                    //System.out.println("target address  "+TA);
                    int current = hex2dec(linesOfCode.get(i + 1).getAddress());
                    // System.out.print("current " + current);
                    target = TA - current;
                    setBP(target);
                    if (b == false && p == false) {
                        target = TA;
                    } else if (b == true && p == false) {
                        target = TA - base;
                    }
                    else if (b == false && p == true) {
                        target = TA - current;
                    }
                    if(target < 0) {
                        target = 2048-target;
                    }
                    //System.out.println("target " + target + "TA " + TA + "current " + current);

                }
            }
            

            String s = line.getObjectCode(symbolTable);
            line.text= s;
            String ret = String.format("%3d   %6s   %s\t      %s", line.getLine_no(), line.getAddress(), line.getCode_line(), s);
            System.out.println(ret);
            // String s1 = symbolTable.getLocation(line.getInstr().getOperands().get(0).getCode(symbolTable, target));
            //System.out.print(" operand "+s1);
            //int TA = Integer.parseInt(s1);
            //System.out.print(TA);
            //  int current = Integer.parseInt(linesOfCode.get(i+1).getAddress());
            //target = TA - current;

            i++;
        }

    }

    public static int hex2dec(String s) {
//            if(s == null)
//                return 0;
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    public static void setBP(int target) {
        if (enableBase == false) {
            b = false;
            p = true;
            //continue;
        } else if ((target) <= 2047 && (target) >= -2048) {
            b = false;
            p = true;
        } else if ((target) <= 4096 && (target) >= 0) {
            b = true;
            p = false;
        }
    }
    public  ArrayList<String> mod(){

         int i = 0;
         ArrayList<String> mods = new ArrayList<>();
        while (i < linesOfCode.size()) {

            Line line = linesOfCode.get(i);
            if(line.getInstr()!= null && line.getInstr().getFormat() == 4 && line.getInstr().getMnemonic().charAt(0)!='#' && !"RSUB".equals(line.getInstr().getMnemonic())){
                    
                String r = "M";
                                  
                int loc = hex2dec(line.getAddress())+1;
                String start = decToHex(loc, 6);
                r += start;
                r += "05";
                mods.add(r);

            }
            i++;
        
        }
        return mods;
    }
    public String newText(int i){
        String r= "T";
        while(i < linesOfCode.size()){
                    //System.out.println("dsfs");
            String t = "";
            while(t.length() <= 60 ){
                if(" ".equals(linesOfCode.get(i).getObjectCode(symbolTable))){
                    break;
                }
                t += linesOfCode.get(i).getObjectCode(symbolTable);
                        System.out.println(t);
                        i++;
                                    System.out.println(i);

            }
             r += t.length() + t;
         }
        return r;
    }
    public ArrayList<String> text(){
        System.out.println("dsfs");
        int i=0;
        ArrayList<String> texts = new ArrayList<>();
        while(i < linesOfCode.size()){
                    //System.out.println("dsfs");
            String t = "";
            int st = i;
            while(i < linesOfCode.size() && (t.length()+linesOfCode.get(i).text.length()) <= 60 &&!" ".equals(linesOfCode.get(i).text)){
                
                t += linesOfCode.get(i).text;
                        
                     i++;   
            }
            while(i < linesOfCode.size() && " ".equals(linesOfCode.get(i).text))
                i++;
           // i=linesOfCode.get(i).getLine_no();
            String r = "T" + linesOfCode.get(st).getAddress().toUpperCase()+ (t.length()) + t.toUpperCase();
            //System.out.println(r);
            texts.add(r);
           // i++;
           
        
        }
        
        
        return texts;
    }
    public String head(){
            int end = hex2dec(linesOfCode.get(linesOfCode.size()-1).getAddress());
            int start = hex2dec(linesOfCode.get(0).getAddress());
            int length = end - start;
            String h = "H";
            h += linesOfCode.get(0).getLabel() + linesOfCode.get(0).getAddress() + decToHex(length,6);
    
        return h;
    }
    public String end(){
        String e = "E";
        e += linesOfCode.get(0).getAddress();
        
    return e;
    }

    public static void main(String[] args) {
        String asmFileName = "example4.txt";
        String srcCodeFileName = "src-prog-" + asmFileName;
        Assembler assembler = new Assembler();
        Boolean pass1result = assembler.pass1(asmFileName, srcCodeFileName);
        //ToDo: nzabat mkanha fein
        if (enableBase == true) {
            base = getBaseValue(baseCounter);
        }
        //System.out.print(base);
        if(pass1result)
            assembler.pass2(asmFileName, srcCodeFileName, symbolTable);
        ArrayList<String> modRecords;
               
        modRecords = assembler.mod();
        int i =0;
        while(i < modRecords.size()){
                System.out.println(modRecords.get(i));
                i++;
        }
        String head = assembler.head();
        System.out.println(head);
        String end = assembler.end();
        System.out.println(end);
        
         ArrayList<String> textRecords;
               
        textRecords = assembler.text();
        i =0;
        while(i < textRecords.size()){
                System.out.println(textRecords.get(i));
                i++;
        }
        // Assembler start = new Assembler();
        // System.out.println(String.format("%3d   %6s   %8s   %6s   %18s   %31s", 1, "0003A0", "TERMPROJ", "START", "3A0", ""));
    }

}
