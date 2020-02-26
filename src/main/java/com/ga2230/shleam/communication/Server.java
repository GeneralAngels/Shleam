/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.communication;

import com.ga2230.shleam.structure.Module;
import com.ga2230.shleam.utils.Logger;

import java.net.ServerSocket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * This is used to run the TCP/IP server for handling function calls.
 */
public abstract class Server {

    // Log tag
    private static final String TAG = "Server";

    // Alive state
    private static boolean listening = true;

    // Accepting socket
    private static ServerSocket server = null;

    // Clients array
    private static ArrayList<Client> clients = null;

    /**
     * Starts the Shleam server on the provided port with the provided parent as its module.
     *
     * @param port   Port
     * @param parent Module
     */
    public static void begin(int port, Module parent) {
        try {
            Server.clients = new ArrayList<>();
            Server.server = new ServerSocket(port);
            // Listen on new thread
            new Thread(() -> {
                while (Server.listening) {
                    try {
                        // Add client
                        Server.clients.add(new Client(Server.server.accept(), parent));
                        // Sleep for one second
                        sleep(1000);
                    } catch (Exception e) {
                        Logger.log(TAG, e.toString());
                    }
                }
            }).start();
        } catch (Exception e) {
            Logger.log(TAG, e.toString());
        }
    }


}
