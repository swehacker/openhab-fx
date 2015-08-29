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

package com.swehacker.openhab.fx.screens;

import com.swehacker.openhab.fx.App;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class ScreenController extends StackPane {
    public static final Logger logger = Logger.getLogger(App.class.getName());

    public enum SCREEN {
        HOME("/fxml/home.fxml"),
        SENSOR("/fxml/sensor.fxml");

        final String resource;

        SCREEN(String resource) {
            this.resource = resource;
        }

        public String resource() {
            return resource;
        }
    }

    private HashMap<String, Node> screens = new HashMap<>();

    /**
     * Loads the fxml file specified by resource, and it gets the top Node for the screen.
     * We can also get the controller associated to this screen, which allows us to set the parent for the screen, as
     * all the controllers shared the common type ControlledScreen. Finally the screen is added to the screens hash map.
     * As you can see from the code, the loaded fxml file, doesn't get added to the scene graph, so the loaded screen
     * doesn't get displayed or loaded to the screen.
     *
     * @return
     */
    public void loadScreens() {
        for (SCREEN scrResource : SCREEN.values()) {
            try {
                logger.finest("Loading " + scrResource.resource());
                FXMLLoader myLoader = new FXMLLoader();
                InputStream in = ScreenController.class.getResourceAsStream(scrResource.resource());
                myLoader.setBuilderFactory(new JavaFXBuilderFactory());
                myLoader.setLocation(ScreenController.class.getResource(scrResource.resource()));
                Parent loadScreen = myLoader.load();
                Screen myScreenControler = myLoader.getController();
                myScreenControler.setScreenParent(this);
                screens.put(scrResource.name(), loadScreen);
            } catch (Exception e) {
                logger.severe(e.getMessage());
            }
        }
    }

    /**
     * This method displays a screen with a given screen id.
     * We check if there is already a screen been displayed, so we need to play the screen transition sequence.
     * If there isn't any screen, we just add it to the graph and perform a nice fade-in animation.
     * If there is a screen already been displayed, we play an animation to fade out the current screen, and we defined
     * an eventHandler to handle execution after this.
     * <p>
     * Once the screen is invisible, we remove it from the graph, and we add the new screen. Again, a nice animation is
     * played to show the new screen.
     */
    public void changeScreen(SCREEN screen) {
        final DoubleProperty opacity = opacityProperty();

        //Is there is more than one screen
        if (!getChildren().isEmpty()) {
            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                    new KeyFrame(new Duration(250), event -> {
                        //remove displayed screen
                        getChildren().remove(0);
                        getChildren().add(0, screens.get(screen.name()));
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }, new KeyValue(opacity, 0.0)));
            fade.play();
        } else {
            //no one else been displayed, then just show
            setOpacity(0.0);
            getChildren().add(screens.get(screen.name()));
            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
            fadeIn.play();
        }
    }
}
