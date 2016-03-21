/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Patrik Falk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.swehacker.openhab.fx;

import com.swehacker.openhab.fx.screens.ScreenController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static final String OS_NAME = System.getProperty("ensemble.os.name", System.getProperty("os.name"));
    private static final String OS_ARCH = System.getProperty("ensemble.os.arch", System.getProperty("os.arch"));
    public static final boolean IS_IPHONE = false;
    public static final boolean IS_IOS = "iOS".equals(OS_NAME) || "iOS Simulator".equals(OS_NAME);
    public static final boolean IS_ANDROID = "android".equals(System.getProperty("javafx.platform")) || "Dalvik".equals(System.getProperty("java.vm.name"));
    public static final boolean IS_EMBEDDED = "arm".equals(OS_ARCH) && !IS_IOS && !IS_ANDROID;
    public static final boolean IS_DESKTOP = !IS_EMBEDDED && !IS_IOS && !IS_ANDROID;
    public static final boolean IS_MAC = OS_NAME.startsWith("Mac");

    private ScreenController screenController = new ScreenController();

    private boolean home = true;

    private Scene scene;

    static {
        // Setup logging
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);
        logger.addHandler(ch);

        System.setProperty("java.net.useSystemProxies", "true");
        logger.config("IS_IPHONE = " + IS_IPHONE);
        logger.config("IS_MAC = " + IS_MAC);
        logger.config("IS_IOS = " + IS_IOS);
        logger.config("IS_ANDROID = " + IS_ANDROID);
        logger.config("IS_EMBEDDED = " + IS_EMBEDDED);
        logger.config("IS_DESKTOP = " + IS_DESKTOP);
    }

    @Override
    public void init() {
        screenController.loadScreens();
        screenController.changeScreen(ScreenController.SCREEN.HOME);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setStylesheets() {
        scene.getStylesheets().setAll("/stylesheets/default.css");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // CREATE SCENE
        BorderPane root = new BorderPane();
        root.getStyleClass().addAll("root");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(root);
        root.setTop(screenController);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        if ( !IS_DESKTOP) {
            scene = new Scene(scrollPane, bounds.getWidth(), bounds.getHeight());
            logger.config("Setting the Screen to " + bounds.getWidth() + "x" + bounds.getHeight());
        } else {
            scene = new Scene(scrollPane, 230, bounds.getHeight());
            primaryStage.setX(bounds.getMaxX() - 230);
            logger.config("Setting the Screen to 230x" + bounds.getHeight());
        }

        if (IS_EMBEDDED || IS_ANDROID) {
            //new ScrollEventSynthesizer(scene);
            primaryStage.setFullScreen(true);
        }

        setStylesheets();
        primaryStage.setScene(scene);

        // START FULL SCREEN IF WANTED
        if (PlatformFeatures.START_FULL_SCREEN) {
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            logger.config("Setting the Screen to " + bounds.getWidth() + "x" + bounds.getHeight());
        }

        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App.launch(args);
    }
}
