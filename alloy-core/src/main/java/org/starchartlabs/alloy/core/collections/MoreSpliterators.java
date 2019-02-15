/*
 * Copyright (C) 2019 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.core.collections;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Provides methods for streamlining and reducing boilerplate for operations related to Java {@link Spliterator}s
 *
 * @author romeara
 * @since 0.4.0
 */
public final class MoreSpliterators {

    /**
     * Returns a spliterator which allows stopping traversal when the previously traversed elements meet criteria
     * specified by a predicate. If the predicate condition is never met, the spliterator traverses as normal
     *
     * <p>
     * The returned spliterator will have a sub-set of the characteristics of the original
     *
     * @param spliterator
     *            Original spliterator which traverses elements until complete
     * @param accumulator
     *            Function which accepts a representation of previously traversed elements and the current element. If
     *            no elements were previously traversed, the provided previous value will be an empty Optional. Must
     *            return non-null
     * @param completedPredicate
     *            Predicate defining the conditions under which to cease traversal of the spliterator
     * @param <T>
     *            Representation supplied by the provided {@code spliterator}
     * @param <A>
     *            Representation of accumulated data from previously traversed rows
     * @return A spliterator which allows short-circuiting traversal based on a predicate
     * @since 0.4.0
     */
    public static <T, A> Spliterator<T> shortCircuit(Spliterator<T> spliterator,
            BiFunction<Optional<A>, ? super T, A> accumulator, Predicate<A> completedPredicate) {
        return new ShortCircuitSpliterator<>(spliterator, accumulator, completedPredicate);
    }

    /**
     * Returns a spliterator which allows traversing over elements provided via a paging protocol
     *
     * @param pageProvider
     *            A source-specific implementation which handles the protocol for reading a sequence of paged data
     * @param <T>
     *            Representation of a single element in the sequence
     * @return A spliterator which allows traversing over elements provided via a paging protocol
     * @since 0.4.0
     */
    public static <T> Spliterator<T> ofPaged(PageProvider<T> pageProvider) {
        return new PageSpliterator<>(pageProvider);
    }

    private static class ShortCircuitSpliterator<T, A> implements Spliterator<T> {

        private static final int SUPPORTED_CHARACTERISTICS = Spliterator.DISTINCT | Spliterator.IMMUTABLE
                | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED;

        private final Spliterator<T> spliterator;

        private final Predicate<A> completedPredicate;

        private final AccumulatingConsumer<T, A> consumer;

        public ShortCircuitSpliterator(Spliterator<T> spliterator, BiFunction<Optional<A>, ? super T, A> accumulator,
                Predicate<A> completedPredicate) {
            this.spliterator = Objects.requireNonNull(spliterator);
            this.completedPredicate = Objects.requireNonNull(completedPredicate);
            this.consumer = new AccumulatingConsumer<>(accumulator);
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            boolean complete = consumer.getAccumulated()
                    .map(completedPredicate::test)
                    .orElse(false);

            if (!complete) {
                complete = !spliterator.tryAdvance(input -> consumer.accept(action, input));
            }

            return !complete;
        }

        @Override
        public Spliterator<T> trySplit() {
            // TODO romeara Not sure there is a good way to safely split this for parallel operation - for now, stick to
            // the safer option
            return null;
        }

        @Override
        public long estimateSize() {
            // There is no way to know when the condition will be met, so present the worst-case scenario (of "never
            // short circuited") according to the underlying spliterator
            return spliterator.estimateSize();
        }

        @Override
        public int characteristics() {
            // Certain characteristic behaviors do not persist once short-circuiting is applied
            // For example, SIZED can no longer be guaranteed, as the traversal may complete before iterating over all
            // underlying values
            return SUPPORTED_CHARACTERISTICS & spliterator.characteristics();
        }

        // Expose information available in the supporting spliterator, allowing the "SORTED" characteristic to be
        // supported if provided by the supporting spliterator
        @Override
        public Comparator<? super T> getComparator() {
            return spliterator.getComparator();
        }

        private static class AccumulatingConsumer<T, A> {

            private final BiFunction<Optional<A>, ? super T, A> accumulator;

            private Optional<A> accumulated;

            public AccumulatingConsumer(BiFunction<Optional<A>, ? super T, A> accumulator) {
                this.accumulator = Objects.requireNonNull(accumulator);
                this.accumulated = Optional.empty();
            }

            public void accept(Consumer<? super T> action, T input) {
                action.accept(input);
                accumulated = Optional.of(accumulator.apply(accumulated, input));
            }

            public Optional<A> getAccumulated() {
                return accumulated;
            }

        }

    }

    private static class PageSpliterator<T> implements Spliterator<T> {

        private final PageProvider<T> pageProvider;

        private LinkedList<T> elements;

        public PageSpliterator(PageProvider<T> pageProvider) {
            this.pageProvider = Objects.requireNonNull(pageProvider);
            elements = new LinkedList<>();
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            // Populate next set of elements, if possible
            if (elements.isEmpty() && pageProvider.hasNext()) {
                elements.addAll(pageProvider.next());
            }

            T element = elements.poll();

            if (element != null) {
                action.accept(element);
            }

            return (element != null);
        }

        @Override
        public Spliterator<T> trySplit() {
            return Optional.ofNullable(pageProvider.trySplit())
                    .map(a -> new PageSpliterator<T>(a))
                    .orElse(null);
        }

        @Override
        public long estimateSize() {
            return pageProvider.estimateSize();
        }

        @Override
        public int characteristics() {
            // This implementation cannot know what properties various paging elements support, aside from not being
            // modifiable and having some form of ordering (without an ordering, stable paging would not be possible)
            return Spliterator.IMMUTABLE | Spliterator.ORDERED;
        }

    }
}
