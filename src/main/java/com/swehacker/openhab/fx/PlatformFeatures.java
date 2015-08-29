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

import javafx.application.ConditionalFeature;
import javafx.application.Platform;

public class PlatformFeatures {
    private static final String os = System.getProperty("os.name");
    private static final String arch = System.getProperty("os.arch");

    private static final boolean WINDOWS = os.startsWith("Windows");
    private static final boolean MAC = os.startsWith("Mac");
    private static final boolean LINUX = os.startsWith("Linux");
    private static final boolean ANDROID = "android".equals(System.getProperty("javafx.platform")) || "Dalvik".equals(System.getProperty("java.vm.name"));
    private static final boolean IOS = os.startsWith("iOS");
    private static final boolean EMBEDDED = "arm".equals(arch) && !IOS && !ANDROID;

    public static final boolean SUPPORTS_BENDING_PAGES = !EMBEDDED;
    public static final boolean HAS_HELVETICA = MAC || IOS;
    public static final boolean USE_IOS_THEME = IOS;
    public static final boolean START_FULL_SCREEN = EMBEDDED || IOS || ANDROID;
    public static final boolean LINK_TO_SOURCE = !(EMBEDDED || IOS || ANDROID);
    public static final boolean DISPLAY_PLAYGROUND = !(EMBEDDED || IOS || ANDROID);
    public static final boolean USE_EMBEDDED_FILTER = EMBEDDED || IOS || ANDROID;
    public static final boolean WEB_SUPPORTED = Platform.isSupported(ConditionalFeature.WEB);

    private PlatformFeatures(){}
}
