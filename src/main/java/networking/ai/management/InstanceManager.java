package networking.ai.management;

import ai.AI;
import networking.ai.instance.AIInstance;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by Sam Gunner on 29/03/2019.
 *
 * Manages an instance of the AI, with it's own subsystem instances
 */
public class InstanceManager {
    private Process process;

    public void startAIInstance() throws IOException {



        String cp = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        cp += ";";
        String[] parts = cp.split("/");
        String toAdd = "";
        for (int i = 0; i < parts.length - 4; i++) {
            toAdd += "/" + parts[i];
        }
        toAdd += "/lib/";
        toAdd = toAdd.substring(1);

        File folder = new File(toAdd);
        File[] files = folder.listFiles();

        for (File file : files) {
            cp += toAdd + file.getName() + ";";
        }

        System.out.println("CP: " + cp);
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", cp, "networking.ai.instance.AIInstance");
        processBuilder.inheritIO();
        processBuilder.start();
    }
}
