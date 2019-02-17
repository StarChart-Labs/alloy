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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.mockito.Mockito;
import org.starchartlabs.alloy.core.collections.MoreSpliterators;
import org.starchartlabs.alloy.core.collections.PageIterator;
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

        try {
            int expected = Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED
                    | Spliterator.SORTED;

            int result = MoreSpliterators.shortCircuit(spliterator, this::accumulateMock, a -> a != null).characteristics();

            // Make sure of the available options, only the intended supporting ones pass through
            // Each of the pruned options is either violated by the nature of short-circuiting or requires additional work
            // to support
            Assert.assertEquals(result, expected);
        } finally {
            Mockito.verify(spliterator).characteristics();
            Mockito.verifyNoMoreInteractions(spliterator);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shortCircuitInheritedComparator() throws Exception {
        Comparator<Object> expected = Mockito.mock(Comparator.class);
        Spliterator<Object> spliterator = Mockito.mock(Spliterator.class);

        Mockito.when(spliterator.getComparator()).thenReturn(expected);

        try {
            Comparator<Object> result = MoreSpliterators.shortCircuit(spliterator, this::accumulateMock, a -> a != null)
                    .getComparator();

            // Passing through to the underlying spliterator allows the short-circuit implementation to support the
            // SORTED characteristic, if the underlying stream does
            Assert.assertEquals(result, expected);
        } finally {
            Mockito.verify(spliterator).getComparator();
            Mockito.verifyNoMoreInteractions(spliterator);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void ofPagedNullPageProvider() throws Exception {
        MoreSpliterators.ofPaged(null);
    }

    @Test
    public void ofPagedNoPages() throws Exception {
        PageIterator<String> pageIterator = new TestPageIterator();

        Spliterator<String> result = MoreSpliterators.ofPaged(pageIterator);

        Assert.assertNotNull(result);
        List<String> values = StreamSupport.stream(result, false)
                .collect(Collectors.toList());

        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void ofPagedSinglePage() throws Exception {
        PageIterator<String> pageIterator = new TestPageIterator(Arrays.asList("1", "2"));

        Spliterator<String> result = MoreSpliterators.ofPaged(pageIterator);

        Assert.assertNotNull(result);
        List<String> values = StreamSupport.stream(result, false)
                .collect(Collectors.toList());

        Assert.assertEquals(values.size(), 2);
        Assert.assertTrue(values.contains("1"));
        Assert.assertTrue(values.contains("2"));
    }

    @Test
    public void ofPagedMultiplePages() throws Exception {
        PageIterator<String> pageIterator = new TestPageIterator(Arrays.asList("1", "2"), Arrays.asList("3", "4"));

        Spliterator<String> result = MoreSpliterators.ofPaged(pageIterator);

        Assert.assertNotNull(result);
        List<String> values = StreamSupport.stream(result, false)
                .collect(Collectors.toList());

        Assert.assertEquals(values.size(), 4);
        Assert.assertTrue(values.contains("1"));
        Assert.assertTrue(values.contains("2"));
        Assert.assertTrue(values.contains("3"));
        Assert.assertTrue(values.contains("4"));
    }

    @Test
    public void ofPagedTrySplitNotSupported() throws Exception {
        PageIterator<?> pageIterator = Mockito.mock(PageIterator.class);
        Mockito.when(pageIterator.trySplit()).thenReturn(null);

        try {
            Spliterator<?> spliterator = MoreSpliterators.ofPaged(pageIterator);

            Spliterator<?> result = spliterator.trySplit();
            Assert.assertNull(result);
        } finally {
            Mockito.verify(pageIterator).trySplit();
            Mockito.verifyNoMoreInteractions(pageIterator);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void ofPagedTrySplit() throws Exception {
        PageIterator<?> pageIterator = Mockito.mock(PageIterator.class);
        Mockito.when(pageIterator.trySplit()).thenReturn(Mockito.mock(PageIterator.class));

        try {
            Spliterator<?> spliterator = MoreSpliterators.ofPaged(pageIterator);

            Spliterator<?> result = spliterator.trySplit();
            Assert.assertNotNull(result);
        } finally {
            Mockito.verify(pageIterator).trySplit();
            Mockito.verifyNoMoreInteractions(pageIterator);
        }
    }

    @Test
    public void ofPagedEstimateSize() throws Exception {
        long expected = 2L;
        PageIterator<?> pageIterator = Mockito.mock(PageIterator.class);
        Mockito.when(pageIterator.estimateSize()).thenReturn(expected);

        try {
            Spliterator<?> spliterator = MoreSpliterators.ofPaged(pageIterator);

            long result = spliterator.estimateSize();

            Assert.assertEquals(result, expected);
        } finally {
            Mockito.verify(pageIterator).estimateSize();
            Mockito.verifyNoMoreInteractions(pageIterator);
        }
    }

    @Test
    public void ofPagedEstimateSizeAddElements() throws Exception {
        PageIterator<?> pageIterator = new TestPageIterator(true, Arrays.asList("1", "2", "3", "4"),
                Arrays.asList("22", "33"));

        Spliterator<?> spliterator = MoreSpliterators.ofPaged(pageIterator);

        // Eat the first value to load elements into the cache
        spliterator.tryAdvance(a -> {
        });
        long result = spliterator.estimateSize();

        Assert.assertEquals(result, 5, "Expected result of 3 remaining from first page and two from second page");

    }

    @Test
    public void ofPagedEstimateSizeNoOverflow() throws Exception {
        PageIterator<?> pageIterator = new TestPageIterator(false, Arrays.asList("1", "2", "3", "4"),
                Arrays.asList("22", "33"));

        Spliterator<?> spliterator = MoreSpliterators.ofPaged(pageIterator);

        // Eat the first value to load elements into the cache
        spliterator.tryAdvance(a -> {
        });
        long result = spliterator.estimateSize();

        Assert.assertEquals(result, Long.MAX_VALUE,
                "Expected page iterator estimate of MAX to be ceiling for estimate");
    }

    @Test
    public void ofPagedCharacteristics() throws Exception {
        PageIterator<?> pageIterator = Mockito.mock(PageIterator.class);

        try {
            Spliterator<?> spliterator = MoreSpliterators.ofPaged(pageIterator);

            int result = spliterator.characteristics();

            Assert.assertEquals(result, Spliterator.IMMUTABLE | Spliterator.ORDERED);
        } finally {
            Mockito.verifyNoMoreInteractions(pageIterator);
        }
    }

    private Integer accumulate(Optional<Integer> a, Integer b) {
        return a.map(c -> c * b).orElse(b);
    }

    private Object accumulateMock(Optional<Object> a, Object b) {
        return b;
    }

    private static class TestPageIterator implements PageIterator<String> {

        private final LinkedList<Collection<String>> pages;

        private final boolean estimateSize;

        @SafeVarargs
        public TestPageIterator(Collection<String>... pages) {
            this(false, pages);
        }

        @SafeVarargs
        public TestPageIterator(boolean estimateSize, Collection<String>... pages) {
            this.pages = new LinkedList<>(Arrays.asList(pages));
            this.estimateSize = estimateSize;
        }

        @Override
        public boolean hasNext() {
            return !pages.isEmpty();
        }

        @Override
        public Collection<String> next() {
            return pages.pollFirst();
        }

        @Override
        public long estimateSize() {
            return (estimateSize ? getEstimatedSize() : Long.MAX_VALUE);
        }

        @Override
        public PageIterator<String> trySplit() {
            return null;
        }

        private long getEstimatedSize() {
            return pages.stream()
                    .map(Collection::size)
                    .collect(Collectors.summingLong(a -> a));
        }

    }

}
