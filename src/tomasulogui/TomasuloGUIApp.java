/*
 * TomasuloGUIApp.java
 */

package tomasulogui;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class TomasuloGUIApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new TomasuloGUIView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of TomasuloGUIApp
     */
    public static TomasuloGUIApp getApplication() {
        return Application.getInstance(TomasuloGUIApp.class);
    }

    /**
     * Main method launching the application.git
     */
    public static void main(String[] args) {
        launch(TomasuloGUIApp.class, args);
    }
}
