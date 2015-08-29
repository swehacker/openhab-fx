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

import com.swehacker.openhab.fx.App;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * A filter for logging client responses (rest) for debugging.
 */
public class ClientResponseLoggingFilter implements ClientResponseFilter {
    
    private static final Logger logger = Logger.getLogger(App.class.getName());

    @Override
    public void filter(final ClientRequestContext reqCtx, final ClientResponseContext resCtx) throws IOException {
        StringBuilder log = new StringBuilder();

        log.append("status: " + resCtx.getStatus());
        log.append("\n");
        log.append("date: " + resCtx.getDate());
        log.append("\n");
        log.append("last-modified: " + resCtx.getLastModified());
        log.append("\n");
        log.append("location: " + resCtx.getLocation());
        log.append("\n");
        log.append("headers:");
        log.append("\n");
        for (Entry<String, List<String>> header : resCtx.getHeaders().entrySet()) {
            log.append("\t" + header.getKey() + " :");
            for (String value : header.getValue()) {
                log.append(value + ", ");
            }
            log.append("\n");
        }
        if (resCtx.getMediaType() != null) {
            log.append("media-type: " + resCtx.getMediaType().getType());
        }

        logger.finest(log.toString());
    }
}
