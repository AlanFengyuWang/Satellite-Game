package unsw.satellites;

import unsw.files.*;
import unsw.utils.Angle;

public class ShrinkingSatellite extends Satellite{

    //Constructor
    public ShrinkingSatellite(String satelliteId, double height, Angle position) {
        super();
        this.id = satelliteId;
        this.height = height;
        this.position = position;

		entity_type = "ShrinkingSatellite";
		linear_velocity = 1000.0;
		max_range = 2000000;
		num_files_storage = Double.POSITIVE_INFINITY;
		byte_storage = 150;
		send_rate = 10; // byte per min
		receive_rate = 15; // byte per min

		supported_devices.add("LaptopDevice");
		supported_devices.add("HandheldDevice");
		supported_devices.add("DesktopDevice");
		
    }

	//methos
	public boolean has_quantum(File f) {
        if(f.getContent().contains("quantum")) return true;
        return false;
    }


    //Getters and setters

}
