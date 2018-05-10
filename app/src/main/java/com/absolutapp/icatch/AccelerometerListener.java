package com.absolutapp.icatch;

/**
 * Created by Joakim on 2018-05-09.
 */

public interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}
