/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core;

import org.starchartlabs.alloy.core.Strings;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StringsTest {

    @Test
    public void nullToEmptyNull() throws Exception {
        String result = Strings.nullToEmpty(null);

        Assert.assertEquals(result, "");
    }

    @Test
    public void nullToEmptyEmpty() throws Exception {
        String result = Strings.nullToEmpty("");

        Assert.assertEquals(result, "");
    }

    @Test
    public void nullToEmptyNeither() throws Exception {
        String result = Strings.nullToEmpty("string");

        Assert.assertEquals(result, "string");
    }

    @Test
    public void emptyToNullNull() throws Exception {
        String result = Strings.emptyToNull(null);

        Assert.assertEquals(result, null);
    }

    @Test
    public void emptyToNullEmpty() throws Exception {
        String result = Strings.emptyToNull("");

        Assert.assertEquals(result, null);
    }

    @Test
    public void emptyToNullNeither() throws Exception {
        String result = Strings.emptyToNull("string");

        Assert.assertEquals(result, "string");
    }

    @Test
    public void isNullOrEmptyNull() throws Exception {
        boolean result = Strings.isNullOrEmpty(null);

        Assert.assertTrue(result);
    }

    @Test
    public void isNullOrEmptyEmpty() throws Exception {
        boolean result = Strings.isNullOrEmpty("");

        Assert.assertTrue(result);
    }

    @Test
    public void isNullOrEmpty() throws Exception {
        boolean result = Strings.isNullOrEmpty("string");

        Assert.assertFalse(result);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void padStartNullString() throws Exception {
        Strings.padStart(null, 10, 'a');
    }

    @Test
    public void padStartStringLongerThanMinimum() throws Exception {
        String result = Strings.padStart("string", 2, 'a');

        Assert.assertEquals(result, "string");
    }

    @Test
    public void padStart() throws Exception {
        String result = Strings.padStart("string", 10, 'a');

        Assert.assertEquals(result, "aaaastring");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void padEndNullString() throws Exception {
        Strings.padEnd(null, 10, 'a');
    }

    @Test
    public void padEndStringLongerThanMinimum() throws Exception {
        String result = Strings.padEnd("string", 2, 'a');

        Assert.assertEquals(result, "string");
    }

    @Test
    public void padEnd() throws Exception {
        String result = Strings.padEnd("string", 10, 'a');

        Assert.assertEquals(result, "stringaaaa");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void repeatNullString() throws Exception {
        Strings.repeat(null, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Invalid count: -1")
    public void repeatNegativeCount() throws Exception {
        Strings.repeat("string", -1);
    }

    @Test
    public void repeatZeroCount() throws Exception {
        String result = Strings.repeat("string", 0);

        Assert.assertEquals(result, "");
    }

    @Test
    public void repeat() throws Exception {
        String result = Strings.repeat("string", 5);

        Assert.assertEquals(result, "stringstringstringstringstring");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void commonPrefixNullA() throws Exception {
        Strings.commonPrefix(null, "com.starchartlabs.b");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void commonPrefixNullB() throws Exception {
        Strings.commonPrefix("com.starchartlabs.a", null);
    }

    @Test
    public void commonPrefixNoCommon() throws Exception {
        String result = Strings.commonPrefix("com.starchartlabs.a", "nope");

        Assert.assertEquals(result, "");
    }

    @Test
    public void commonPrefix() throws Exception {
        String result = Strings.commonPrefix("com.starchartlabs.a", "com.google.b");

        Assert.assertEquals(result, "com.");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void commonSuffixNullA() throws Exception {
        Strings.commonSuffix(null, "b.starchartlabs.com");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void commonSuffixNullB() throws Exception {
        Strings.commonSuffix("a.starchartlabs.com", null);
    }

    @Test
    public void commonSuffixNoCommon() throws Exception {
        String result = Strings.commonSuffix("a.starchartlabs.com", "nope");

        Assert.assertEquals(result, "");
    }

    @Test
    public void commonSuffix() throws Exception {
        String result = Strings.commonSuffix("a.starchartlabs.com", "b.google.com");

        Assert.assertEquals(result, ".com");
    }

    @Test
    public void formatNullTemplate() throws Exception {
        String result = Strings.format(null, "a");

        Assert.assertEquals(result, "null [a]");
    }

    @Test
    public void formatNullArgs() throws Exception {
        String result = Strings.format("%s", (Object[]) null);

        Assert.assertEquals(result, "(Object[])null");
    }

    @Test
    public void formatFewerArgsThanPlaceholders() throws Exception {
        String result = Strings.format("%s, %s", "a");

        Assert.assertEquals(result, "a, %s");
    }

    @Test
    public void formatMoreArgsThanPlaceholders() throws Exception {
        String result = Strings.format("%s, %s", "a", "b", "c", "d");

        Assert.assertEquals(result, "a, b [c, d]");
    }

    @Test
    public void formatMatchingArgsAndPlaceholders() throws Exception {
        String result = Strings.format("%s, %s", "a", "b");

        Assert.assertEquals(result, "a, b");
    }

}
