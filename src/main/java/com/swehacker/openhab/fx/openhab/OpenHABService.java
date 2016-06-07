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

package com.swehacker.openhab.fx.openhab;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.SseFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OpenHABService {
    private final WebTarget target;
    private final Client client;
    private static final Properties labels = new Properties();
    private final List<Item> items = new ArrayList<>();

    static {
        // Static map that connects item names (openhab) to "readable"
        labels.put("Office_DeskLamp", "Skrivbord");
        labels.put("Office_WBLamp", "Arbetsbänk");
        labels.put("Office_Elect", "Elektronik");
        labels.put("Bedroom_Bookcase", "Bokhylla");
        labels.put("Bedroom_Lamp1", "Sovrum vänster");
        labels.put("Bedroom_Lamp2", "Sovrum höger");
        labels.put("Living_TableLamp", "Bordslampa");
        labels.put("Living_FloorLamp", "Golvlampa");
        labels.put("Hall_Sensor", "Hall");
        labels.put("WANHAO_4S", "3D Skrivare");
        labels.put("Temperature_Living", "Vardagsrum");
        labels.put("Temperature_Office", "Labbet");
        labels.put("Temperature_Bed", "Sovrum");
        labels.put("Temperature_Balcony", "Balkong");
    }

    public enum STATE {
        ON("ON"),
        OFF("OFF");

        private final String state;

        STATE(final String state) {
            this.state = state;
        }
    }

    public OpenHABService(final String ip, final int port) {
        client = ClientBuilder.newBuilder()
                .register(SseFeature.class)
                .register(ClientResponseLoggingFilter.class)
                .build();
        target = client.target(UriBuilder.fromUri("http://" + ip + ":" + port + "/rest").build());

        EventSource eventSource = EventSource.target(target.path("/items")).build();
        EventListener listener = inboundEvent -> System.out.println(inboundEvent.getName() + "; "
                + inboundEvent.readData(String.class));
        eventSource.register(listener, "message-to-client");
        eventSource.open();
    }

    public void toggle(Item item, STATE state) {
        WebTarget target = client.target(item.getLink());
        Invocation.Builder builder = target.request(MediaType.TEXT_PLAIN_TYPE);
        builder.post(Entity.entity(state.state, MediaType.TEXT_PLAIN));
    }

    public List<Item> getSensor() throws Exception {
        List<Item> all = getAll();
        ArrayList<Item> sensors = new ArrayList<>();
        for (Item item : all) {
            if ("NumberItem".equals(item.getType())) {
                sensors.add(item);
            }
        }
        return sensors;
    }

    public List<Item> getSwitches() throws Exception {
        List<Item> all = getAll();
        ArrayList<Item> switches = new ArrayList<>();
        for (Item item : all) {
            if ("SwitchItem".equals(item.getType())) {
                switches.add(item);
            }
        }
        return switches;
    }

    private List<Item> getAll() throws Exception {
        ArrayList<Item> items = new ArrayList<>();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        Document document = builder.parse(new ByteArrayInputStream(
                target.path("/items")
                        .request()
                        .accept(MediaType.APPLICATION_XML)
                        .get(String.class)
                        .getBytes(StandardCharsets.UTF_8))
        );

        NodeList rootNode = document.getDocumentElement().getChildNodes();
        for (int a = 0; a < rootNode.getLength(); a++) {
            Node node = rootNode.item(a);
            String type = ((Element) node).getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue();
            String name = ((Element) node).getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            String label = getLabel(name);
            String state = ((Element) node).getElementsByTagName("state").item(0).getChildNodes().item(0).getNodeValue();
            String link = ((Element) node).getElementsByTagName("link").item(0).getChildNodes().item(0).getNodeValue();

            items.add(new Item(type, name, label, state, link));
        }

        return items;
    }

    private String getLabel(String name) {
        String label = (String) labels.get(name);
        if (label == null) {
            return name;
        }

        return label;
    }
}
