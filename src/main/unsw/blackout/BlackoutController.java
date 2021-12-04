package unsw.blackout;

import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.swing.text.Position;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.satellites.*;
import unsw.Entity.Entities;
import unsw.Entity.Entity;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.SatelliteException.SatelliteNotFoundException;
import unsw.devices.*;
import unsw.files.File;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;


public class BlackoutController {
    // ArrayList<Satellite> satellites = new ArrayList<Satellite>();
    // ArrayList<Device> devices = new ArrayList<Device>();
    // ArrayList<Entity> entities = new ArrayList<Entity>();

    Entities entities = new Entities();

    public void createDevice(String deviceId, String type, Angle position) {
        Entity entity;
        switch(type) {
            case "HandheldDevice":
                var HandleDevice = new HandheldDevice(deviceId, position);
                entity = HandleDevice;
                break;
            case "LaptopDevice":
                var LaptopDevice = new LaptopDevice(deviceId, position);
                entity = LaptopDevice;
                break;
            case "DesktopDevice":
                var DesktopDevice = new DesktopDevice(deviceId, position);
                entity = DesktopDevice;
                break;
            default:
                throw new IllegalArgumentException(type);
        }
        // entities.add(entity);
        entities.addEntity(entity);
    }

    public void removeDevice(String deviceId) {
        entities.removeDevice(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Entity entity;
        switch(type) {
            case "StandardSatellite":
                var standard_satellite = new StandardSatellite(satelliteId, height, position);
                entity = standard_satellite;
                break;
            case "RelaySatellite":
                var RelaySatellite = new RelaySatellite(satelliteId, height, position);
                entity = RelaySatellite;
                break;
            case "ShrinkingSatellite":
                var ShrinkingSatellite = new ShrinkingSatellite(satelliteId, height, position);
                entity = ShrinkingSatellite;
                break;
            default:
                throw new IllegalArgumentException(type);
        }
        entities.addEntity(entity);
    }

    public void removeSatellite(String satelliteId) {
        entities.removeSatellite(satelliteId);
    }

    public List<String> listDeviceIds() {
        return entities.listDeviceIds();
    }

    public List<String> listSatelliteIds() {
        return entities.listSatelliteIds();
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        Entity entity = entities.getEntity(deviceId, "Device");
        entity.create_file_given_name(filename, content);
    }

    public EntityInfoResponse getInfo(String id) {
        // Entity entity = null;
        // for(Entity e : entities) {
        //     if(e.getId() == id) {
        //         entity = e;
        //     }
        // }
        Entity entity = entities.getEntity(id);
        if (entity == null) return null;

        EntityInfoResponse ent_info = null;
        if(!entity.getfiles().isEmpty()) {
            Map<String, FileInfoResponse> result = new HashMap<>();
            for(File f : entity.getfiles()) {
                result.put(f.getFile_name(), new FileInfoResponse(f.getFile_name(), f.getUploaded_cotent(), f.getSize(), f.has_transfer_completed()));
            }
            ent_info = new EntityInfoResponse(id, entity.getposition(), entity.getHeight(), entity.getEntityType(), result);
        }
        else {
            ent_info = new EntityInfoResponse(id, entity.getposition(), entity.getHeight(), entity.getEntityType());
        }
        return ent_info;
    }

    public void simulate() {
        //satellite moves by one numberogMinutes
        ArrayList<Entity> got_entities = entities.getEntities();
        for(Entity entity : got_entities) {
            if(entity.getEntityType().contains("Satellite")) {
                Satellite s = (Satellite) entity;
                s.moveStatellite(1);
            }
            entity.update_all_uploading_downloading_files(1);
        }


    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        List<String> communicableEntities = new ArrayList<String>();
        Entity entity_target = entities.getEntity(id);
        if (entity_target == null) return null;

        ArrayList<Entity> list_entities = entities.getEntities();
        for(Entity destination : list_entities) {
            if(!destination.getId().equals(id)) {
                if(entities.is_reachable_between_entities_without_RelaySatellite_helper(destination.getId(), id)) {
                    if(!communicableEntities.contains(destination.getId()))
                        System.out.println("destinatnion id = " + destination.getId());
                        communicableEntities.add(destination.getId());
                }
                else {
                    List<Entity> intermediators = entities.is_reachable_between_entities_with_RelaySatellite_helper(destination.getId(), "", entity_target.getId()); 
                    if(intermediators != null) {
                        for(Entity m : intermediators) {
                            if(!communicableEntities.contains(m.getId()))
                                communicableEntities.add(m.getId());
                        }
                        if(!communicableEntities.contains(destination.getId()))
                            communicableEntities.add(destination.getId());
                    }
                }
            }
        }
        return communicableEntities;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException, SatelliteNotFoundException, FileNotFoundException,
    VirtualFileNoStorageSpaceException, VirtualFileNoBandwidthException, VirtualFileNotFoundException {
        //check the entity
        Entity fromEntity = null;
        Entity toEntity = null;
        for(Entity entity : entities.getEntities()) {
            if(entity.getId().equals(fromId)) fromEntity = entity;
            if(entity.getId().equals(toId)) toEntity = entity;
        }
        if (fromEntity == null) throw new SatelliteNotFoundException(fileName);

        //check the satellite
        Satellite fromSatellite = null;
        Satellite toSatellite = null;
        if(fromEntity.getEntityType().contains("Satellite")) {
            fromSatellite = (Satellite)fromEntity;
        }
        if(toEntity.getEntityType().contains("Satellite")) {
            toSatellite = (Satellite)toEntity;
        }

        if(fromSatellite != null && fromSatellite.bandwidth_is_full()) throw new VirtualFileNoBandwidthException(fromId);
        if(toSatellite != null && toSatellite.bandwidth_is_full()) throw new VirtualFileNoBandwidthException(toId);

        //no room on the satellite
        if(toSatellite != null && toSatellite.tooManyFiles()) throw new VirtualFileNoStorageSpaceException(Double.toString(toSatellite.getNum_files_storage()));
        if(toSatellite != null && toSatellite.notEnoughStorage()) throw new VirtualFileNoStorageSpaceException(Double.toString(toSatellite.getFile_content_size_storage()));
        
        //check files  
        File f = null;
        ArrayList<File> files = toEntity.getfiles();
        for(File file : files) {
            if(file.getFile_name().equals(fileName)) {
                f = file;
            }
        }
        if (f != null) throw new VirtualFileAlreadyExistsException(fileName);   //when file already exists in toEntity.

        files = fromEntity.getfiles();

        File send_file = null;
        for(File file : files) {
            if(file.getFile_name().equals(fileName)) {
                int min_bandwidth_rate = -1;
                if(fromSatellite != null && toSatellite != null) {
                    min_bandwidth_rate = fromSatellite.getSend_rate() > toSatellite.getReceive_rate() ? toSatellite.getReceive_rate() : fromSatellite.getSend_rate();
                }
                else if(fromSatellite == null) {
                    min_bandwidth_rate = toSatellite.getReceive_rate();
                }
                else if(toSatellite == null) {
                    min_bandwidth_rate = fromSatellite.getSend_rate();
                }
                send_file = new File(file.getFile_name(), file.getContent(), false, min_bandwidth_rate);
                //start activate uploading the file
                send_file.setIs_uploading_not_downloading(true);
            }
        }
        if (send_file == null) throw new VirtualFileNotFoundException(fileName);   //when file does not exists in fromEntity.
        //send file
        toEntity.store_file(send_file);
    }
}
