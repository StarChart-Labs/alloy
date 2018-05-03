/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.starchartlabs.alloy.core.Comparators;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ComparatorsTest {

    @DataProvider(name = "getIsInOrderCases")
    public Object[][] getIsInOrderCases() {
        return new Object[][] {
            new Object[] { Collections.emptyList(), true },
            new Object[] { Arrays.asList("value1"), true },
            new Object[] { Arrays.asList("value1", "value2"), true },
            new Object[] { Arrays.asList("value1", "value1", "value2", "value2"), true },
            new Object[] { Arrays.asList("value2", "value1"), false }
        };
    }

    @DataProvider(name = "getIsInStrictOrderCases")
    public Object[][] getIsInStrictOrderCases() {
        return new Object[][] {
            new Object[] { Collections.emptyList(), true },
            new Object[] { Arrays.asList("value1"), true },
            new Object[] { Arrays.asList("value1", "value2"), true },
            new Object[] { Arrays.asList("value1", "value1", "value2", "value2"), false },
            new Object[] { Arrays.asList("value2", "value1"), false }
        };
    }

    @DataProvider(name = "getEmptiesFirstCases")
    public Object[][] getEmptiesFirstCases() {
        return new Object[][] {
            new Object[] { Arrays.asList(null, Optional.empty()), Arrays.asList(Optional.empty(), null) },
            new Object[] { Arrays.asList(Optional.of("value"), Optional.empty()),
                    Arrays.asList(Optional.empty(), Optional.of("value")) },
            new Object[] { Arrays.asList(Optional.of("value2"), Optional.of("value1")),
                    Arrays.asList(Optional.of("value1"), Optional.of("value2")) }
        };
    }

    @DataProvider(name = "getEmptiesLastCases")
    public Object[][] getEmptiesLastCases() {
        return new Object[][] {
            new Object[] { Arrays.asList(Optional.empty(), null), Arrays.asList(null, Optional.empty()) },
            new Object[] { Arrays.asList(Optional.empty(), Optional.of("value")),
                    Arrays.asList(Optional.of("value"), Optional.empty()) },
            new Object[] { Arrays.asList(Optional.of("value2"), Optional.of("value1")),
                    Arrays.asList(Optional.of("value1"), Optional.of("value2")) }
        };
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void isInOrderNullIterable() throws Exception {
        Comparators.isInOrder(null, String.CASE_INSENSITIVE_ORDER);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void isInOrderNullComparator() throws Exception {
        Comparators.isInOrder(Collections.emptyList(), null);
    }

    @Test(dataProvider = "getIsInOrderCases")
    public void isInOrder(List<String> input, boolean expected) throws Exception {
        boolean result = Comparators.isInOrder(input, String.CASE_INSENSITIVE_ORDER);

        Assert.assertEquals(result, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void isInStrictOrderNullIterable() throws Exception {
        Comparators.isInStrictOrder(null, String.CASE_INSENSITIVE_ORDER);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void isInStrictOrderNullComparator() throws Exception {
        Comparators.isInStrictOrder(Collections.emptyList(), null);
    }

    @Test(dataProvider = "getIsInStrictOrderCases")
    public void isInStrictOrder(List<String> input, boolean expected) throws Exception {
        boolean result = Comparators.isInStrictOrder(input, String.CASE_INSENSITIVE_ORDER);

        Assert.assertEquals(result, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void emptiesFirstNullValueComparator() throws Exception {
        Comparators.emptiesFirst(null);
    }

    @Test(dataProvider = "getEmptiesFirstCases")
    public void emptiesFirst(List<Optional<String>> unsorted, List<Optional<String>> expected) throws Exception {
        Assert.assertNotEquals(unsorted, expected, "Invalid test - input to sort already matches expected result");

        List<Optional<String>> result = unsorted.stream()
                .sorted(Comparators.emptiesFirst(String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        Assert.assertEquals(result, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void emptiesLastNullValueComparator() throws Exception {
        Comparators.emptiesLast(null);
    }

    @Test(dataProvider = "getEmptiesLastCases")
    public void emptiesLast(List<Optional<String>> unsorted, List<Optional<String>> expected) throws Exception {
        Assert.assertNotEquals(unsorted, expected, "Invalid test - input to sort already matches expected result");

        List<Optional<String>> result = unsorted.stream()
                .sorted(Comparators.emptiesLast(String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        Assert.assertEquals(result, expected);
    }

}
