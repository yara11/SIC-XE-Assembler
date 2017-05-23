/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxe_assembler;

import java.util.HashMap;

/**
 *
 * @author catherine
 */
public class Lit {
   private String value;
   private int length;
   private boolean mark = false;
   private boolean isHex = false;
   private String address;
   
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isIsHex() {
        return isHex;
    }

    public void setIsHex(boolean isHex) {
        this.isHex = isHex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getObject(){
        if(isHex)
            return value;
        else{
            String s = "";
            int i=0;
            for(i =0; i<length; i++){
                int c = (int)value.charAt(i);
                String x = Assembler.decToHex(c, 1);
                s += x;
            }
            return s;
        }
    }
    public String toString(){
        return "value " +value + "   address " + address + "    length " + length + "    marked " + String.valueOf(mark);
    }
}
