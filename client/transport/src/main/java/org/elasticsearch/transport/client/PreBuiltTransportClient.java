/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.transport.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.percolator.PercolatorPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.mustache.MustachePlugin;
import org.elasticsearch.transport.Netty3Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


/**
 * A builder to create an instance of {@link TransportClient}
 * This class pre-installs the {@link Netty3Plugin}, {@link ReindexPlugin}, {@link PercolatorPlugin}, and {@link MustachePlugin}
 * for the client. These plugins are all elasticsearch core modules required.
 */
@SuppressWarnings({"unchecked","varargs"})
public class PreBuiltTransportClient extends TransportClient {
    private static final Collection<Class<? extends Plugin>> PRE_INSTALLED_PLUGINS = Collections.unmodifiableList(Arrays.asList(
        TransportPlugin.class, ReindexPlugin.class, PercolatorPlugin.class, MustachePlugin.class));

    @SafeVarargs
    public PreBuiltTransportClient(Settings settings, Class<? extends Plugin>... plugins) {
        this(settings, Arrays.asList(plugins));
    }

    public PreBuiltTransportClient(Settings settings, Collection<Class<? extends Plugin>> plugins) {
        super(settings, Settings.EMPTY, addPlugins(plugins, PRE_INSTALLED_PLUGINS));
    }

    /**
     * The default transport implementation for the transport client.
     */
    public static final class TransportPlugin extends Netty3Plugin {
        // disable assertions for permissions since we might not have the permissions here
        // compared to if we are loaded as a real module to the es server
        public TransportPlugin(Settings settings) {
            super(Settings.builder().put("netty.assert.buglevel", false).put(settings).build());
        }
    }
}
