/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API and implementation are heavily based on Guava's Throwables class, and fall under their Apache 2.0 copyright:
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Utilities intended to enhance and streamline use of {@code Throwable}
 *
 * @author romeara
 * @since 0.2.0
 */
public final class Throwables {

    /**
     * Prevent instantiation of utility class
     */
    private Throwables() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate instance of utility class '" + getClass().getName() + "'");
    }

    /**
     * Throws {@code throwable} if it is an instance of {@code declaredType}
     *
     * @param throwable
     *            The throwable to type check and throw if an instance of specified type
     * @param declaredType
     *            The type to check against the provided throwable
     * @param <X>
     *            The type to check against the provided throwable
     * @throws X
     *             If the provided {@code throwable} is an instance of {@code X}
     * @since 0.2.0
     */
    public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType) throws X {
        Objects.requireNonNull(throwable);
        Objects.requireNonNull(declaredType);

        if (declaredType.isInstance(throwable)) {
            throw declaredType.cast(throwable);
        }
    }

    /**
     * Throws {@code throwable} if it is a {@link RuntimeException} or {@link Error}
     *
     * @param throwable
     *            The throwable to type check and throw if an instance of a runtime issue
     * @since 0.2.0
     */
    public static void throwIfUnchecked(Throwable throwable) {
        Objects.requireNonNull(throwable);

        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }

        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
    }

    /**
     * Returns the innermost cause of {@code throwable}. The first throwable in a chain provides context from when the
     * error or exception was initially detected
     *
     * <pre>
     * assertEquals("Unable to assign a customer id", Throwables.getRootCause(e).getMessage());
     * </pre>
     *
     * @param throwable
     *            The throwable to walk the cause trace of and find the root
     * @return The root of the cause chain in the provided throwable
     * @since 0.2.0
     */
    public static Throwable getRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);

        // Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
        // the slower pointer, then there's a loop.
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;

        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;

            if (throwable == slowPointer) {
                throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
            }
            if (advanceSlowPointer) {
                slowPointer = slowPointer.getCause();
            }
            advanceSlowPointer = !advanceSlowPointer; // only advance every other iteration
        }

        return throwable;
    }

    /**
     * Gets a {@code Throwable} cause chain as a list. The first entry in the list will be {@code
     * throwable} followed by its cause hierarchy. Note that this is a snapshot of the cause chain and will not reflect
     * any subsequent changes to the cause chain.
     *
     * @param throwable
     *            The {@code Throwable} to extract causes from
     * @return An unmodifiable list containing the cause chain starting with {@code throwable}
     * @since 0.2.0
     */
    public static List<Throwable> getCausalChain(Throwable throwable) {
        Objects.requireNonNull(throwable);

        List<Throwable> causes = new ArrayList<>(4);
        causes.add(throwable);

        // Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
        // the slower pointer, then there's a loop.
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;

        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
            causes.add(throwable);

            if (throwable == slowPointer) {
                throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
            }
            if (advanceSlowPointer) {
                slowPointer = slowPointer.getCause();
            }
            advanceSlowPointer = !advanceSlowPointer; // only advance every other iteration
        }

        return Collections.unmodifiableList(causes);
    }

    /**
     * Returns a string containing the result of {@link Throwable#toString() toString()}, followed by the full,
     * recursive stack trace of {@code throwable}. Note that you probably should not be parsing the resulting string; if
     * you need programmatic access to the stack frames, call {@link Throwable#getStackTrace()}
     *
     * @param throwable
     *            Representation of the error to get stack trace information from
     * @return String representation of the call sequence which resulted in the error
     * @since 0.2.0
     */
    public static String getStackTraceAsString(Throwable throwable) {
        Objects.requireNonNull(throwable);

        StringWriter stringWriter = new StringWriter();

        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
