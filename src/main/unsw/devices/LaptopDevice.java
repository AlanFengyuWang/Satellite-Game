package unsw.devices;

import unsw.utils.Angle;

import java.util.*;

public class LaptopDevice extends Device{
    public LaptopDevice(String deviceId, Angle position) {
        super();
        this.id = deviceId;
        this.position = position;
        this.max_range = 100000;

        entity_type = "LaptopDevice";
        
        supported_devices.add("laptop computers");
    }

}
