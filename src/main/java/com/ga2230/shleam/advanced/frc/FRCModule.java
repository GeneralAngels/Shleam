package com.ga2230.shleam.advanced.frc;

import com.ga2230.shleam.base.structure.Function;
import com.ga2230.shleam.base.structure.Module;
import com.ga2230.shleam.base.structure.Result;

public class FRCModule extends Module {

    private long timeOffset = 0;

    public FRCModule(String id) {
        super(id);

        // Set the time offset
        this.timeOffset = millis();

        // Register a special sleep command for async autonomous
        register("sleep", new Function() {

            private boolean sleeping = false;
            private long targetTime = 0;

            @Override
            public Result execute(String parameter) throws Exception {
                if (!sleeping) {
                    long time = Integer.parseInt(parameter);
                    // Set target time
                    targetTime = millis() + time;
                    // Sleeping switch
                    sleeping = true;
                } else {
                    if (millis() > targetTime) {
                        sleeping = false;
                        // Return done
                        return Result.finished("Done");
                    }
                }
                return Result.notFinished("Sleeping");
            }
        });

        // Add the timer interface for delta calculations
        register("timer", new Function() {

            private long offset = 0;

            @Override
            public Result execute(String parameter) throws Exception {
                if (parameter.equals("reset")) {
                    // Reset the offset
                    offset = millis();
                    // Return an OK
                    return Result.finished("Timer reset");
                } else {
                    // Calculate delta
                    long delta = millis() - offset;
                    // Create result
                    String result = delta + "ms, " + (delta / 1000) + "s.";
                    // Log result
                    log("Timer result: " + result);
                    // Return result
                    return Result.finished(result);
                }
            }
        });
    }

    /**
     * Simple log to output.
     *
     * @param string Message
     */
    protected void log(String string) {
        System.out.println(getID().toUpperCase() + ": " + string);
    }

    /**
     * Returns the current system time in millis.
     *
     * @return System time.
     */
    protected long millis() {
        return System.currentTimeMillis() - this.timeOffset;
    }

}
