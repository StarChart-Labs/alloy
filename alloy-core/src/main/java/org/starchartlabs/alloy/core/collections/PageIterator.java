/*
 * Copyright (C) 2019 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.core.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * Represents a specialized iterator for handling elements read in distinct chunks ("pages"). Instances of
 * {@link PageIterator} are primarily used with {@link MoreSpliterators#ofPaged(PageIterator)} to allow treating a
 * sequence of pages as a standard Java {@link Stream}
 *
 * <p>
 * Implementations of {@link PageIterator} handle underlying traversal and advancement through a sequence of pages
 *
 * @author romeara
 *
 * @param <T>
 *            Type representing a single element of the paged data set
 * @since 0.4.0
 * @see MoreSpliterators#ofPaged(PageIterator)
 */
public interface PageIterator<T> extends Iterator<Collection<T>> {

    /**
     * Returns an estimate of the number of elements that would be encountered by a complete traversal, or returns
     * {@link Long#MAX_VALUE} if infinite, unknown, or too expensive to compute.
     *
     * <p>
     * Used by spliterator implementations for {@link Spliterator#estimateSize}
     *
     * @return The estimated size, or {@code Long.MAX_VALUE} if infinite, unknown, or too expensive to compute.
     * @since 0.4.0
     */
    long estimateSize();

    /**
     * Utilized by spliterator implementations to partition via {@link Spliterator#trySplit()}
     *
     * <p>
     * If a non-null result is provided, it is expected that the elements covered by the result will not be covered by
     * this PageProvider
     *
     * @return a {@code PageProvider} covering some portion of the elements, or {@code null} if this spliterator cannot
     *         be split
     * @since 0.4.0
     */
    PageIterator<T> trySplit();

}
