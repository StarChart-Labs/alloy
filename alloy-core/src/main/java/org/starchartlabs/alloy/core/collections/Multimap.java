/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API are heavily based on Guava's Multimap class, and fall under their Apache 2.0 copyright:
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

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

/**
 * A collection that maps keys to values, similar to {@link Map}, but in which each key may be associated with
 * <i>multiple</i> values. You can visualize the contents of a multimap as a single "flattened" collection of key-value
 * pairs:
 *
 * <ul>
 * <li>a -&gt; 1
 * <li>a -&gt; 2
 * <li>b -&gt; 3
 * </ul>
 *
 * <h3>Views</h3>
 *
 * <p>
 * Much of the power of the multimap API comes from the <i>view collections</i> it provides. These always reflect the
 * latest state of the multimap itself. When they support modification, the changes are <i>write-through</i> (they
 * automatically update the backing multimap). These view collections are:
 *
 * <ul>
 * <li>{@link #asMap}, mentioned above
 * <li>{@link #flatKeys()}, {@link #keySet}, {@link #flatValues()}, {@link #flatEntries()}, which are similar to the
 * corresponding view collections of {@link Map}
 * <li>and, notably, even the collection returned by {@link #get get(key)} is an active view of the values corresponding
 * to {@code key}
 * </ul>
 *
 * <p>
 * The collections returned by the {@link #replaceValues replaceValues} and {@link #removeAll removeAll} methods, which
 * contain values that have just been removed from the multimap, are <i>not</i> views.
 *
 * <h3>Comparison to a map of collections</h3>
 *
 * <p>
 * Multimaps are commonly used in places where a {@code Map<K, Collection<V>>} would otherwise have appeared. The
 * differences include:
 *
 * <ul>
 * <li>There is no need to populate an empty collection before adding an entry with {@link #put put}.
 * <li>{@code get} never returns {@code null}, only an empty collection.
 * <li>A key is contained in the multimap if and only if it maps to at least one value. Any operation that causes a key
 * to have zero associated values has the effect of <i>removing</i> that key from the multimap.
 * <li>The total entry count is available as {@link #size}.
 * <li>Many complex operations become easier; for example, {@code Collections.min(multimap.values())} finds the smallest
 * value across all keys.
 * </ul>
 *
 * <h3>Other Notes</h3>
 *
 * <p>
 * As with {@code Map}, the behavior of a {@code Multimap} is not specified if key objects already present in the
 * multimap change in a manner that affects {@code equals} comparisons. Use caution if mutable objects are used as keys
 * in a {@code Multimap}.
 *
 * <p>
 * All methods that modify the multimap are optional. The view collections returned by the multimap may or may not be
 * modifiable. Any modification method that is not supported will throw {@link UnsupportedOperationException}.
 *
 * <p>
 * Heavily based on the Guava Multimap authored by Jared Levy
 *
 * @author romeara
 * @since 0.3.0
 */
public interface Multimap<K, V> {

    /**
     * @return The number of distinct keys in this multimap
     * @since 0.3.0
     */
    int size();

    /**
     * This method does not return the number of <i>distinct keys</i> in the multimap, which is given by
     * {@code keySet().size()} or {@code asMap().size()}. See the opening section of the {@link Multimap} class
     * documentation for clarification.
     *
     * @return The number of key-value pairs in this multimap
     * @since 0.3.0
     */
    int flatSize();

    /**
     * @return {@code true} if this multimap contains no key-value pairs
     * @since 0.3.0
     */
    boolean isEmpty();

    /**
     * @param key
     *            Distinct key to compare against available keys in this multimap
     * @return {@code true} if this multimap contains at least one key-value pair with the key {@code key}
     * @since 0.3.0
     */
    boolean containsKey(@Nullable K key);

    /**
     * @param value
     *            Value to compare against available values in this multimap
     * @return {@code true} if this multimap contains at least one key-value pair with the value {@code value}
     * @since 0.3.0
     */
    boolean containsValue(@Nullable V value);

    /**
     * @param key
     *            Distinct key to compare against available keys in this multimap
     * @param value
     *            Value to compare against available values in this multimap
     * @return {@code true} if this multimap contains at least one key-value pair with the key {@code key} and the value
     *         {@code value}
     * @since 0.3.0
     */
    boolean containsEntry(@Nullable K key, @Nullable V value);

    /**
     * Stores a key-value pair in this multimap.
     *
     * <p>
     * Some multimap implementations allow duplicate key-value pairs, in which case {@code put} always adds a new
     * key-value pair and increases the multimap size by 1. Other implementations prohibit duplicates, and storing a
     * key-value pair that's already in the multimap has no effect.
     *
     * @param key
     *            Key to add to this multimap
     * @param value
     *            Single value to add to the collection of values associated with the given {@code key}
     * @return {@code true} if the method increased the size of the multimap, or {@code false} if the multimap already
     *         contained the key-value pair and doesn't allow duplicates
     * @since 0.3.0
     */
    boolean put(@Nullable K key, @Nullable V value);

    /**
     * Removes a single key-value pair with the key {@code key} and the value {@code value} from this multimap, if such
     * exists. If multiple key-value pairs in the multimap fit this description, which one is removed is unspecified.
     *
     * @param key
     *            Key the value to remove is associated with
     * @param value
     *            Single value to remove to the collection of values associated with the given {@code key}
     * @return {@code true} if the multimap changed
     * @since 0.3.0
     */
    boolean remove(@Nullable K key, @Nullable V value);

    /**
     * Stores a key-value pair in this multimap for each of {@code values}, all using the same key, {@code key}.
     * Equivalent to (but expected to be more efficient than):
     *
     * <pre>
     * for (V value : values) {
     *     put(key, value);
     * }
     * </pre>
     *
     * <p>
     * In particular, this is a no-op if {@code values} is empty.
     *
     * @param key
     *            Key to associate the value(s) to add with
     * @param values
     *            Value(s) to add to the collection of values associated with the given {@code key}
     * @return {@code true} if the multimap changed
     * @since 0.3.0
     */
    boolean putAll(@Nullable K key, Iterable<? extends V> values);

    /**
     * @param multimap
     *            Multimap of entries to store in this multimap (in the order returned by
     *            {@link Multimap#flatEntries()})
     * @return {@code true} if the multimap changed
     * @since 0.3.0
     */
    boolean putAll(Multimap<? extends K, ? extends V> multimap);

    /**
     * Stores a collection of values with the same key, replacing any existing values for that key.
     *
     * <p>
     * If {@code values} is empty, this is equivalent to {@link #removeAll(Object) removeAll(key)}
     *
     * @param key
     *            The key to replace existing values for
     * @param values
     *            Values a new collection of values to associate with the given {@code key}
     * @return The collection of replaced values, or an empty collection if no values were previously associated with
     *         the key
     * @since 0.3.0
     */
    Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values);

    /**
     * Removes all values associated with the key {@code key}
     *
     * <p>
     * Once this method returns, {@code key} will not be mapped to any values, so it will not appear in
     * {@link #keySet()}, {@link #asMap()}, or any other views.
     *
     * @param key
     *            Key to remove all associated values for
     * @return The values that were removed
     * @since 0.3.0
     */
    Collection<V> removeAll(@Nullable K key);

    /**
     * Removes all key-value pairs from the multimap, leaving it {@linkplain #isEmpty empty}
     *
     * @since 0.3.0
     */
    void clear();

    /**
     * Changes to the returned collection will update the underlying multimap, and vice versa.
     *
     * @param key
     *            Key to read all available values for
     * @return A view collection of the values associated with {@code key} in this multimap, if any. Note that when
     *         {@code containsKey(key)} is false, this returns an empty collection, not {@code null}
     * @since 0.3.0
     */
    Collection<V> get(@Nullable K key);

    /**
     * Changes to the returned set will update the underlying multimap, and vice versa. However, <i>adding</i> to the
     * returned set is not possible.
     *
     * @return aAview collection of all <i>distinct</i> keys contained in this multimap. Note that the key set contains
     *         a key if and only if this multimap maps that key to at least one value
     * @since 0.3.0
     */
    Set<K> keySet();

    /**
     * Changes to the returned multiset will update the underlying multimap, and vice versa. However, <i>adding</i> to
     * the returned collection is not possible.
     *
     * @return A view collection containing the key from each key-value pair in this multimap, <i>without</i> collapsing
     *         duplicates. This collection has the same size as this multimap, and
     *         {@code keys().count(k) == get(k).size()} for all {@code k}
     * @since 0.3.0
     */
    Collection<K> flatKeys();

    /**
     * Changes to the returned collection will update the underlying multimap, and vice versa. However, <i>adding</i> to
     * the returned collection is not possible.
     *
     * @return A view collection containing the <i>value</i> from each key-value pair contained in this multimap,
     *         without collapsing duplicates (so {@code values().size() == size()})
     * @since 0.3.0
     */
    Collection<V> flatValues();

    /**
     * Changes to the returned collection or the entries it contains will update the underlying multimap, and vice
     * versa. However, <i>adding</i> to the returned collection is not possible.
     *
     * @return A view collection of all key-value pairs contained in this multimap, as {@link Entry} instances
     * @since 0.3.0
     */
    Collection<Entry<K, V>> flatEntries();

    /**
     * Changes to the returned map or the collections that serve as its values will update the underlying multimap, and
     * vice versa. The map does not support {@code put} or {@code putAll}, nor do its entries support
     * {@link Entry#setValue setValue}.
     *
     * @return A view of this multimap as a {@code Map} from each distinct key to the nonempty collection of that key's
     *         associated values. Note that {@code this.asMap().get(k)} is equivalent to {@code this.get(k)} only when
     *         {@code k} is a key contained in the multimap; otherwise it returns {@code null} as opposed to an empty
     *         collection
     * @since 0.3.0
     */
    Map<K, Collection<V>> asMap();

    /**
     * Performs the given action for all key-value pairs contained in this multimap. If an ordering is specified by the
     * {@code Multimap} implementation, actions will be performed in the order of iteration of {@link #flatEntries()}.
     * Exceptions thrown by the action are relayed to the caller.
     *
     * <p>
     * To loop over all keys and their associated value collections, write
     * {@code Multimaps.asMap(multimap).forEach((key, valueCollection) -> action())}.
     *
     * @param action
     *            The action to perform per key/value pair
     * @since 0.3.0
     */
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);

        flatEntries().forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }

}
