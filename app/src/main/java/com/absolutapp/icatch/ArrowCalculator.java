package com.absolutapp.icatch;

import android.location.Location;

public class ArrowCalculator {

    private Location goal;
    //private GPS gps;

    public ArrowCalculator(Location goal){
        this.goal = goal;
        //this.gps = gps;
    }


    public float getDirection(Location myLocation){

      //  Location me = gps.getCurrentBest();
        if(myLocation == null){
            return 0;
        }
        //double lat = me.getLatitude() - goal.getLatitude();
        //double longi = me.getLongitude() - goal.getLongitude();

        return myLocation.bearingTo(goal);
    }

    public void setGoal(Location goal){
        this.goal = goal;
    }

    public float getDistance(Location myLocation){
        //Location me = my.getCurrentBest();
        if(myLocation == null){
            return -1;//"No location";//Float.MAX_VALUE;
        }
        return  myLocation.distanceTo(goal);
    }

    public String getDistanceString(Location myLocation){
        float d = getDistance(myLocation);
        if(d<0){
            return "No location";
        }
        else{
            return Math.round(d) + " m";
        }
    }


    //public Location getMyLocation() {
    //    return gps.getCurrentBest();
    //}
}


