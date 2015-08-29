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

import javafx.geometry.VPos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LightSwitch extends Region {
    private boolean on = false;
    private final Region icon = new Region();
    private final Text title = new Text("Light");

    public LightSwitch() {
        getStyleClass().addAll("switch", "switch-off");
        title.getStyleClass().addAll("switch", "switch-title");
        icon.getStyleClass().addAll("switch", "switch-icon-off");

        this.setClip(new Rectangle(100, 100));

        BorderPane pane = new BorderPane();

        VBox box = new VBox();
        box.setId("vbox");

        title.setTextAlignment(TextAlignment.CENTER);
        title.setTextOrigin(VPos.CENTER);
        title.setFontSmoothingType(FontSmoothingType.LCD);
        box.getChildren().add(title);

        pane.setTop(box);
        pane.setCenter(icon);

        this.getChildren().add(pane);
    }

    public void setName(String title) {
        this.title.setText(title);
    }

    public void flip() {
        if (on) {
            on = false;
            this.getStyleClass().add("switch-off");
            this.getStyleClass().remove("switch-on");
            icon.getStyleClass().add("switch-icon-off");
            icon.getStyleClass().remove("switch-icon-on");
        } else {
            on = true;
            this.getStyleClass().add("switch-on");
            this.getStyleClass().remove("switch-off");
            icon.getStyleClass().add("switch-icon-on");
            icon.getStyleClass().remove("switch-icon-off");
        }
    }

    public boolean isOn() {
        return on;
    }
}
