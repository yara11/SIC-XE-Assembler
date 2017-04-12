package sicxe_assembler;

public class LocationCounter {
    private static int current_loc;
    private static LocationCounter locationCounter = null;
    
    private LocationCounter(int initial) {
        current_loc = initial;
    }
    
    public static LocationCounter getInstance(int initial) {
        if(locationCounter == null) {
            locationCounter = new LocationCounter(initial);
        }
        return locationCounter;
    }
    
    public static int increment(int offset) {
        current_loc += offset;
        return current_loc;
    }
    
    public static int getLocation() {
        return current_loc;
    }
}
