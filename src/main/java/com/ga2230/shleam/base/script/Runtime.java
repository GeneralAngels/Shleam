/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.base.script;

import com.ga2230.shleam.base.structure.Function;
import com.ga2230.shleam.base.structure.Module;
import com.ga2230.shleam.base.structure.Result;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is used to run Shleam scripts.
 */
public class Runtime extends Module {

    // Runtime constants
    private static final String CALL_SEPARATOR = "\n";
    private static final String LIST_SEPARATOR = ", ";
    private static final String ARGUMENT_SEPARATOR = " ";
    private static final String COMMENT_INDICATOR = "//";

    // Dictionary names
    private static final String DICTIONARY_STATE = "state";
    private static final String DICTIONARY_ERROR = "error";
    private static final String DICTIONARY_CURRENT_BLOCK = "current_block";
    private static final String DICTIONARY_CURRENT_ASYNC = "current_async";

    // Call queue
    private ArrayList<String> awaitingQueue = new ArrayList<>();
    private ArrayList<String> asyncQueue = new ArrayList<>();
    private ArrayList<String> finishedQueue = new ArrayList<>();

    // Parent module for running calls on.
    private Module parent;

    /**
     * Default constructor, with parent.
     *
     * @param parent Parent module
     */
    public Runtime(Module parent) {
        super("runtime");
        // Set parent
        this.parent = parent;
        // Register functions
        register("load", new Function() {
            @Override
            public Result execute(String parameter) throws Exception {
                // Clear queues
                awaitingQueue.clear();
                asyncQueue.clear();
                finishedQueue.clear();
                // Parse script
                String[] functionCalls = parameter.split(CALL_SEPARATOR);
                // Add all function calls to the queue
                awaitingQueue.addAll(Arrays.asList(functionCalls));
                // Update the module's state
                set(DICTIONARY_STATE, "loaded");
                // Return a successful result
                return Result.finished("Script loaded");
            }
        });
    }

    /**
     * Scans the queues once more and executes the functions.
     */
    public void next() {
        try {
            // Make sure the awaiting queue is not empty yet
            if (awaitingQueue.size() > 0) {
                // Read the first call in the queue
                String currentCall = awaitingQueue.get(0);
                // Make sure the current call is not an empty line or a comment line
                if (currentCall.length() > 0 && !currentCall.startsWith(COMMENT_INDICATOR)) {
                    // Determine type
                    boolean isAsync = currentCall.startsWith("a");
                    // Execute and move queues
                    if (isAsync) {
                        // Move to the async queue
                        asyncQueue.add(awaitingQueue.remove(0));
                    } else {
                        // Update the current blocking function (for telemetry)
                        set(DICTIONARY_CURRENT_BLOCK, currentCall);
                        // Execute the call and check the result
                        Result result = perform(currentCall);
                        // Check if the function is finished
                        if (result.isFinished())
                            // Move queues
                            finishedQueue.add(awaitingQueue.remove(0));
                    }
                } else {
                    awaitingQueue.remove(0);
                }
            }

            // Make sure the async queue is not empty yet
            if (asyncQueue.size() > 0) {
                // Execute the functions
                for (int callIndex = 0; callIndex < asyncQueue.size(); callIndex++) {
                    // Execute the call and check the result
                    Result result = perform(asyncQueue.get(callIndex));
                    // Check if the function is finished
                    if (result.isFinished())
                        // Move queues
                        finishedQueue.add(asyncQueue.remove(callIndex));
                }
                // Create a list of running async functions
                StringBuilder listBuilder = new StringBuilder();
                for (int callIndex = 0; callIndex < asyncQueue.size(); callIndex++) {
                    if (listBuilder.length() > 0)
                        listBuilder.append(LIST_SEPARATOR);
                    listBuilder.append(asyncQueue.get(callIndex));
                }
                // Update the current async functions (for telemetry)
                set(DICTIONARY_CURRENT_ASYNC, listBuilder.toString());
            }
            // Update the module's state
            set(DICTIONARY_STATE, asyncQueue.size() == 0 && awaitingQueue.size() == 0 ? "finished" : "not-finished");
        } catch (Exception thrownException) {
            // Update the error (for telemetry)
            set(DICTIONARY_ERROR, thrownException.toString());
        }
    }

    /**
     * Executes a function call and returns the result.
     *
     * @param call Function call
     * @return Result object
     */
    private Result perform(String call) throws Exception {
        /*
            Example for a function call:
            a master print Hello World
            ^ Call type
              ^^^^^^ Module ID
                     ^^^^^ Function call
                           ^^^^^^^^^^^ Parameter
         */
        String[] callType = call.split(ARGUMENT_SEPARATOR, 2);
        String[] moduleID = callType[1].split(ARGUMENT_SEPARATOR, 2);
        String[] functionCall = moduleID[1].split(ARGUMENT_SEPARATOR, 2);
        // Find module
        Module module = this.parent.descendant(moduleID[0]);
        // Make sure module exists
        if (module != null) {
            // Execute the call
            Result result = module.execute(functionCall[0], functionCall.length > 1 ? functionCall[1] : null);
            // Do something
            // Return result
            return result;
        } else {
            // Fallback "finished" for a failed find.
            return Result.finished("Module not found");
        }
    }

    /**
     * Enqueues a function call.
     *
     * @param call Function call
     */
    public void enqueue(String call) {
        // Add call to the awaiting queue
        awaitingQueue.add(call);
    }
}
