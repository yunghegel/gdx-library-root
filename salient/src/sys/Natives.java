package sys;

import app.Salient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.badlogic.gdx.files.FileHandle;
import events.lifecycle.ShutdownEvent;
import events.lifecycle.StartupEvent;
import events.natives.WindowFocusEvent;
import events.natives.WindowIconifiedEvent;
import events.natives.WindowMaximizedEvent;
import events.natives.WindowRestoredEvent;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.desktop.DesktopFileChooser;
import org.lwjgl.glfw.GLFW;
import io.DirectoryMappings;

public class Natives  implements WindowNatives, Lwjgl3WindowListener, LifecycleListener {

    boolean isMaximized = false;
    private DesktopFileChooser fileChooser;


    public Natives(){
        Gdx.app.addLifecycleListener(this);
        fileChooser = new DesktopFileChooser();
    }


    @Override
    public void created(Lwjgl3Window window) {
        new StartupEvent().post();
    }

    @Override
    public void iconified(boolean isIconified) {
        new WindowIconifiedEvent(isIconified).post();
    }

    @Override
    public void maximized(boolean isMaximized) {
        new WindowMaximizedEvent(isMaximized).post();

    }

    @Override
    public void focusLost() {
        new WindowFocusEvent(false).post();

    }

    @Override
    public void focusGained() {
        new WindowFocusEvent(true).post();

    }

    @Override
    public boolean closeRequested() {
       new ShutdownEvent().post();
        Salient.INSTANCE.getProjectManager().currentProject.save();
        return false;
    }

    @Override
    public void filesDropped(String[] files) {

    }

    @Override
    public void refreshRequested() {

    }

    @Override
    public void setPosition(int x, int y) {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        GLFW.glfwSetWindowPos(window.getWindowHandle(), x, y);
    }

    @Override
    public void setFullscreen() {

        if(isMaximized){
            new WindowMaximizedEvent(true).post();

            Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
            GLFW.glfwRestoreWindow(window.getWindowHandle());
            isMaximized = false;
        }
        else {
            new WindowMaximizedEvent(false).post();
            Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
            GLFW.glfwMaximizeWindow(window.getWindowHandle());
            isMaximized = true;
        }


    }

    @Override
    public void setIconified() {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        GLFW.glfwIconifyWindow(window.getWindowHandle());


        new WindowIconifiedEvent(window.isIconified()).post();
    }

    @Override
    public void setWindowedMode(int width, int height) {
        Gdx.graphics.setWindowedMode(width, height);
    }

    @Override
    public void resizeWindow(int width, int height) {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        GLFW.glfwSetWindowSize(window.getWindowHandle(), width, height);
    }

    @Override
    public void restoreWindow() {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        GLFW.glfwRestoreWindow(window.getWindowHandle());
        new WindowRestoredEvent().post();

    }

    @Override
    public void dragWindow(int x, int y) {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        window.setPosition(x, y);
    }

    @Override
    public int getWindowX() {
        return ((Lwjgl3Graphics) Gdx.graphics).getWindow().getPositionX();

    }

    @Override
    public int getWindowY() {
        return ((Lwjgl3Graphics) Gdx.graphics).getWindow().getPositionY();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        new ShutdownEvent().post();
        Salient.INSTANCE.getProjectManager().save();
    }

    public void chooseFile(NativeFileChooserCallback callback){
        NativeFileChooserConfiguration config = new NativeFileChooserConfiguration();
        config.directory = new FileHandle(DirectoryMappings.INSTANCE.getPROJECTS_DIR());
        fileChooser.chooseFile(config,callback);

    }

    public void chooseFile(FileHandle directory, NativeFileChooserCallback callback){
        NativeFileChooserConfiguration config = new NativeFileChooserConfiguration();
        config.directory = directory;
        fileChooser.chooseFile(config,callback);
    }

}
