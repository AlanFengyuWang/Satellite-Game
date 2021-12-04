package unsw.satellites;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite{
	//variables

	//Constructor
    public StandardSatellite(String satelliteId, double height, Angle position) {
        super();
        this.id = satelliteId;
        this.height = height;
        this.position = position;

		linear_velocity = 2500.0;
		max_range = 150000;
		num_files_storage = 3.0;
		byte_storage = 80;
		send_rate = 1; //1 byte per min
		receive_rate = 1; //1 byte per min
		entity_type = "StandardSatellite";

		supported_devices.add("HandheldDevice");
		supported_devices.add("LaptopDevice");
    }

	//methods
	@Override
	public boolean bandwidth_is_full() {
		//for standardSatellite, it can only transfer 1 file at a time 
		if(get_num_of_IsUploading_not_downloading() + get_num_of_NotUploading_is_downloading() > receive_rate) return true;
		return false;
	  }

	//Getters and setters

}
