package unsw.devices;

import unsw.utils.Angle;

import java.util.*;


public class DesktopDevice extends Device{
    public DesktopDevice(String deviceId, Angle position) {
        super();
        this.id = deviceId;
        this.position = position;
        this.max_range = 200000;

        entity_type = "DesktopDevice";
        supported_devices.add("desktop computers");
        supported_devices.add("servers");
    }


}
