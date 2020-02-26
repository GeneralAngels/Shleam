/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam;

import com.ga2230.shleam.communication.Server;
import com.ga2230.shleam.structure.Module;

/**
 * This is the launcher for the Shleam framework.
 */
public abstract class Shleam {

    /**
     * Start the Shleam server.
     *
     * @param port   Server port
     * @param parent Parent module
     */
    public static void begin(int port, Module parent) {
        Server.begin(port, parent);
    }

}
