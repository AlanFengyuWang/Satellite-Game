package unsw.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.management.remote.TargetedNotification;

import unsw.satellites.RelaySatellite;
import unsw.satellites.Satellite;
import unsw.utils.MathsHelper;

public class Entities {
    ArrayList<Entity> entities = new ArrayList<Entity>();
    ArrayList<Entity> communicableRelaySatellites = new ArrayList<Entity>();
    
    //methods
    public List<Entity> is_reachable_between_entities_with_RelaySatellite_helper(String e1, String intermediator, String e2) {
        /**
         * Return the entity if e1 and e2 are reachable
         */
        if(this.is_reachable_between_entities_without_RelaySatellite_helper(intermediator, e2)) {
            return this.getCommunicableRelaySatellites();
        }

        ArrayList<Entity> ms = this.getEntities("RelaySatellite");
        for(Entity m1 : ms) {
            if(!e1.equals(m1.getId()) && this.is_reachable_between_entities_without_RelaySatellite_helper(e1, m1.getId())
            ) {
                return is_reachable_between_entities_with_RelaySatellite_helper(m1.getId(), e1, e2);
            }
        }
        //when they are not reachable
        return null;
    }

    public boolean is_reachable_between_entities_without_RelaySatellite_helper(String e1, String e2) {

        Entity entity1 = this.getEntity(e1, "Satellite");
        Entity entity2 = this.getEntity(e2, "Satellite");

        if((entity1 == null && entity2 == null) ||
        (e1 == "" || e2 == "")
        ) return false;

        if(entity1 == null) {
            entity1 = this.getEntity(e1, "Device");
            return (
                this.is_reachable_between_satellite_devive_without_RelaySatellite(entity2.getId(), entity1.getId())
            );
        }
        if(entity2 == null) {
            entity2 = this.getEntity(e2, "Device");
            return (
                this.is_reachable_between_satellite_devive_without_RelaySatellite(entity1.getId(), entity2.getId())
            );
        }
        return (
            this.is_reachable_between_satellites_without_RelaySatellite(entity1.getId(), entity2.getId())
        );
    }

    public boolean is_reachable_between_satellite_devive_without_RelaySatellite(String s1, String d1) {
        //get device and satellite
        Entity satellite = this.getEntity(s1, "Satellite");
        Entity device = this.getEntity(d1, "Device");

        boolean twoEntityReachable = 
            MathsHelper.isVisible(satellite.getHeight(), satellite.getposition(), device.getposition()) &&
            MathsHelper.getDistance(satellite.getHeight(), satellite.getposition(), 
            device.getHeight(), device.getposition()) <= 
            Math.min(device.getMax_range(), satellite.getMax_range());

        return twoEntityReachable;
    }

    public Entity is_reachable_between_satellite_device_with_RelaySatellite(String s1, String d1) {
        /**
         * Return the intermediate entity if the result is true, else return null
         */

        //get device and satellite
        Entity satellite = this.getEntity(s1, "Satellite");
        Entity device = this.getEntity(d1, "Device");

        //get intermediate satellites
        ArrayList<Entity> relaySatellites = getEntities("RelaySatellites");
        boolean withRelaySatelliteReachagble = false;
        Entity intermediator = null;
        for(Entity relaySatellite : relaySatellites) {
            if(!relaySatellite.getId().equals(satellite.getId()) &&
            !relaySatellite.getId().equals(device.getId())
            ) {
                withRelaySatelliteReachagble = 
                MathsHelper.isVisible(relaySatellite.getHeight(), relaySatellite.getposition(), 
                satellite.getHeight(), satellite.getposition()) &&
                MathsHelper.getDistance(relaySatellite.getHeight(), relaySatellite.getposition(), device.getposition()) <= 
                Math.min(device.getMax_range(), relaySatellite.getMax_range()) &&
                MathsHelper.getDistance(device.getHeight(), device.getposition(), 
                relaySatellite.getHeight(), relaySatellite.getposition()) <= 
                Math.min(satellite.getMax_range(), relaySatellite.getMax_range()); 

                if (withRelaySatelliteReachagble)
                    intermediator = relaySatellite; 
            }
        }
        return intermediator;  
    }

    public boolean is_reachable_between_satellites_without_RelaySatellite(String s1, String s2) {
        Entity fromSatellite = this.getEntity(s1, "Satellite");
        Entity toSatellite = this.getEntity(s2, "Satellite");
        boolean twoSatelliteReachable = 
            MathsHelper.isVisible(fromSatellite.getHeight(), fromSatellite.getposition(), 
            toSatellite.getHeight(), toSatellite.getposition()) &&
            MathsHelper.getDistance(fromSatellite.getHeight(), fromSatellite.getposition(), 
            toSatellite.getHeight(), toSatellite.getposition()) <= 
            Math.min(fromSatellite.getMax_range(), toSatellite.getMax_range());

        return twoSatelliteReachable;
    }

    public Entity is_reachable_between_satellites_with_RelaySatellite(String s1, String s2) {
        /**
         * Return the intermediate entity if the result is true, else return null
         */
        Entity fromSatellite = this.getEntity(s1, "Satellite");
        Entity toSatellite = this.getEntity(s2, "Satellite");

        //get intermediate satellites
        ArrayList<Entity> relaySatellites = getEntities("RelaySatellites");
        boolean withRelaySatelliteReachagble = false;
        Entity intermediator = null;
        for(Entity relaySatellite : relaySatellites) {
            if(!relaySatellite.getId().equals(toSatellite.getId()) &&
            !relaySatellite.getId().equals(fromSatellite.getId())
            ) {
                withRelaySatelliteReachagble = 
                MathsHelper.isVisible(relaySatellite.getHeight(), relaySatellite.getposition(), 
                toSatellite.getHeight(), toSatellite.getposition()) &&
                MathsHelper.getDistance(fromSatellite.getHeight(), fromSatellite.getposition(), 
                relaySatellite.getHeight(), relaySatellite.getposition()) <= 
                Math.min(fromSatellite.getMax_range(), relaySatellite.getMax_range()) &&
                MathsHelper.getDistance(fromSatellite.getHeight(), fromSatellite.getposition(), 
                relaySatellite.getHeight(), relaySatellite.getposition()) <= 
                Math.min(toSatellite.getMax_range(), relaySatellite.getMax_range()); 

                if (withRelaySatelliteReachagble)
                    intermediator = relaySatellite; 
            }
        }
        return intermediator;  
    }

    public List<String> listDeviceIds() {
        List<String> list_device_ids = new ArrayList<String>();
        for(Entity entity : entities) {
            if(entity.getEntityType().contains("Device")) {
                list_device_ids.add(entity.getId());
            }
        }
        return new ArrayList<String>(list_device_ids);
    }

    public List<String> listSatelliteIds() {
        ArrayList<String> satelite_ids = new ArrayList<String>();
        for(Entity entity : entities) {
            if(entity.getEntityType().contains("Satellite")) {
                satelite_ids.add(entity.getId());
            }
        }
        return new ArrayList<String>(satelite_ids);
    }

    //getters and setters
    public List<Entity> getCommunicableRelaySatellites() {
        return communicableRelaySatellites;
    }
    public void addEntity(Entity e) {
        entities.add(e);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Entity> getEntities(String entityType) {
        ArrayList<Entity> entities_output = new ArrayList<Entity>();
        for(Entity entity : entities) {
            if(entity.getEntityType().equals(entityType)) 
                entities_output.add(entity);
        }
        return entities_output;
    }

    public Entity getEntity(String entityID) {
        for(Entity entity : entities) {
            if(entity.getId().equals(entityID)) return entity;
        }
        return null;
    }

    public Entity getEntity(String entityID, String entityType) {
        for(Entity entity : entities) {
            if(entity.getId().equals(entityID) && entity.getEntityType().contains(entityType)) 
                return entity;
        }
        return null;
    }

    public void removeDevice(String deviceId) {
        List<Entity> toRemove = new ArrayList<Entity>();
        for(Entity entity : entities) {
            if(deviceId.equals(entity.getId()) && entity.getEntityType().contains("Device")) {
                toRemove.add(entity);
            }
        }
    }

    public void removeSatellite(String deviceId) {
        List<Entity> toRemove = new ArrayList<Entity>();
        for(Entity entity: entities) {
            toRemove.add(entity);
        }
        entities.removeAll(toRemove);
    }

}
