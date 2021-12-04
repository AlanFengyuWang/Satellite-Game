package unsw.Entity;
import unsw.files.File;
import unsw.satellites.ShrinkingSatellite;
import unsw.utils.Angle;

import java.util.*;

public abstract class Entity {
    protected String entity_type_name;
    protected int file_size;
    protected String id;
    protected double height;
    protected Angle position;
    protected String entity_type;
    protected int max_range;

    ArrayList<File> files = new ArrayList<File>();

    //methods
    public int get_num_of_IsUploading_not_downloading() {
        int count = 0;
        for(File file : files) {
            if(file.is_uploading_not_downloading()) count++;
        }
        return count;
    }

    public int get_num_of_NotUploading_is_downloading() {
        int count = 0;
        for(File file : files) {
            if(!file.is_uploading_not_downloading()) count++;
        }
        return count;
    }

    public double getTotalStorage() {
        int totalStorage = 0;
        for(File file : files) 
            totalStorage += file.getSize();
        return totalStorage;
    }

    public void update_all_uploading_downloading_files(int minutes) {
        for(File f : files) {
            if(f.getUploaded_cotent().length() != f.getSize()) {
                if(entity_type == "ShrinkingSatellite") {
                    ShrinkingSatellite shrinkingSatellite = (ShrinkingSatellite) this;
                    if(shrinkingSatellite.has_quantum(f)) {
                        f.update_content(f, minutes, f.getBandwidth_rate(), true);
                    }
                }
                else
                    f.update_content(f, minutes, f.getBandwidth_rate(), false);
            }
        }
    }

    public void store_file(File f) {
        //when it contains quantum, it will only take up two-thirds of the original size to store
        if (entity_type == "ShrinkingSatellite") {
            ShrinkingSatellite shrinkingSatellite = (ShrinkingSatellite) this;
            if(shrinkingSatellite.has_quantum(f) && f.has_transfer_completed()){
                f.setSize(f.getSize() * 2/3);
            }
        }
        files.add(f);
    }

    //getters and setters
    public String getId() {
        return this.id;
    }

    public double getHeight() {
        return this.height;
    }

    public int getMax_range() {
		return this.max_range;
	}
      
    public Angle getposition() {
        return this.position;
    }

    public String getEntityType() {
		return this.entity_type;
	}

    public String getEntity_type_name() {
		return this.entity_type_name;
	}

	public void setEntity_type_name(String entity_type_name) {
		this.entity_type_name = entity_type_name;
	}

	public int getFile_size() {
		return this.file_size;
	}

    public int getNum_files() {
        return this.files.size();
    }

	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}

    public void addfile(File f) {
        files.add(f);
    }
    public ArrayList<File> getfiles() {
        return files;
    }
    
    public File get_file(String file_name) {
        for(File f : files) {
            if(f.getFile_name() == file_name) {
            return f;
            }
        }
        return null;
    }

    public void create_file_given_name(String file_name, String content) {
        File file = new File(file_name, content, true);
        this.addfile(file);
      }

}
