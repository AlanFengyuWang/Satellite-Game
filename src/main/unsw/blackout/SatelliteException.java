package unsw.blackout;

public class SatelliteException extends Exception{
    public SatelliteException(String message) {
        super(message);
    }

    /**
     * Representing the caes where the satellite cannot be found 
     */
    public static class SatelliteNotFoundException extends SatelliteException{
        public SatelliteNotFoundException(String message) {
            super(message);
        }
    }

}   
