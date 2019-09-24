/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API are heavily based on Guava's Preconditions class, and fall under their Apache 2.0 copyright:
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

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

/**
 * Utilities intended to streamline verification of expected input conditions by callers.
 *
 * <p>
 * In general, these utilities allow code of the form
 *
 * <pre>
 * if (!expression) {
 *     throw new SomeException();
 * }
 * </pre>
 *
 * to be replaced with
 *
 * <pre>
 * Preconditions.check*(expression);
 * </pre>
 *
 * for compactness, behavioral clarity, and readability
 *
 * <p>
 * It is highly recommended to constrain use of these utilities to verifying possible failure conditions which are the
 * caller's fault, as otherwise behavior may be confusing in stack traces, and to future readers of caller
 * implementations
 *
 * <p>
 * For failure messages which may have non-trivial performance costs to create, it is highly recommended to use method
 * forms which accept a {@link Supplier}
 *
 * @author romeara
 * @since 0.1.0
 */
public final class Preconditions {

    /**
     * Prevent instantiation of utility class
     */
    private Preconditions() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate instance of utility class '" + getClass().getName() + "'");
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression
     *            A boolean expression
     * @throws IllegalArgumentException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalArgumentException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkArgument(@Nullable T value, Predicate<T> predicate) {
        Objects.requireNonNull(predicate);

        if (!predicate.test(value)) {
            throw new IllegalArgumentException();
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression
     *            A boolean expression
     * @param errorMessage
     *            The message to provide in the exception, if the check fails; will be converted via
     *            {@link String#valueOf}
     * @throws IllegalArgumentException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param errorMessage
     *            The message to provide in the exception, if the check fails; will be converted via
     *            {@link String#valueOf}
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalArgumentException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkArgument(@Nullable T value, Predicate<T> predicate, @Nullable Object errorMessage) {
        Objects.requireNonNull(predicate);

        if (!predicate.test(value)) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * <p>
     * Preferred to {@link #checkArgument(boolean, Object)} when elements needs to be substituted or otherwise processed
     * for performance reasons
     *
     * @param expression
     *            A boolean expression
     * @param errorMessageSupplier
     *            A supplier for the message to provide in the exception, if the check fails; Will not be invoked if
     *            {@code expression} is true
     * @throws IllegalArgumentException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkArgument(boolean expression, Supplier<String> errorMessageSupplier) {
        Objects.requireNonNull(errorMessageSupplier);

        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessageSupplier.get()));
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * <p>
     * Preferred to {@link #checkArgument(Object, Predicate, Object)} when elements needs to be substituted or otherwise
     * processed for performance reasons
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param errorMessageSupplier
     *            A supplier for the message to provide in the exception, if the check fails; Will not be invoked if
     *            {@code predicate} is true
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalArgumentException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkArgument(@Nullable T value, Predicate<T> predicate,
            Supplier<String> errorMessageSupplier) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(errorMessageSupplier);

        if (!predicate.test(value)) {
            throw new IllegalArgumentException(String.valueOf(errorMessageSupplier.get()));
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * <p>
     * Preferred to {@link #checkArgument(boolean, Object)} when elements needs to be substituted or otherwise processed
     * for performance reasons
     *
     * @param expression
     *            A boolean expression
     * @param template
     *            A template for a formatted message. The message is formed by replacing each {@code %s} placeholder in
     *            the template with an argument. These are matched by position - the first {@code %s} gets
     *            {@code args[0]}, etc. Unmatched arguments will be appended to the formatted message in square braces.
     *            Unmatched placeholders will be left as-is.
     * @param args
     *            The arguments to be substituted into the message template. Arguments are converted to strings using
     *            {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkArgument(boolean expression, @Nullable String template, @Nullable Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(Strings.format(template, args)));
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * <p>
     * Preferred to {@link #checkArgument(Object, Predicate, Object)} when elements needs to be substituted or otherwise
     * processed for performance reasons
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param template
     *            A template for a formatted message. The message is formed by replacing each {@code %s} placeholder in
     *            the template with an argument. These are matched by position - the first {@code %s} gets
     *            {@code args[0]}, etc. Unmatched arguments will be appended to the formatted message in square braces.
     *            Unmatched placeholders will be left as-is.
     * @param args
     *            The arguments to be substituted into the message template. Arguments are converted to strings using
     *            {@link String#valueOf(Object)}.
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalArgumentException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkArgument(@Nullable T value, Predicate<T> predicate,
            @Nullable String template, @Nullable Object... args) {
        Objects.requireNonNull(predicate);

        if (!predicate.test(value)) {
            throw new IllegalArgumentException(Strings.format(template, args));
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression
     *            A boolean expression
     * @throws IllegalStateException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalStateException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkState(@Nullable T value, Predicate<T> predicate) {
        Objects.requireNonNull(predicate);

        if (!predicate.test(value)) {
            throw new IllegalStateException();
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression
     *            A boolean expression
     * @param errorMessage
     *            The message to provide in the exception, if the check fails; will be converted via
     *            {@link String#valueOf}
     * @throws IllegalStateException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkState(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param errorMessage
     *            The message to provide in the exception, if the check fails; will be converted via
     *            {@link String#valueOf}
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalStateException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkState(@Nullable T value, Predicate<T> predicate, @Nullable Object errorMessage) {
        Objects.requireNonNull(predicate);

        if (!predicate.test(value)) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * <p>
     * Preferred to {@link #checkState(boolean, Object)} when elements needs to be substituted or otherwise processed
     * for performance reasons
     *
     * @param expression
     *            A boolean expression
     * @param errorMessageSupplier
     *            A supplier for the message to provide in the exception, if the check fails; Will not be invoked if
     *            {@code expression} is true
     * @throws IllegalStateException
     *             If {@code expression} is false
     * @since 0.1.0
     */
    public static void checkState(boolean expression, Supplier<String> errorMessageSupplier) {
        Objects.requireNonNull(errorMessageSupplier);

        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessageSupplier.get()));
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * <p>
     * Preferred to {@link #checkArgument(Object, Predicate, Object)} when elements needs to be substituted or otherwise
     * processed for performance reasons
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param errorMessageSupplier
     *            A supplier for the message to provide in the exception, if the check fails; Will not be invoked if
     *            {@code predicate} is true
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalStateException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkState(@Nullable T value, Predicate<T> predicate, Supplier<String> errorMessageSupplier) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(errorMessageSupplier);

        if (!predicate.test(value)) {
            throw new IllegalStateException(String.valueOf(errorMessageSupplier.get()));
        }

        return value;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * <p>
     * Preferred to {@link #checkState(boolean, Object)} when elements needs to be substituted or otherwise processed
     * for performance reasons
     *
     * @param expression
     *            A boolean expression
     * @param template
     *            A template for a formatted message. The message is formed by replacing each {@code %s} placeholder in
     *            the template with an argument. These are matched by position - the first {@code %s} gets
     *            {@code args[0]}, etc. Unmatched arguments will be appended to the formatted message in square braces.
     *            Unmatched placeholders will be left as-is.
     * @param args
     *            The arguments to be substituted into the message template. Arguments are converted to strings using
     *            {@link String#valueOf(Object)}.
     * @throws IllegalStateException
     *             If {@code expression} is false
     * @since 0.5.0
     */
    public static void checkState(boolean expression, @Nullable String template, @Nullable Object... args) {
        if (!expression) {
            throw new IllegalStateException(Strings.format(template, args));
        }
    }

    /**
     * Evaluates a value against a provided {@link Predicate} expression
     *
     * <p>
     * Preferred to {@link #checkArgument(Object, Predicate, Object)} when elements needs to be substituted or otherwise
     * processed for performance reasons
     *
     * @param value
     *            An assignable value to evaluate
     * @param predicate
     *            An operation to evaluate against {@code value}
     * @param template
     *            A template for a formatted message. The message is formed by replacing each {@code %s} placeholder in
     *            the template with an argument. These are matched by position - the first {@code %s} gets
     *            {@code args[0]}, etc. Unmatched arguments will be appended to the formatted message in square braces.
     *            Unmatched placeholders will be left as-is.
     * @param args
     *            The arguments to be substituted into the message template. Arguments are converted to strings using
     *            {@link String#valueOf(Object)}.
     * @param <T>
     *            The type of the value being evaluated
     * @return {@code value} if the provided {@code predicate} evaluates to true
     * @throws IllegalStateException
     *             If {@code predicate} evaluates to false
     * @since 0.5.0
     */
    public static <T> T checkState(@Nullable T value, Predicate<T> predicate, @Nullable String template,
            @Nullable Object... args) {
        Objects.requireNonNull(predicate);

        if (!predicate.test(value)) {
            throw new IllegalStateException(Strings.format(template, args));
        }

        return value;
    }

}
