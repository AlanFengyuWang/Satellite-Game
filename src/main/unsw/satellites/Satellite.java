package unsw.satellites;

import unsw.Entity.Entity;
import unsw.utils.Angle;

import java.util.*;

public abstract class Satellite extends Entity{
    // protected String satelliteId;
    protected double linear_velocity;
    protected double num_files_storage;
    protected int byte_storage;
    protected int send_rate;
    protected int receive_rate;
    protected String satellite_type;
    protected double angular_velocity;
    protected boolean is_move_up;

    ArrayList<String> supported_devices = new ArrayList<String>();

  //methods
  public boolean bandwidth_is_full() {
    //i.e. if you have a bandwidth of 8 bytes per minute and are currently uploading/download 
    //8 files, you cannot upload/download any more files.
    if(get_num_of_IsUploading_not_downloading() > send_rate || get_num_of_NotUploading_is_downloading() > receive_rate) return true;
    return false;
  }

  public boolean tooManyFiles() {
    return getNum_files() > num_files_storage ? true : false;
  }

  public boolean notEnoughStorage() {
    return getTotalStorage() > byte_storage ? true : false;
  }

  //gettter and setters
  public double getNum_files_storage() {
		return this.num_files_storage;
	}

	public double getFile_content_size_storage() {
		return this.byte_storage;
	}

  public Double getAngularVelocity() {
    angular_velocity = linear_velocity / height;
    return angular_velocity;
  }

  public Angle moveStatellite(double minutes) {
    angular_velocity = linear_velocity / height;
    double angel = angular_velocity * minutes;
    this.position = this.position.add(Angle.fromRadians(angel));
    return this.position; 
  }

  public Angle moveStatellite_down(double minutes) {
    angular_velocity = linear_velocity / height;
    double angel = angular_velocity * minutes;
    this.position = this.position.add(Angle.fromRadians(angel));
    return this.position; 
  }

  public Angle moveStatellite_up(double minutes) {
    angular_velocity = linear_velocity / height;
    double angel = angular_velocity * minutes;
    this.position = this.position.subtract(Angle.fromRadians(angel));
    return this.position; 
  }

  public void set_is_move_up(boolean is_move_up) {
    this.is_move_up = is_move_up;
  }

  public boolean get_is_move_up() {
    return is_move_up;
  }

  public ArrayList<String> getSupportingDevices() {
    return supported_devices;
  }

  public String getSatellite_type() {
		return this.satellite_type;
	}

  public double getLinear_velocity() {
		return this.linear_velocity;
	}

	public int getByte_storage() {
		return this.byte_storage;
	}

	public int getSend_rate() {
		return this.send_rate;
	}

	public int getReceive_rate() {
		return this.receive_rate;
	}

}
