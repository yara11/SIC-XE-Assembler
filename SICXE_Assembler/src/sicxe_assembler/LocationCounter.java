package sicxe_assembler;

public class LocationCounter {
    private int current_loc;
    
    public LocationCounter(int initial) {
        this.current_loc = initial;
    }
    
    public int increment(int offset) {
        this.current_loc += offset;
        return this.current_loc;
    }
    
    public int getLocation() {
        return this.current_loc;
    }
    
    // takes decimal integer
    public void setLocation(int location) {
        this.current_loc = location;
    }
    
    // takes hexadecimal string
    public void setLocation(String location) {
        this.current_loc = Integer.parseInt(location, 16);
    }
}
