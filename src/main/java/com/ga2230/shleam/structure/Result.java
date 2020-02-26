/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.structure;

/**
 * This class is used to represent a return state of a called function.
 */
public class Result {

    private boolean finished = false;
    private String result = "Default result";

    /**
     * This is the default constructor.
     *
     * @param finished Is the function finished
     * @param result   Function result / Error message
     */
    private Result(boolean finished, String result) {
        this.finished = finished;
        this.result = result;
    }

    /**
     * External constructor for a finished function.
     *
     * @param result Function result
     * @return Constructed result
     */
    public static Result finished(String result) {
        return new Result(true, result);
    }

    /**
     * External constructor for an ongoing function / failed function.
     *
     * @param result Error message
     * @return Constructed result
     */
    public static Result notFinished(String result) {
        return new Result(false, result);
    }

    /**
     * Pass-through constructor for a result.
     *
     * @param finished Finished state
     * @param result   Result / Error message
     * @return Constructed result
     */
    public static Result create(boolean finished, String result) {
        return new Result(finished, result);
    }

    /**
     * Getter for finished.
     *
     * @return The finish state
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Getter for result.
     *
     * @return The result
     */
    public String getResult() {
        return result;
    }
}
