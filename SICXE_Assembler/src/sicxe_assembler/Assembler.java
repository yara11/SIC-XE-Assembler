package sicxe_assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.math.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Assembler {

    private final String regFileName = "register_set.txt";
    private final String instrSetFileName = "instruction_set.txt";
    private static Boolean b, p;
    public static int target;
    public static ArrayList<String> definitions = new ArrayList<>();
    public static ArrayList<String> references = new ArrayList<>();
    public static ArrayList<Lit> literals = new ArrayList<>();
    public static ArrayList<SymbolTable> symtables = new ArrayList<>();
    Boolean org_enable = false;
    int prev_locctr;
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
    private static int consec = 1;
    private static  ArrayList<String> RRecords= new ArrayList<>();
    private static  ArrayList<String> DRecords= new ArrayList<>();
    
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

    public Assembler() {
        // Load the registers file
        RegisterSet.getInstance(regFileName);
        // Load instruction set file
        InstructionSet.getInstance(instrSetFileName);

    }

    public Boolean pass1(String asmFileName, String outputSrcFileName) {
        ArrayList<String> records = new ArrayList<>();
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
                if ( Line.hamada(line, 9, 14).toUpperCase().equals("EXTDEF")) {
                    String def = Line.hamada(line, 16, line.length()-1);
                    System.err.println(def);
                    String[] defs = def.split(",");
                    for(String d:defs)
                    {
                        definitions.add(d);
                    }
                    
                }
                if ( Line.hamada(line, 9, 14).toUpperCase().equals("EXTREF")) {
                    String ref = Line.hamada(line, 16, line.length()-1);
                    System.err.println(ref);
                    String rrecord="R^";
                    String[] refs = ref.split(",");
                    for(String r:refs)
                    {
                        references.add(r);
                        rrecord += r + "^";
                        System.err.println(rrecord);
                    }
                    RRecords.add(rrecord);
                }
                if ( Line.hamada(line, 9, 14).toUpperCase().equals("CSECT")) {
                    LOCCTR.setLocation(0);
                    consec ++;
                    
                    symtables.add(symbolTable);
                    SymbolTable newsym = new SymbolTable();
                    symtables.add(newsym);
                    symbolTable = symtables.get(consec - 1);
                    
                }
                if (Line.hamada(line, 9, 14).toUpperCase().equals("LTORG")) {
                    addLit();
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
                if (Line.hamada(line, 9, 14).toUpperCase().equals("ORG")) {
                    String operands_str = Line.hamada(line, 16, line.length() - 1);
                    if (operands_str.length() > 0) {
                        String[] ttt = operands_str.split("[-+*/]");
                        int A = 0, R = 0;
                        for (String s : ttt) {
                            if (symbolTable.isLabel(s)) {
                                operands_str = operands_str.replaceAll(s, Integer.toString(symbolTable.getEntry(s).getDecimalValue()));
                                if (symbolTable.getEntry(s).getFlag() == 'A') {
                                    A++;
                                } else {
                                    R++;
                                }
                            } else if (isDecimal(s)) {
                                //do nothing
                            } else {
                                System.out.println("ERROR: symbol not found");
                                genPass2 = false;
                            }
                        }
                        try {
                            // System.err.println("hena elmoshkella" + operands_str);
<<<<<<< HEAD
                            String x;
                            x = (new ScriptEngineManager().getEngineByName("JavaScript").eval(operands_str)).toString();
                            x = x.substring(0, x.length()-2);
                            // x = x.substring(0, x.length() - 2);
=======
                            String x = (new ScriptEngineManager().getEngineByName("JavaScript").eval(operands_str)).toString();
                             x = x.substring(0, x.length() - 2);
>>>>>>> refs/remotes/origin/cathy
                            prev_locctr = LOCCTR.getLocation();
                            org_enable = true;
                            LOCCTR.setLocation(x);
                        } catch (ScriptException ex) {
                            Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //LOCCTR.setLocation(operands_str);
                    } else if (org_enable) {
                        // System.out.println("La2");
                        org_enable = false;
                        LOCCTR.setLocation(prev_locctr);
                    } else {
                        System.out.println("Error wallahy");
                    }
                }
                if (!cur_line.isValid()) {
                    genPass2 = false;
                }

                // Update symbol table
                if (cur_line.getLabel() != null) {
                    // is symbol (constant) ->
                    // case of EQU, ORG
                    if (cur_line.getIsDirective() && Line.hamada(line, 9, 14).toUpperCase().equals("EQU")) {
                        String operands_str = Line.hamada(line, 16, line.length() - 1);
                        if (operands_str.equals("[*]")) {
                            symbolTable.addSymbol(cur_line.getLabel(), decToHex(LOCCTR.getLocation(), 6), 'R');
                        } else {
                            String[] ttt = operands_str.split("[-+*/]");
                            int A = 0, R = 0;
                            for (String s : ttt) {
                                if (symbolTable.isLabel(s)) {
                                    operands_str = operands_str.replaceAll(s, Integer.toString(symbolTable.getEntry(s).getDecimalValue()));
                                    if (symbolTable.getEntry(s).getFlag() == 'A') {
                                        A++;
                                    } else {
                                        R++;
                                    }
                                } else if (isDecimal(s)) {
                                    //do nothing
                                } else {
                                    System.out.println("ERROR: symbol not found");
                                    genPass2 = false;
                                }
                            }
                            try {
                                //System.err.println("hena elmoshkella" + operands_str);
                                String x = (new ScriptEngineManager().getEngineByName("JavaScript").eval(operands_str)).toString();
                                x = x.substring(0,x.length()-2);
                                symbolTable.addSymbol(cur_line.getLabel(), decToHex(Integer.parseInt(x), 6), R > A ? 'R' : 'A');
                            } catch (ScriptException ex) {
                                Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        // is label -> address
                        symbolTable.addSymbol(cur_line.getLabel(), decToHex(LOCCTR.getLocation(), 6), 'A');
                    }
                    symW.write(symbolTable.getEntry(cur_line.getLabel()).toString());
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
        return genPass2;
    }

    public void validateLabelswKeda() {
        for (Line curLine : linesOfCode) {
            if (!curLine.validateOperands(this.symbolTable)) {
                //  this.symbolTable.removeSymbol(curLine.getLabel());
            } else if (curLine.getLabel() != null) {

            }
        }
    }

    public void addLit() {

        int i;
        for (i = 0; i < literals.size(); i++) {
            if (literals.get(i).isMark() == false) {
                Lit l = literals.get(i);
                String s = l.getValue();
                Line line = new Line(s, LOCCTR, this.symbolTable, 0);

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
            return hex2dec(symbolTable.getEntry(str).getValue());
        }

        return 0;
    }

    public void pass2(String asmFileName, String outputSrcFileName, SymbolTable symbolTable) {

        consec = 1;
        System.out.println("\n\n~~~~~~~PASS 2~~~~~~~~\n\n");
        int i = 0;
        while (i < linesOfCode.size()) {

            Line line = linesOfCode.get(i);
            
            if ( line.getIsDirective() == true && line.dir.getName().toUpperCase().equals("CSECT")) {
                consec++;
            }
            System.err.println("control section " + consec);
            symbolTable = symtables.get(consec-1);
            if (!line.validateOperands(symbolTable)) {
                System.out.println(line.toString());
                break;
            }
            
            if ( line.getIsDirective() == true && line.dir.getName().toUpperCase().equals("EXTDEF")) {
                String D="D^";
                
                D = "D^";
            for(String d : definitions){
                if(symbolTable.isLabel(d))
                {
                System.err.println(d+ "hena" + symbolTable.getEntry(d).getValue());
                D =D+ d +"^"+ symbolTable.getEntry(d).getValue() +"^";
                }
            }
                DRecords.add(D);
                
                
            }
            
            
            if (baseCounter < baseLines.size() && line.getLine_no() == baseLines.get(baseCounter).getLine_no()) {
                //System.out.println("test");
                enableBase = true;
                base = getBaseValue(baseCounter);
                System.out.println("base is " + base);
                baseCounter++;
            }

            ArrayList<Operand> op = new ArrayList<>();
            if (line.getIsError() == false && line.getIsComment() == false && line.getInstr() != null && line.getIsDirective() == false && line.getInstr().getOperands().size() > 0) {
                op = line.getInstr().getOperands();
                if (op.get(0).getType() == 'l' || op.get(0).getType() == 'v') {
                    //String l = op.get(0).getName();
                    // System.out.print(" operand "+l);
                    String n = op.get(0).getCode(symbolTable, line.getInstr().getFormat());
                    if (n == null) {
                        System.out.println(String.format("BLAME THIS: %s %s", line.getInstr().getMnemonic(), op.get(0).getName()));
                    }
                    //           System.out.println(" location "+n);
                    int TA = hex2dec(n);
                    //System.out.println("target address  "+TA);
                    int current = hex2dec(linesOfCode.get(i + 1).getAddress());
                    // System.out.print("current " + current);
                    target = TA - current;
                    //System.out.print("test target" +target);
                    if (line.getInstr().getFormat() != 4) {
                        setBP(target);
                    }
                    //System.out.print("test");

                    //if(line.getInstr().getI() == true && line.getInstr().getN() == false)
                    //  target = TA;
                    if (b == false && p == false) {
                        target = TA;
                    } else if (b == true && p == false) {
                        target = TA - base;
                        String x = Integer.toBinaryString(target);
                        //System.out.println( x + "    target " + target + "TA " + TA + "base " + base);

                    } else if (b == false && p == true) {
                        target = TA - current;
                    }
                    if (target < 0) {
                        target = 4096 + target;
                    }
                    if (line.getInstr().getFormat() != 4 && target > 4095 && (target < 0 && b == true)) {
                        System.out.println("error target base out of bounds");
                        System.exit(0);
                    }
                    //String t = Integer.toBinaryString(target);
                    //System.out.println( t + "    target " + target + "TA " + TA + "current " + current);

                }
            }

            String s = line.getObjectCode(symbolTable).toUpperCase();
            line.text = s;
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
            // System.out.println("pcrelatve1");
            if (target > 2048 || target < -2047) {
                System.out.println("error no base allowed");
                System.exit(0);
            }
            b = false;
            p = true;
            //continue;
        } else if (target < 2048 && target > -2048) {
            //  System.out.println("pcrelatve2");
            b = false;
            p = true;
        } else /*if (target < 4096)*/ {
            //System.out.println("baserelative");
            b = true;
            p = false;
        }
    }

    public ArrayList<String> mod() {

        int i = 0;
        ArrayList<String> mods = new ArrayList<>();
        while (i < linesOfCode.size()) {

            Line line = linesOfCode.get(i);
            if (line.getInstr() != null && line.getInstr().getFormat() == 4 && line.getInstr().getMnemonic().charAt(0) != '#' && !"RSUB".equals(line.getInstr().getMnemonic())) {

                String r = "M^";
                String x = line.getInstr().getOperands().get(0).getName();
                //System.err.println("modify " +x);
                int loc = hex2dec(line.getAddress()) + 1;
                String start = decToHex(loc, 6);
                r += start;
                r += "^05";
                for(String s : references)
                {
                    if (x.equalsIgnoreCase(s))
                    {System.err.println("hena1");
                        r +="^+" + s;
                        
                    }
                }
                mods.add(r.toUpperCase());

            }
            i++;

        }
        return mods;
    }

    public String newText(int i) {
        String r = "T";
        while (i < linesOfCode.size()) {
            //System.out.println("dsfs");
            String t = "";
            while (t.length() <= 60) {
                if (" ".equals(linesOfCode.get(i).getObjectCode(symbolTable))) {
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

    public ArrayList<String> text() {
        //  System.out.println("dsfs");
        int i = 0;
        ArrayList<String> texts = new ArrayList<>();
        while (i < linesOfCode.size()) {
            //System.out.println("dsfs");
            String t = "";
            int st = i;
            int n = 0;
            int length;
            while (i < linesOfCode.size() && (t.length() + linesOfCode.get(i).text.length()) < 60 + n && !" ".equals(linesOfCode.get(i).text)/* && !(linesOfCode.get(i).getIsDirective() == true && (!linesOfCode.get(i).getDir().getName().equalsIgnoreCase("BASE") || !linesOfCode.get(i).getDir().getName().equalsIgnoreCase("NOBASE")))*/) {

                t += linesOfCode.get(i).text + "^";
                n++;
                i++;
            }
            length = t.length() - n;
            while (i < linesOfCode.size() && " ".equals(linesOfCode.get(i).text)) {
                i++;
            }
            // i=linesOfCode.get(i).getLine_no();
            String r = "T^" + linesOfCode.get(st).getAddress().toUpperCase() + "^" + decToHex(length / 2, 2) + "^" + t.toUpperCase();
            //System.out.println(r);
            r = r.substring(0, r.length() - 1).toUpperCase();
            texts.add(r);
            // i++;

        }

        return texts;
    }

    public String head() {
        int end = hex2dec(linesOfCode.get(linesOfCode.size() - 1).getAddress());
        int start = hex2dec(linesOfCode.get(0).getAddress());
        int length = end - start;
        String h = "H^";
        h += linesOfCode.get(0).getLabel() + "^" + linesOfCode.get(0).getAddress() + "^" + decToHex(length, 6);

        return h.toUpperCase();
    }

    public String end() {
        String e = "E^";
        e += linesOfCode.get(0).getAddress().toUpperCase();

        return e;
    }

    public static void main(String[] args) throws IOException {

        String asmFileName = "control_section";
        String srcCodeFileName = "src-prog-" + asmFileName;
        Assembler assembler = new Assembler();
        Boolean pass1result = assembler.pass1(asmFileName, srcCodeFileName);
        for(String r: references)
        {
            System.err.println("ref "+r);
        }
        //ToDo: nzabat mkanha fein
        if (enableBase == true) {
            base = getBaseValue(baseCounter);
        }
        int i = 0;
        for (i = 0; i < literals.size(); i++) {
            System.out.println(literals.get(i).toString());

        }
        //System.out.print(base);
        if (pass1result) {
            assembler.pass2(asmFileName, srcCodeFileName, symbolTable);
                     }
            consec = 1;
            symbolTable = symtables.get(consec-1);
            String D="";
            
            System.err.println(D);
            ArrayList<String> modRecords;
            i=0;
            while (i < DRecords.size()) {
                System.out.println(DRecords.get(i));
                i++;
            }
            modRecords = assembler.mod();
            i=0;
            while (i < modRecords.size()) {
                System.out.println(modRecords.get(i));
                i++;
            }
            String head = assembler.head();
            System.out.println(head);
            String end = assembler.end();
            System.out.println(end);

            ArrayList<String> textRecords;

            textRecords = assembler.text();
            i = 0;
            while (i < RRecords.size()) {
                System.out.println(RRecords.get(i));
                i++;
            }
            i=0;
            while (i < textRecords.size()) {
                System.out.println(textRecords.get(i));
                i++;
            }
            FileWriter htme = new FileWriter("HTME.txt");
            htme.write(head + "\r\n");
            i = 0;
            while (i < textRecords.size()) {
                htme.write(textRecords.get(i) + "\r\n");
                i++;
            }
            i = 0;
            while (i < modRecords.size()) {
                htme.write(modRecords.get(i) + "\r\n");
                i++;
            }
            htme.write(end + "\r\n");

            htme.close();
            // Assembler start = new Assembler();
            // System.out.println(String.format("%3d   %6s   %8s   %6s   %18s   %31s", 1, "0003A0", "TERMPROJ", "START", "3A0", ""));
        }
    
    Boolean isDecimal(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
