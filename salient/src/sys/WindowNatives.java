package sys;

public interface WindowNatives {

    void setPosition(int x, int y);

    void setFullscreen();

    void setIconified();

    void setWindowedMode(int width, int height);

    void resizeWindow(int width, int height);

    void restoreWindow();

    void dragWindow(int x, int y);

    int getWindowX();

    int getWindowY();

}
