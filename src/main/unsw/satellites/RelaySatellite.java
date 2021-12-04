package unsw.satellites;
import unsw.utils.Angle;

public class RelaySatellite extends Satellite{
	public static final Integer INFINITY = 999999;

    //Constructor
    public RelaySatellite(String satelliteId, double height, Angle position) {
        super();
        this.id = satelliteId;
        this.height = height;
        this.position = position;

		entity_type = "RelaySatellite";
		num_files_storage = 0.0;
		byte_storage = 150;
		max_range = 300000;
		send_rate = INFINITY; // byte per min
		receive_rate = INFINITY; // byte per min
		linear_velocity = 1500.0;

		supported_devices.add("LaptopDevice");
		supported_devices.add("HandheldDevice");
		supported_devices.add("DesktopDevice");
    }

	@Override
	public Angle moveStatellite(double minutes) {
		//if the range is outside [140, 190], it will choose 
		//whatever direction gets it to the region [140°, 190°] in the shortest amount of time.
		if(position.compareTo(Angle.fromDegrees(190)) == 1 &&
		position.compareTo(Angle.fromDegrees(345)) == -1
		) {
			//move clockwise
			set_is_move_up(true);
			return moveStatellite_up(minutes);
		}
		else if(
			position.compareTo(Angle.fromDegrees(140)) == -1 &&
			(position.compareTo(Angle.fromDegrees(345)) == 1 ||
			position.compareTo(Angle.fromDegrees(345)) == 0)
		){
			//move anti-clockwise
			set_is_move_up(false);
			return moveStatellite_down(minutes);
		}
		//when it's in the range, it moves backward once it reached to the limit
		else {
			//move up when reach to the upper limit.
			if(position.compareTo(Angle.fromDegrees(190)) == 1 ||
			position.compareTo(Angle.fromDegrees(190)) == 0
			) {
				set_is_move_up(true);
				return moveStatellite_up(minutes);
			}
			//move down when reach to the lower limit.
			else if(position.compareTo(Angle.fromDegrees(140)) == 0 ||
			position.compareTo(Angle.fromDegrees(140)) == -1
			) {
				set_is_move_up(false);
				return moveStatellite_down(minutes);
			}
			//when it has not reached to the limit bound.
			else {
				if(get_is_move_up()) 
					return moveStatellite_up(minutes);
				else
					return moveStatellite_down(minutes);
			}
		}
	}
}
