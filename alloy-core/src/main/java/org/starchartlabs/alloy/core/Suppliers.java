/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual Supplier-implementing classes are heavily based on Guava's Suppliers class, and fall under their Apache 2.0 copyright:
 *
 * Copyright (C) 2007 The Guava Authors
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

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

/**
 * Utilities intended to enhance and streamline use of {@link Supplier} instances
 *
 * @author romeara
 * @since 0.1.0
 */
public final class Suppliers {

    /**
     * Prevent instantiation of utility class
     */
    private Suppliers() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate instance of utility class '" + getClass().getName() + "'");
    }

    /**
     * Returns a new supplier which calls the provided supplier and then applies the provided function to map the result
     * to a new representation. Note that the resulting supplier will not call {@code supplier} or invoke
     * {@code function} until it is called
     *
     * @param supplier
     *            Supplier which provides an initial value
     * @param function
     *            Mapping function which converts the supplied value to a different form
     * @return A supplier which converts a supplied value into a different representation via the provided
     *         {@code function}
     * @since 0.1.0
     */
    public static <S, T> Supplier<T> map(Supplier<S> supplier, Function<S, T> function) {
        return new MappingSupplier<>(supplier, function);
    }

    /**
     * Returns a supplier which caches the instance retrieved during the first call to {@code get()} and returns that
     * value on subsequent calls to {@code get()}
     *
     * <p>
     * The returned supplier is thread-safe. The delegate's {@code get()} method will be invoked at most once unless the
     * underlying {@code get()} throws an exception
     *
     * @param delegate
     *            Supplier which will provide values when a valid cached value has not been stored
     * @return A supplier which caches the instance retrieved during the first call to {@code get()} and returns that
     *         value on subsequent calls
     * @since 0.1.0
     */
    public static <T> Supplier<T> memoize(Supplier<T> delegate) {
        return new MemoizingSupplier<>(delegate);
    }

    /**
     * Returns a supplier that caches the instance supplied by the delegate and removes the cached value after the
     * specified time has passed. Subsequent calls to {@code get()} return the cached value if the expiration time has
     * not passed. After the expiration time, a new value is retrieved, cached, and returned
     *
     * <p>
     * The returned supplier is thread-safe
     *
     * @param delegate
     *            Supplier which will provide values when a valid cached value has not been stored or has expired
     * @param duration
     *            the length of time after a value is created that it should stop being returned by subsequent
     *            {@code get()} calls
     * @param unit
     *            the unit that {@code duration} is expressed in
     * @return A supplier that caches the instance supplied by the delegate and removes the cached value after the
     *         specified time has passed
     * @throws IllegalArgumentException
     *             if {@code duration} is not positive
     * @since 0.1.0
     */
    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
        return new MemoizingWithExpirationSupplier<>(delegate, duration, unit);
    }

    /**
     * @param delegate
     *            Supplier to create a thread-safe wrapper around
     * @return A supplier which synchronizes on the {@code delegate} before calling its {@code get} method, making it
     *         thread-safe
     * @since 0.1.0
     */
    public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
        return new SynchronizedSupplier<>(delegate);
    }

    private static final class MappingSupplier<S, T> implements Supplier<T>, Serializable {

        private static final long serialVersionUID = 0L;

        private final Supplier<S> supplier;

        private final Function<S, T> function;

        public MappingSupplier(Supplier<S> supplier, Function<S, T> function) {
            this.supplier = Objects.requireNonNull(supplier);
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public T get() {
            return function.apply(supplier.get());
        }

        @Override
        public int hashCode() {
            return Objects.hash(supplier, function);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            boolean result = false;

            if (obj instanceof MappingSupplier) {
                MappingSupplier<?, ?> compare = (MappingSupplier<?, ?>) obj;

                result = Objects.equals(compare.supplier, supplier)
                        && Objects.equals(compare.function, function);
            }

            return result;
        }

        @Override
        public String toString() {
            return "Suppliers.map(" + supplier + ", " + function + ")";
        }

    }

    private static final class MemoizingSupplier<T> implements Supplier<T>, Serializable {

        private static final long serialVersionUID = 0L;

        private final Supplier<T> delegate;

        private transient volatile boolean initialized;

        private transient T cached;

        public MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public T get() {
            if (!initialized) {
                synchronized (this) {
                    if (!initialized) {
                        cached = delegate.get();
                        initialized = true;
                    }
                }
            }

            return cached;
        }

        @Override
        public int hashCode() {
            return Objects.hash(delegate);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            boolean result = false;

            if (obj instanceof MemoizingSupplier) {
                MemoizingSupplier<?> compare = (MemoizingSupplier<?>) obj;

                result = Objects.equals(compare.delegate, delegate);
            }

            return result;
        }

        @Override
        public String toString() {
            return "Suppliers.memoize(" + delegate + ")";
        }

    }

    private static class MemoizingWithExpirationSupplier<T> implements Supplier<T>, Serializable {

        private static final long serialVersionUID = 0L;

        private final Supplier<T> delegate;

        private final long durationNanos;

        private transient volatile boolean initialized;

        private transient volatile long expirationNanos;

        private transient T value;

        private MemoizingWithExpirationSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
            Objects.requireNonNull(unit);
            this.delegate = Objects.requireNonNull(delegate);
            this.durationNanos = unit.toNanos(duration);

            initialized = false;

            Preconditions.checkArgument(duration > 0, "Expiration duration must be greater than 0");
        }

        @Override
        public T get() {
            long now = System.nanoTime();

            if (!initialized || now - expirationNanos >= 0) {
                synchronized (this) {
                    value = delegate.get();
                    initialized = true;
                    expirationNanos = now + durationNanos;
                }
            }

            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(delegate, durationNanos);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            boolean result = false;

            if (obj instanceof MemoizingWithExpirationSupplier) {
                MemoizingWithExpirationSupplier<?> compare = (MemoizingWithExpirationSupplier<?>) obj;

                result = Objects.equals(compare.delegate, delegate)
                        && Objects.equals(compare.durationNanos, durationNanos);
            }

            return result;
        }

        @Override
        public String toString() {
            return "Suppliers.memoizeWithExpiration(" + delegate + ", " + durationNanos + ", NANOS)";
        }

    }

    private static class SynchronizedSupplier<T> implements Supplier<T>, Serializable {

        private static final long serialVersionUID = 0L;

        private final Supplier<T> delegate;

        private SynchronizedSupplier(Supplier<T> delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public T get() {
            synchronized (delegate) {
                return delegate.get();
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(delegate);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            boolean result = false;

            if (obj instanceof SynchronizedSupplier) {
                SynchronizedSupplier<?> compare = (SynchronizedSupplier<?>) obj;

                result = Objects.equals(compare.delegate, delegate);
            }

            return result;
        }

        @Override
        public String toString() {
            return "Suppliers.synchronizedSupplier(" + delegate + ")";
        }

    }

}
