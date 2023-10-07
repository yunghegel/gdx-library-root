/* (C)2023 */
package app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Launcher {

    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Editor(), createConfig());
    }

    private static Lwjgl3ApplicationConfiguration createConfig() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 8);
        config.setIdleFPS(0);
        config.setTitle("Salient");
        config.useVsync(false);
        config.setResizable(true);
        config.setWindowedMode(1920, 1080);
        config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 4, 3);
        config.setAutoIconify(true);

        config.setWindowIcon("icon.png");
//        config.setDecorated(false);

        return config;
    }
}
