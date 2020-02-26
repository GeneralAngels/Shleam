/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.base.utils;

/**
 * This is used to log errors, warnings, and additional information.
 */
public abstract class Logger {

    /**
     * Log a message.
     *
     * @param tag     Calling class' tag
     * @param message Message
     */
    public static void log(String tag, String message) {
        // Print to system output
        System.out.println(tag.toUpperCase() + ": " + message);
    }

}
