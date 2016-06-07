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

import com.swehacker.openhab.fx.controls.LightSwitch;
import com.swehacker.openhab.fx.controls.Sensor;
import com.swehacker.openhab.fx.openhab.Item;
import com.swehacker.openhab.fx.openhab.OpenHABService;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class HomeScreen implements Screen {
    private static final String SERVER_ADDRESS = "192.168.1.5";
    private static final int SERVER_PORT = 8080;
    private OpenHABService openHABService;
    private ScreenController parent;

    @FXML
    FlowPane switchPanel;

    @FXML
    FlowPane sensorPanel;

    @Override
    public void setScreenParent(ScreenController screenPage) {
        this.parent = screenPage;
        init();
    }

    public void init() {
        openHABService = new OpenHABService(SERVER_ADDRESS, SERVER_PORT);
        update();
    }

    private void update() {
        try {
            for (Item item : openHABService.getSwitches()) {
                LightSwitch roomSwitch = new LightSwitch();
                roomSwitch.setName(item.getLabel());
                if ("ON".equals(item.getState())) {
                    roomSwitch.flip();
                }

                roomSwitch.setOnMouseClicked(event -> {
                    roomSwitch.flip();
                    if (roomSwitch.isOn()) {
                        openHABService.toggle(item, OpenHABService.STATE.ON);
                    } else {
                        openHABService.toggle(item, OpenHABService.STATE.OFF);
                    }
                });
                switchPanel.getChildren().add(roomSwitch);
            }

            for (Item item : openHABService.getSensor()) {
                Sensor roomSensor = new Sensor();
                roomSensor.setName(item.getLabel());
                roomSensor.setValue(item.getState());
                roomSensor.setOnMouseClicked(event -> {
                    parent.changeScreen(ScreenController.SCREEN.SENSOR);
                });
                sensorPanel.getChildren().add(roomSensor);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
