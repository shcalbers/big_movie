package com.nhlstenden.bmdb.rcaller;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;

import javafx.scene.image.Image;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RManager {

    static {
        try {
            executeRScript("init");
            new File("./plots/").mkdir();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private RManager() {}

    public static Image getRPlot(String file_name) {
        try {
            return tryGetRPlot(file_name);
        } catch(IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.toString());
            return null;
        }
    }

    private static Image tryGetRPlot(String file_name) throws IOException {
        executeRScript(file_name);
        return new Image("file:./plots/" + file_name + ".png");
    }

    private static void executeRScript(String file_name) throws IOException {
        RCaller caller = RCaller.create();
        caller.setRCode(loadRScript(file_name));
        caller.runOnly();

        return;
    }

    private static RCode loadRScript(String file_name) throws IOException {
        RCode code = RCode.create();

        try (InputStream stream = RManager.class.getResourceAsStream("/r/" + file_name + ".R");
             InputStreamReader streamReader = new InputStreamReader(stream);
             BufferedReader reader = new BufferedReader(streamReader)) {

            for (String line; (line = reader.readLine()) != null; )
                code.addRCode(line);

        } catch (Exception e) {
            throw new IOException("Failed to load R Script: " + file_name + ".R", e);
        }

        return code;
    }

}