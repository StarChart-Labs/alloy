/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API are heavily based on Guava's MoreObjects class, and fall under their Apache 2.0 copyright:
 *
 * Copyright (C) 2014 The Guava Authors
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * Provides methods for streamlining and reducing boilerplate in relation to basic {@link Object} operations
 *
 * @author romeara
 * @since 0.1.0
 */
public final class MoreObjects {

    /**
     * Prevent instantiation of utility class
     */
    private MoreObjects() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate instance of utility class '" + getClass().getName() + "'");
    }

    /**
     * Creates an instance of {@link ToStringHelper}.
     *
     * <p>
     * This is helpful for implementing {@link Object#toString()}. Example:
     *
     * <pre>
     *
     * {@code
     *   // Returns "ClassName{}"
     *   MoreObjects.toStringHelper(this)
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   MoreObjects.toStringHelper(this)
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "MyObject{x=1}"
     *   MoreObjects.toStringHelper("MyObject")
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "ClassName{x=1, y=foo}"
     *   MoreObjects.toStringHelper(this)
     *       .add("x", 1)
     *       .add("y", "foo")
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   MoreObjects.toStringHelper(this)
     *       .omitNullValues()
     *       .add("x", 1)
     *       .add("y", null)
     *       .toString();
     *   }}
     * </pre>
     *
     * @param self
     *            The object to generate the string for (typically {@code this}), used only for its class name
     * @return A builder designed for streamlined implementation of {@link Object#toString()}
     * @since 0.1.0
     */
    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }

    /**
     * Creates an instance of {@link ToStringHelper}.
     *
     * <p>
     * This is helpful for implementing {@link Object#toString()}. Example:
     *
     * <pre>
     *
     * {@code
     *   // Returns "ClassName{}"
     *   MoreObjects.toStringHelper(this)
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   MoreObjects.toStringHelper(this)
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "MyObject{x=1}"
     *   MoreObjects.toStringHelper("MyObject")
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "ClassName{x=1, y=foo}"
     *   MoreObjects.toStringHelper(this)
     *       .add("x", 1)
     *       .add("y", "foo")
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   MoreObjects.toStringHelper(this)
     *       .omitNullValues()
     *       .add("x", 1)
     *       .add("y", null)
     *       .toString();
     *   }}
     * </pre>
     *
     * @param clazz
     *            The {@link Class} of the instance, used only for its class name
     * @return A builder designed for streamlined implementation of {@link Object#toString()}
     * @since 0.1.0
     */
    public static ToStringHelper toStringHelper(Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }

    /**
     * Creates an instance of {@link ToStringHelper}.
     *
     * <p>
     * This is helpful for implementing {@link Object#toString()}. Example:
     *
     * <pre>
     *
     * {@code
     *   // Returns "ClassName{}"
     *   MoreObjects.toStringHelper(this)
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   MoreObjects.toStringHelper(this)
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "MyObject{x=1}"
     *   MoreObjects.toStringHelper("MyObject")
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "ClassName{x=1, y=foo}"
     *   MoreObjects.toStringHelper(this)
     *       .add("x", 1)
     *       .add("y", "foo")
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   MoreObjects.toStringHelper(this)
     *       .omitNullValues()
     *       .add("x", 1)
     *       .add("y", null)
     *       .toString();
     *   }}
     * </pre>
     *
     * @param className
     *            The name of the instance type
     * @return A builder designed for streamlined implementation of {@link Object#toString()}
     * @since 0.1.0
     */
    public static ToStringHelper toStringHelper(String className) {
        return new ToStringHelper(className);
    }

    /**
     * Support class for {@link MoreObjects#toStringHelper}. Builder which allows reducing boilerplate around toString
     * implementations
     *
     * @author romeara
     * @since 0.1.0
     */
    public static final class ToStringHelper {

        private final String className;

        private final List<ValueHolder> holders = new LinkedList<>();

        private boolean omitNullValues = false;

        /**
         * Use {@link MoreObjects#toStringHelper(Object)} to create an instance.
         */
        private ToStringHelper(String className) {
            this.className = Objects.requireNonNull(className);
        }

        /**
         * Configures the {@link ToStringHelper} so {@link #toString()} will ignore properties with a null value. The
         * order of calling this method, relative to the {@code add()}/{@code addValue()} methods, is not significant
         *
         * @return This builder instance, for further configuration
         * @since 0.1.0
         */
        public ToStringHelper omitNullValues() {
            omitNullValues = true;

            return this;
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value} format. If {@code value} is
         * {@code null}, the string {@code "null"} is used, unless {@link #omitNullValues()} is called, in which case
         * this name/value pair will not be added
         *
         * @param name
         *            Label for the value being added, must not be null
         * @param value
         *            The data to represent in the built string
         * @return This builder instance, for further configuration
         * @since 0.1.0
         */
        public ToStringHelper add(String name, @Nullable Object value) {
            Objects.requireNonNull(name);
            holders.add(new ValueHolder(name, value));

            return this;
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         * <p>
         * It is strongly encouraged to use {@link #add(String, Object)} instead and give value a readable name
         *
         * @param value
         *            The data to represent in the built string
         * @return This builder instance, for further configuration
         * @since 0.1.0
         */
        public ToStringHelper addValue(@Nullable Object value) {
            holders.add(new ValueHolder(null, value));

            return this;
        }

        @Override
        public String toString() {
            String values = holders.stream()
                    .filter(v -> !omitNullValues || v.isNonNull())
                    .map(ValueHolder::toString)
                    .collect(Collectors.joining(", "));

            StringBuilder builder = new StringBuilder(32).append(className).append('{')
                    .append(values)
                    .append('}');

            return builder.toString();
        }

    }

    /**
     * Holder object which allows null behavior to be determined at build-time within {@link ValueHolder}
     *
     * @author romeara
     */
    private static final class ValueHolder {

        private final String name;

        private final Object value;

        public ValueHolder(@Nullable String name, @Nullable Object value) {
            this.name = name;
            this.value = value;
        }

        /**
         * @return True if the represented value is non-null, false otherwise
         */
        public boolean isNonNull() {
            return value != null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            if (name != null) {
                builder.append(name).append('=');
            }

            if (value != null && value.getClass().isArray()) {
                Object[] objectArray = { value };
                String arrayString = Arrays.deepToString(objectArray);
                builder.append(arrayString, 1, arrayString.length() - 1);
            } else {
                builder.append(value);
            }

            return builder.toString();
        }
    }

}
