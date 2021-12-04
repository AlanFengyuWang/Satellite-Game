package unsw.devices;

import unsw.Entity.Entity;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;
import java.util.*;

public abstract class Device extends Entity{
    protected String device_type;

    protected ArrayList<String> supported_devices = new ArrayList<String>();

    public Device() {
      height = RADIUS_OF_JUPITER;
    }

    //getters and setters
    public String getDevice_type() {
      return this.device_type;
    }
    
    public ArrayList<String> get_supported_devices() {
      return supported_devices;
    }

}
