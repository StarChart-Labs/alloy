/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 *
 * Parts of individual API and implementation are heavily based on Guava's Strings class, and fall under their Apache 2.0 copyright:
 *
 * Copyright (C) 2010 The Guava Authors
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

import javax.annotation.Nullable;

//TODO romeara migration doc
/**
 * Utilities intended to enhance and streamline use of {@code String} or {@code CharSequence} instances.
 *
 * @author romeara
 * @since 0.1.0
 */
public final class Strings {

    /**
     * Prevent instantiation of utility class
     */
    private Strings() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate instance of utility class '" + getClass().getName() + "'");
    }

    /**
     * Returns the given string if it is non-null; the empty string otherwise
     *
     * @param string
     *            The string to test and possibly return
     * @return {@code string} itself if it is non-null; {@code ""} if it is null
     */
    public static String nullToEmpty(@Nullable String string) {
        return (string == null) ? "" : string;
    }

    /**
     * Returns the given string if it is nonempty; {@code null} otherwise
     *
     * @param string
     *            The string to test and possibly return
     * @return {@code string} itself if it is nonempty; {@code null} if it is empty or null
     * @since 0.1.0
     */
    @Nullable
    public static String emptyToNull(@Nullable String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    /**
     * Returns {@code true} if the given string is null or is the empty string.
     *
     * <p>
     * Consider normalizing your string references with {@link #nullToEmpty}. If you do, you can use
     * {@link String#isEmpty()} instead of this method, and you won't need special null-safe forms of methods like
     * {@link String#toUpperCase} either. Or, if you'd like to normalize "in the other direction," converting empty
     * strings to {@code null}, you can use {@link #emptyToNull}.
     *
     * @param string
     *            A string reference to check
     * @return {@code true} if the string is null or the empty string, false otherwise
     * @since 0.1.0
     */
    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Constructs a string, of length at least {@code minLength}, consisting of {@code string} prepended with as many
     * copies of {@code padChar} as are necessary to reach that length. For example,
     *
     * <ul>
     * <li>{@code padStart("7", 3, '0')} returns {@code "007"}
     * <li>{@code padStart("2010", 3, '0')} returns {@code "2010"}
     * </ul>
     *
     * <p>
     * See {@link java.util.Formatter} for a richer set of formatting capabilities.
     *
     * @param string
     *            The string which should appear at the end of the result
     * @param minLength
     *            The minimum length the resulting string must have. Can be zero or negative, in which case the input
     *            string is always returned
     * @param padChar
     *            The character to insert at the beginning of the result until the minimum length is reached
     * @return A padded string of at least {@code minLength} characters which ends with {@code string}
     * @since 0.1.0
     */
    public static String padStart(String string, int minLength, char padChar) {
        Objects.requireNonNull(string);

        String result = string;

        if (string.length() < minLength) {
            StringBuilder sb = new StringBuilder(minLength);
            for (int i = string.length(); i < minLength; i++) {
                sb.append(padChar);
            }
            result = sb.append(string).toString();
        }

        return result;
    }

    /**
     * Constructs a string, of length at least {@code minLength}, consisting of {@code string} appended with as many
     * copies of {@code padChar} as are necessary to reach that length. For example,
     *
     * <ul>
     * <li>{@code padEnd("4.", 5, '0')} returns {@code "4.000"}
     * <li>{@code padEnd("2010", 3, '!')} returns {@code "2010"}
     * </ul>
     *
     * <p>
     * See {@link java.util.Formatter} for a richer set of formatting capabilities.
     *
     * @param string
     *            The string which should appear at the beginning of the result
     * @param minLength
     *            The minimum length the resulting string must have. Can be zero or negative, in which case the input
     *            string is always returned
     * @param padChar
     *            The character to append to the end of the result until the minimum length is reached
     * @return A padded string of at least {@code minLength} characters which starts with {@code string}
     * @since 0.1.0
     */
    public static String padEnd(String string, int minLength, char padChar) {
        Objects.requireNonNull(string);

        String result = string;

        if (string.length() < minLength) {
            StringBuilder sb = new StringBuilder(minLength);
            sb.append(string);

            for (int i = string.length(); i < minLength; i++) {
                sb.append(padChar);
            }

            result = sb.toString();
        }
        return result;
    }

    /**
     * Constructs a string consisting of a specific number of concatenated copies of an input string. For example,
     * {@code repeat("hey", 3)} returns the string {@code "heyheyhey"}.
     *
     * @param string
     *            A non-null string to repeat
     * @param count
     *            The number of times to repeat the provided string; Must be nonnegative
     * @return A string containing {@code string} repeated {@code count} times, or the empty string if {@code count} is
     *         zero
     * @throws IllegalArgumentException
     *             If {@code count} is negative
     * @since 0.1.0
     */
    public static String repeat(String string, int count) {
        Objects.requireNonNull(string);
        Preconditions.checkArgument(count >= 0, () -> format("Invalid count: %s", count));

        String result = "";

        if (count > 1) {
            // Array copying has significant performance benefits over appending via something like StringBuilder
            final int len = string.length();
            final long longSize = (long) len * (long) count;
            final int size = (int) longSize;

            if (size != longSize) {
                throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
            }

            final char[] array = new char[size];
            string.getChars(0, len, array, 0);
            int n;
            for (n = len; n < size - n; n <<= 1) {
                System.arraycopy(array, 0, array, n, n);
            }
            System.arraycopy(array, 0, array, n, size - n);

            result = new String(array);
        }

        return result;
    }

    /**
     * @param a
     *            One of two sequences to find the longest common prefix of
     * @param b
     *            One of two sequences to find the longest common prefix of
     * @return The longest string {@code prefix} such that {@code a.toString().startsWith(prefix)} and
     *         {@code b.toString().startsWith(prefix)}, without splitting surrogate pairs. If {@code a} and {@code b}
     *         have no common prefix, returns the empty string
     * @since 0.1.0
     */
    public static String commonPrefix(CharSequence a, CharSequence b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        int maxPrefixLength = Math.min(a.length(), b.length());
        int greatestCommonIndex = 0;

        while (greatestCommonIndex < maxPrefixLength && a.charAt(greatestCommonIndex) == b.charAt(greatestCommonIndex)) {
            greatestCommonIndex++;
        }

        if (validSurrogatePairAt(a, greatestCommonIndex - 1) || validSurrogatePairAt(b, greatestCommonIndex - 1)) {
            greatestCommonIndex--;
        }

        return a.subSequence(0, greatestCommonIndex).toString();
    }

    /**
     * @param a
     *            One of two sequences to find the longest common suffix of
     * @param b
     *            One of two sequences to find the longest common suffix of
     * @return The longest string {@code suffix} such that {@code a.toString().endsWith(suffix)} and
     *         {@code b.toString().endsWith(suffix)}, without splitting surrogate pairs. If {@code a} and {@code b} have
     *         no common suffix, returns empty string
     * @since 0.1.0
     */
    public static String commonSuffix(CharSequence a, CharSequence b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        int maxSuffixLength = Math.min(a.length(), b.length());
        int lowestCommonIndex = 0;

        while (lowestCommonIndex < maxSuffixLength && a.charAt(a.length() - lowestCommonIndex - 1) == b.charAt(b.length() - lowestCommonIndex - 1)) {
            lowestCommonIndex++;
        }

        if (validSurrogatePairAt(a, a.length() - lowestCommonIndex - 1) || validSurrogatePairAt(b, b.length() - lowestCommonIndex - 1)) {
            lowestCommonIndex--;
        }

        return a.subSequence(a.length() - lowestCommonIndex, a.length()).toString();
    }

    /**
     * Returns a formatted string using the specified template string and arguments. Only {@code %s} placeholder
     * substitution is supported
     *
     * <p>
     * Provided in addition to the standard Java {@code String.format(String, Object[])} for performance reasons - this
     * implementation is intended for more performance-sensitive applications
     *
     * <p>
     * It is highly recommended to use this functionality where execution is deferred, such as operations which accept a
     * {@code Supplier}
     *
     *
     * @param template
     *            A template for a formatted message. The message is formed by replacing each {@code %s} placeholder in
     *            the template with an argument. These are matched by position - the first {@code %s} gets
     *            {@code args[0]}, etc. Unmatched arguments will be appended to the formatted message in square braces.
     *            Unmatched placeholders will be left as-is.
     * @param args
     *            The arguments to be substituted into the message template. Arguments are converted to strings using
     *            {@link String#valueOf(Object)}.
     * @return A formatted message with provided arguments substituted
     * @since 0.1.0
     */
    public static String format(@Nullable String template, @Nullable Object... args) {
        template = String.valueOf(template);

        args = (args == null ? new Object[] { "(Object[])null" } : args);

        // Substitute the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;

        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }

            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);

            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // Append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");

            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }

            builder.append(']');
        }

        return builder.toString();
    }

    private static boolean validSurrogatePairAt(CharSequence string, int index) {
        return index >= 0
                && index <= (string.length() - 2)
                && Character.isHighSurrogate(string.charAt(index))
                && Character.isLowSurrogate(string.charAt(index + 1));
    }
}
