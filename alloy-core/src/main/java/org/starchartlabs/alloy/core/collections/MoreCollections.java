/*
 * Copyright (C) 2018 StarChart Labs Authors.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.core.collections;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Provides methods for streamlining and reducing boilerplate in relation to operations related to Java collections
 *
 * @author romeara
 * @since 0.3.0
 */
public final class MoreCollections {

    /**
     * Creates a builder for streamlined initialization of maps
     *
     * <pre>
     * Map&lt;String, String&gt; example = MoreCollections.&lt;String, String&gt; mapBuilder()
     *         .put("key", "value")
     *         .build();
     * </pre>
     *
     * @param <K>
     *            The type of keys maintained by the resulting map
     * @param <V>
     *            The type of mapped values
     * @return A builder capable of streamlined map creation from single or multiple entries, with a
     *         modifiable/unmodifiable result
     * @since 1.0.0
     */
    public static <K, V> MapBuilder<K, V> mapBuilder() {
        return new MapBuilder<K, V>();
    }

    /**
     * Builder for streamlined initialization of maps
     *
     * @author romeara
     *
     * @param <K>
     *            The type of keys maintained by the resulting map
     * @param <V>
     *            The type of mapped values
     * @since 1.0.0
     */
    public static final class MapBuilder<K, V> {

        private List<Entry<K, V>> values;

        private boolean unmodifiable;

        private MapBuilder() {
            values = new ArrayList<>();
            unmodifiable = true;
        }

        /**
         * @param key
         *            Key to add to the result of this builder
         * @param value
         *            Values to associate with the resulting key
         * @return This builder instance
         * @since 1.0.0
         */
        public MapBuilder<K, V> put(K key, V value) {
            values.add(new AbstractMap.SimpleEntry<>(key, value));

            return this;
        }

        /**
         * @param m
         *            Existing map of values to add to the result of this builder
         * @return This builder instance
         * @since 1.0.0
         */
        public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> m) {
            Objects.requireNonNull(m);

            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }

            return this;
        }

        /**
         * Configures the resulting map to be mutable/modifiable
         *
         * @return This builder instance
         * @since 1.0.0
         */
        public MapBuilder<K, V> mutable() {
            this.unmodifiable = false;

            return this;
        }

        /**
         * Creates a new map from the specified values
         *
         * <pre>
         * Map&lt;String, String&gt; example = MoreCollections.&lt;String, String&gt; mapBuilder()
         *         .put("key", "value")
         *         .build();
         * </pre>
         *
         * @return A map with the specified values/properties
         * @since 1.0.0
         */
        public Map<K, V> build() {
            return build(HashMap::new);
        }

        /**
         * Creates a new map from the specified values
         *
         * <p>
         * Provided for cases where the specific implementation, capacity, or other properties of the resulting map are
         * important. Note that unless mutabliity is specified, the resulting map will be made unmodifiable
         *
         * <pre>
         * Map&lt;String, String&gt; example = MoreCollections.&lt;String, String&gt; mapBuilder()
         *         .put("key", "value")
         *         .build(LinkedHashMap::new);
         * </pre>
         *
         * @param mapSupplier
         *            Supplier which provides a map with specific required properties for use
         * @return A map with the specified values/properties, beginning from the map obtained from the provided
         *         supplier
         * @since 1.0.0
         */
        public Map<K, V> build(Supplier<Map<K, V>> mapSupplier) {
            Objects.requireNonNull(mapSupplier);

            // We iterate in this manner due to constraints on the finality of variables used within lambdas
            Map<K, V> result = mapSupplier.get();

            for (Entry<K, V> entry : values) {
                result.put(entry.getKey(), entry.getValue());
            }

            if (unmodifiable) {
                result = Collections.unmodifiableMap(result);
            }

            return result;
        }

    }

}
