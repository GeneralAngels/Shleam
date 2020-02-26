package com.ga2230.shleam.advanced.frc;

import com.ga2230.shleam.Shleam;
import com.ga2230.shleam.base.script.Runtime;

public class FRCRobot extends FRCModule {

    protected Runtime autonomous;

    public FRCRobot(String id) {
        super("robot");
        // Initialize Shleam
        Shleam.begin(5800, this);
        // Initialize autonomous
        autonomous = new Runtime(this);
    }

    public void autonomousSetup() {

    }

    public void autonomousLoop() {

    }

    public void teleopSetup() {

    }

    public void teleopLoop() {

    }
}
