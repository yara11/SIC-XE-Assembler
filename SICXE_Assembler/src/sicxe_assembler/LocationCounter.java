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
    public void setLocation(int location) {
        this.current_loc = location;
    }
}
