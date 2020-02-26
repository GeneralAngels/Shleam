/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.base.communication;

import com.ga2230.shleam.base.structure.Module;
import com.ga2230.shleam.base.structure.Result;
import com.ga2230.shleam.base.utils.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

/**
 * This is used to handle incoming clients and serve them content.
 */
class Client {

    // Log tag
    private static final String TAG = "Client";

    // Client constants
    private static final String ARGUMENT_SEPARATOR = " ";
    private static final String OUTPUT_SEPARATOR = ":";
    private static final String BASE64_SEPARATOR = "base64:";

    // Alive state
    private boolean listening = true;

    // Socket I/O
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * Default constructor.
     * @param socket Network socket
     * @param parent Module
     */
    public Client(Socket socket, Module parent) {
        // Setup I/O
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            this.reader = null;
            this.writer = null;
        } finally {
            // Make sure we are connected to a good client
            if (this.reader != null && this.writer != null) {
                // Start a new thread
                new Thread(() -> {
                    try {
                        // Begin listening for function calls
                        while (this.listening) {
                            // Make sure the reader is ready
                            if (this.reader.ready()) {
                                // Read the received line
                                String received = this.reader.readLine();
                                // Parse the information
                                String parsed = received;
                                // Check if the information is base64 encoded or not and decode it accordingly
                                boolean isBase64 = received.startsWith(BASE64_SEPARATOR);
                                // Decode base64
                                if (isBase64) {
                                    parsed = new String(Base64.getDecoder().decode(received.substring(BASE64_SEPARATOR.length())));
                                }
                                Result result = null;
                                // Try parsing even further
                                try {
                                    // Call parsing
                                    String[] moduleID = parsed.split(ARGUMENT_SEPARATOR, 2);
                                    String[] functionCall = moduleID[1].split(ARGUMENT_SEPARATOR, 2);
                                    // Module lookup
                                    Module module = parent.descendant(moduleID[0]);
                                    // Make sure the module exists
                                    if (module != null) {
                                        // Execute the call
                                        result = module.execute(functionCall[0], functionCall.length > 1 ? functionCall[1] : null);
                                    } else {
                                        result = Result.notFinished("Module not found");
                                    }
                                } catch (Exception thrownException) {
                                    // Create a failure result
                                    result = Result.notFinished(thrownException.toString());
                                }
                                if (result != null) {
                                    writer.write(String.valueOf(result.isFinished()));
                                    writer.write(OUTPUT_SEPARATOR);
                                    writer.write(isBase64 ? new String(Base64.getEncoder().encode(result.getResult().getBytes())) : result.getResult());
                                }
                                writer.newLine();
                                writer.flush();
                            }
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        Logger.log(TAG, e.toString());
                    }

                    try {
                        socket.close();
                    } catch (Exception e) {
                        Logger.log(TAG, e.toString());
                    }

                }).start();
            }
        }
    }
}
