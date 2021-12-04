package unsw.devices;

import unsw.utils.Angle;

import java.util.*;

public class HandheldDevice extends Device{
    public HandheldDevice(String deviceId, Angle position) {
        super();
        this.id = deviceId;
        this.position = position;
        this.max_range = 50000;
        entity_type = "HandheldDevice";

        supported_devices.add("phone");
        supported_devices.add("GPS devices");
        supported_devices.add("tablets");
    }

    //getters and setters

}
