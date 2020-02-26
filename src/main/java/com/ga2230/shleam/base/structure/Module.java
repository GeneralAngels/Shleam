/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.base.structure;

import com.ga2230.shleam.base.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class Module {

    // Internal dictionary
    protected HashMap<String, String> variables = new HashMap<>();

    // Internal variables
    private String id = "master";
    private ArrayList<Module> children = new ArrayList<>();
    private HashMap<String, Function> functions = new HashMap<>();

    /**
     * Default constructor, with ID.
     *
     * @param id Module ID
     */
    public Module(String id) {
        // Set the ID
        this.id = id;
        // Register functions
        // Lists all functions and all children
        register("help", new Function() {

            private static final String LINE_SEPARATOR = "\r\n";

            @Override
            public Result execute(String parameter) throws Exception {
                // Initiate a string builder
                StringBuilder stringBuilder = new StringBuilder();
                // Append some UI text
                stringBuilder.append("Showing help for module \"").append(getID()).append("\"").append(LINE_SEPARATOR);
                stringBuilder.append("Available functions:").append(LINE_SEPARATOR);
                // List functions
                Module.this.functions.forEach((s, command) -> {
                    stringBuilder.append(s).append(LINE_SEPARATOR);
                });
                // Append some UI text
                stringBuilder.append("Adopted children:").append(LINE_SEPARATOR);
                // List children
                for (Module module : children) {
                    stringBuilder.append(module.getID()).append("(").append(module.getClass().getSimpleName()).append(")").append(LINE_SEPARATOR);
                }
                // Return result
                return Result.finished(stringBuilder.toString());
            }
        });

        // Logs text to the system output
        register("log", new Function() {
            @Override
            public Result execute(String parameter) throws Exception {
                // Log with logger
                Logger.log(getID(), parameter);
                return Result.finished("Logged");
            }
        });
    }

    /**
     * ID getter.
     *
     * @return Module ID
     */
    protected String getID() {
        return this.id;
    }

    /**
     * Writes a value to the internal dictionary.
     *
     * @param name  Value name
     * @param value Value
     */
    protected void set(String name, String value) {
        // Set the variable
        this.variables.put(name, value);
    }

    /**
     * Searches through the internal dictionary and returns the value.
     *
     * @param name Value name
     * @return Value
     */
    protected String get(String name) {
        return get(name, null);
    }

    /**
     * Searches through the internal dictionary and returns the value or a fallback value.
     *
     * @param name     Value name
     * @param fallback Fallback value
     * @return Value
     */
    protected String get(String name, String fallback) {
        // Check if we have the value
        if (this.variables.containsKey(name))
            // Return the value
            return this.variables.get(name);
        // Return the fallback
        return fallback;
    }

    /**
     * Searches for a descendant with the given ID recursively.
     *
     * @param id Descendant ID
     * @return Descendant module
     */
    public Module descendant(String id) {
        // Make sure we are not that ID
        if (this.id.equalsIgnoreCase(id))
            return this;
        // Make sure the call is not for "master"
        if (this.id.equalsIgnoreCase("master"))
            return this;
        // Search through
        for (Module child : this.children) {
            // Search
            Module result = child.descendant(id);
            // Search succeeded?
            if (result != null)
                return result;
        }
        // Fallback null when not found
        return null;
    }

    /**
     * Executes a function by its function name and a parameter.
     *
     * @param functionName Function name
     * @param parameter    Parameter
     * @return Result object
     * @throws Exception Thrown exception
     */
    public Result execute(String functionName, String parameter) throws Exception {
        // Make sure it exists
        if (this.functions.containsKey(functionName)) {
            // Read the function
            Function function = this.functions.get(functionName);
            // Execute the function
            Result result = function.execute(parameter);
            // Do something maybe
            // Return the result
            return result;
        }
        // Fallback null
        return null;
    }

    /**
     * Registers a new child module.
     *
     * @param child Child module
     * @return Child module
     */
    protected Module adopt(Module child) {
        if (!this.children.contains(child))
            this.children.add(child);
        return child;
    }

    /**
     * Unregisters a child module.
     *
     * @param child Child module
     * @return Child module
     */
    protected Module abandon(Module child) {
        if (this.children.contains(child))
            this.children.remove(child);
        return child;
    }

    /**
     * Registers a new external function.
     *
     * @param functionName Function name
     * @param function     Function interface
     * @return Function interface
     */
    protected Function register(String functionName, Function function) {
        if (!this.functions.containsKey(functionName))
            this.functions.put(functionName, function);
        return function;
    }

    /**
     * Unregisters an external function.
     *
     * @param functionName Function name
     * @return Function interface
     */
    protected Function unregister(String functionName) {
        if (this.functions.containsKey(functionName))
            return this.functions.remove(functionName);
        return null;
    }
}
