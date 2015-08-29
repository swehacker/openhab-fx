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

package com.swehacker.openhab.fx.controls;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This represents a temperature sensor.
 */
public class Sensor extends Region {
    private final Region icon = new Region();
    private final Text title = new Text("Temperatur");
    private final Text celsius = new Text("- -");

    public Sensor() {
        getStyleClass().addAll("sensor");
        title.getStyleClass().add("sensor-top");
        this.setClip(new Rectangle(100, 100));

        BorderPane pane = new BorderPane();

        VBox box = new VBox();
        box.getStyleClass().add("sensor-top");
        box.getChildren().add(title);

        pane.setTop(box);

        celsius.getStyleClass().add("sensor-center");
        pane.setCenter(celsius);

        VBox bottomBox = new VBox();

        Text t = new Text("°C");
        t.getStyleClass().add("sensor-bottom");
        bottomBox.getStyleClass().add("sensor-bottom");
        bottomBox.getChildren().add(t);
        pane.setBottom(bottomBox);

        this.getChildren().add(pane);
    }

    public void setValue(String text) {
        try {
            celsius.setText("" + Double.parseDouble(text));
        } catch (Throwable t) {
            System.out.println("Ej initierad sensor, hoppar över");
        }
    }

    public void setName(String title) {
        this.title.setText(title);
    }
}
