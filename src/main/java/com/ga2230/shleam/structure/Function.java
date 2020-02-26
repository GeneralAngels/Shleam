/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.shleam.structure;

/**
 * This interface is used to define a registerable function.
 */
public interface Function {
    Result execute(String parameter) throws Exception;
}
