/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API are heavily based on Guava's ListMultimap class, and fall under their Apache 2.0 copyright:
 *
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.starchartlabs.alloy.core.collections;

import java.util.List;

import javax.annotation.Nullable;

/**
 * A {@code Multimap} that can hold duplicate key-value pairs and that maintains the insertion ordering of values for a
 * given key
 *
 * <p>
 * Due to the operation of the generics, the map returned by {@link #asMap} has {@code List} values, event thought the
 * method signature doesn't say so explicitly
 *
 * @see Multimap
 * @author romeara
 * @since 0.3.0
 */
public interface ListMultimap<K, V> extends Multimap<K, V> {

    /**
     * [{@inheritDoc}
     *
     * @since 0.3.0
     */
    @Override
    List<V> replaceValues(@Nullable K key, Iterable<? extends V> values);

    /**
     * [{@inheritDoc}
     *
     * @since 0.3.0
     */
    @Override
    List<V> removeAll(@Nullable K key);

    /**
     * [{@inheritDoc}
     *
     * @since 0.3.0
     */
    @Override
    List<V> get(@Nullable K key);

}
