/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API are heavily based on Guava's Comparators class, and fall under their Apache 2.0 copyright:
 *
 * Copyright (C) 2016 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.starchartlabs.alloy.core;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides methods for streamlining and reducing boilerplate in relation to use of {@link Comparator} instances
 *
 * @since 0.2.0
 * @author romeara
 */
public final class Comparators {

    /**
     * Prevent instantiation of utility class
     */
    private Comparators() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate instance of utility class '" + getClass().getName() + "'");
    }

    /**
     * @param iterable
     *            The ordered sequence of values to evaluate ordering on
     * @param comparator
     *            Comparator used to determine ordering compliance
     * @param <T>
     *            The type being compared to determine ordering state
     * @return {@code true} if each element in {@code iterable} after the first is greater than or equal to the element
     *         that preceded it, according to the specified comparator
     * @since 0.2.0
     */
    public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Objects.requireNonNull(iterable);
        Objects.requireNonNull(comparator);

        Iterator<? extends T> it = iterable.iterator();

        if (it.hasNext()) {
            T prev = it.next();
            while (it.hasNext()) {
                T next = it.next();
                if (comparator.compare(prev, next) > 0) {
                    return false;
                }
                prev = next;
            }
        }

        return true;
    }

    /**
     * @param iterable
     *            The ordered sequence of values to evaluate strict ordering on
     * @param comparator
     *            Comparator used to determine ordering compliance
     * @param <T>
     *            The type being compared to determine ordering state
     * @return {@code true} if each element in {@code iterable} after the first is <i>strictly</i> greater than the
     *         element that preceded it, according to the specified comparator. Note that an element is not
     *         <i>strictly</i> after another if they are considered equivalent
     * @since 0.2.0
     */
    public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Objects.requireNonNull(iterable);
        Objects.requireNonNull(comparator);
        Iterator<? extends T> it = iterable.iterator();

        if (it.hasNext()) {
            T prev = it.next();
            while (it.hasNext()) {
                T next = it.next();
                if (comparator.compare(prev, next) >= 0) {
                    return false;
                }
                prev = next;
            }
        }

        return true;
    }

    /**
     * @param valueComparator
     *            Comparator used to order present values
     * @param <T>
     *            The type being compared
     * @return A comparator of {@link Optional} values which treats {@link Optional#empty} as less than all other
     *         values, and orders the rest using {@code valueComparator} on the contained value
     * @since 0.2.0
     */
    public static <T> Comparator<Optional<T>> emptiesFirst(Comparator<? super T> valueComparator) {
        Objects.requireNonNull(valueComparator);

        return new EmptyOptionalComparator<>(true, valueComparator);
    }

    /**
     * @param valueComparator
     *            Comparator used to order present values
     * @param <T>
     *            The type being compared
     * @return A comparator of {@link Optional} values which treats {@link Optional#empty} as greater than all other
     *         values, and orders the rest using {@code valueComparator} on the contained value
     * @since 0.2.0
     */
    public static <T> Comparator<Optional<T>> emptiesLast(Comparator<? super T> valueComparator) {
        Objects.requireNonNull(valueComparator);

        return new EmptyOptionalComparator<>(false, valueComparator);
    }

    /**
     * Comparator that sorts based on the present/empty state of optional values
     *
     * @author romeara
     *
     * @param <T>
     *            The type of objects within Optional values that may be compared by this comparator
     */
    private final static class EmptyOptionalComparator<T> implements Comparator<Optional<T>> {

        private final boolean emptiesFirst;

        private final Comparator<T> valueComparator;

        @SuppressWarnings("unchecked")
        public EmptyOptionalComparator(boolean emptiesFirst, Comparator<? super T> valueComparator) {
            this.emptiesFirst = emptiesFirst;
            this.valueComparator = Objects.requireNonNull((Comparator<T>) valueComparator);
        }

        @Override
        public int compare(Optional<T> a, Optional<T> b) {
            boolean aEmpty = a != null && !a.isPresent();
            boolean bEmpty = b != null && !b.isPresent();

            if (aEmpty) {
                return (bEmpty) ? 0 : (emptiesFirst ? -1 : 1);
            } else if (bEmpty) {
                return emptiesFirst ? 1 : -1;
            } else {
                return valueComparator.compare(a.get(), b.get());
            }
        }

        @Override
        public Comparator<Optional<T>> reversed() {
            return new EmptyOptionalComparator<>(!emptiesFirst, valueComparator.reversed());
        }

    }

}
