package de.undefinedhuman.projectcreate.server;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationListener;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class Main {

    public static void main(String[] args) {
        new HeadlessApplication(new HeadlessApplicationListener());
        Log.getInstance().setFileLocation(Files.FileType.Local);
        Log.getInstance().init();
        Log.getInstance().load();
        Log.info("SERVER STARTED!");
        Log.info("SERVER STOPPED!");
        int i = 500;
        while(i > 0) {
            try {
                Thread.sleep(1000);
                i--;
            } catch (InterruptedException e) {
                Log.error("Error in server thread", e);
                Thread.currentThread().interrupt();
            }
        }
        Log.getInstance().save();
        Gdx.app.exit();
        System.exit(0);
    }

}
