package com.absolutapp.icatch;

import android.location.Location;

public class ArrowCalculator {

    private Location goal;
    private GPS gps;

    public ArrowCalculator(GPS gps, Location goal){
        this.goal = goal;
        this.gps = gps;
    }


    public float getDirection(){
        Location me = gps.getCurrentBest();
        if(me == null){
            return 0;
        }
        //double lat = me.getLatitude() - goal.getLatitude();
        //double longi = me.getLongitude() - goal.getLongitude();

        return me.bearingTo(goal);
    }

    public void setGoal(Location goal){
        this.goal = goal;
    }

    public float getDistance(){
        Location me = gps.getCurrentBest();
        if(me == null){
            return Float.MAX_VALUE;
        }
        return    me.distanceTo(goal);
    }


    public Location getMyLocation() {
        return gps.getCurrentBest();
    }
}


