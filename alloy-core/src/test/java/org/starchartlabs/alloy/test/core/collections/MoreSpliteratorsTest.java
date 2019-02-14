/*
 * Copyright (C) 2019 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.mockito.Mockito;
import org.starchartlabs.alloy.core.collections.MoreSpliterators;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MoreSpliteratorsTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void shortCircuitNullSpliterator() throws Exception {
        MoreSpliterators.shortCircuit(null, this::accumulate, a -> a == 0);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shortCircuitNullAccumulator() throws Exception {
        MoreSpliterators.shortCircuit(Spliterators.spliterator(new int[] { 1, 2, 3 }, 0), null, a -> (int) a == 0);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shortCircuitNullPredicate() throws Exception {
        MoreSpliterators.shortCircuit(Spliterators.spliterator(new int[] { 1, 2, 3 }, 0), this::accumulate, null);
    }

    @Test
    public void shortCircuitConditionNeverMet() throws Exception {
        Integer[] input = new Integer[] { 1, 2, 3 };
        Spliterator<Integer> spliterator = Spliterators.spliterator(input, 0);

        Spliterator<Integer> result = MoreSpliterators.shortCircuit(spliterator, this::accumulate, a -> a == 0);

        Collection<Integer> traversed = StreamSupport.stream(result, false)
                .collect(Collectors.toList());

        Assert.assertEquals(traversed.size(), 3);
        Assert.assertTrue(traversed.containsAll(Arrays.asList(input)));
    }

    @Test
    public void shortCircuitConditionMet() throws Exception {
        Spliterator<Integer> spliterator = Spliterators.spliterator(new int[] { 1, 2, 0, 3 }, 0);

        Spliterator<Integer> result = MoreSpliterators.shortCircuit(spliterator, this::accumulate, a -> a == 0);

        // Validate inheritance of properties
        Assert.assertNull(result.trySplit(), "Splitting not currently supported, required addition thread safety work");
        Assert.assertEquals(spliterator.estimateSize(), result.estimateSize());

        Collection<Integer> traversed = StreamSupport.stream(result, false)
                .collect(Collectors.toList());

        // Note: traversal does not end until all values necessary to fulfill the predicate have been traversed
        Assert.assertEquals(traversed.size(), 3);
        Assert.assertTrue(traversed.containsAll(Arrays.asList(1, 2, 0)));
    }

    @Test
    public void shortCircuitSupportedCharacteristics() throws Exception {
        Spliterator<?> spliterator = Mockito.mock(Spliterator.class);
        Mockito.when(spliterator.characteristics())
        .thenReturn(Spliterator.CONCURRENT | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL
                | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SORTED | Spliterator.SUBSIZED);

        int expected = Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED
                | Spliterator.SORTED;

        int result = MoreSpliterators.shortCircuit(spliterator, this::accumulateMock, a -> a != null).characteristics();

        // Make sure of the available options, only the intended supporting ones pass through
        // Each of the pruned options is either violated by the nature of short-circuiting or requires additional work
        // to support
        Assert.assertEquals(result, expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shortCircuitInheritedComparator() throws Exception {
        Comparator<Object> expected = Mockito.mock(Comparator.class);
        Spliterator<Object> spliterator = Mockito.mock(Spliterator.class);

        Mockito.when(spliterator.getComparator()).thenReturn(expected);

        Comparator<Object> result = MoreSpliterators.shortCircuit(spliterator, this::accumulateMock, a -> a != null)
                .getComparator();

        // Passing through to the underlying spliterator allows the short-circuit implementation to support the SORTED
        // characteristic, if the underlying stream does
        Assert.assertEquals(result, expected);
    }

    private Integer accumulate(Optional<Integer> a, Integer b) {
        return a.map(c -> c * b).orElse(b);
    }

    private Object accumulateMock(Optional<Object> a, Object b) {
        return b;
    }

}
