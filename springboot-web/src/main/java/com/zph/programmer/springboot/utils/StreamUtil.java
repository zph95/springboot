package com.zph.programmer.springboot.utils;

import org.springframework.lang.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @Author: zengpenghui
 * @Date: 2020/7/20 15:34
 * @Description:
 */
public class StreamUtil {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <T> java.util.Optional<T> findLast(Stream<T> stream) {
        class OptionalState {

            boolean set = false;
            T value = null;

            void set(@Nullable T value) {
                set = true;
                this.value = value;
            }

            T get() {
                if (!set) {
                    throw new IllegalStateException();
                }
                return value;
            }
        }
        OptionalState state = new OptionalState();

        Deque<Spliterator<T>> splits = new ArrayDeque<>();
        splits.addLast(stream.spliterator());

        while (!splits.isEmpty()) {
            Spliterator<T> spliterator = splits.removeLast();

            if (spliterator.getExactSizeIfKnown() == 0) {
                continue; // drop this split
            }

            // Many spliterators will have trySplits that are SUBSIZED even if they are not themselves
            // SUBSIZED.
            if (spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
                // we can drill down to exactly the smallest nonempty spliterator
                while (true) {
                    Spliterator<T> prefix = spliterator.trySplit();
                    if (prefix == null || prefix.getExactSizeIfKnown() == 0) {
                        break;
                    } else if (spliterator.getExactSizeIfKnown() == 0) {
                        spliterator = prefix;
                        break;
                    }
                }

                // spliterator is known to be nonempty now
                spliterator.forEachRemaining(state::set);
                return java.util.Optional.of(state.get());
            }

            Spliterator<T> prefix = spliterator.trySplit();
            if (prefix == null || prefix.getExactSizeIfKnown() == 0) {
                // we can't split this any further
                spliterator.forEachRemaining(state::set);
                if (state.set) {
                    return java.util.Optional.of(state.get());
                }
                // fall back to the last split
                continue;
            }
            splits.addLast(prefix);
            splits.addLast(spliterator);
        }
        return java.util.Optional.empty();
    }
}
