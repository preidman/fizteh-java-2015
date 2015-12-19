package ru.fizteh.fivt.students.preidman.CollectionsQLEx;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


public class Aggregates {

    public interface Aggregate<C, T> extends Function<C, T> {
        T forGroup(Set<C> set);
    }

    private static <C, T> Collection<T> getValues(Collection<C> set, Function<C, T> expression) {
        Set<T> values = new HashSet<>();
        for (C element : set) {
            values.add(expression.apply(element));
        }
        return values;
    }

    public static <C, T extends Comparable<T>> Aggregate<C, T> max(Function<C, T> expression) {
        return new Aggregate<C, T>() {

            @Override
            public T apply(C element) {
                return (T) expression.apply(element);
            }

            @Override
            public T forGroup(Set<C> set) throws ClassCastException, NoSuchElementException {
                return Collections.max(getValues(set, expression));
            }
        };
    }

    public static <C, T extends Comparable<T>> Aggregate<C, T> min(Function<C, T> expression) {
        return new Aggregate<C, T>() {

            @Override
            public T apply(C element) {
                return (T) expression.apply(element);
            }

            @Override
            public T forGroup(Set<C> set) throws ClassCastException, NoSuchElementException {
                return Collections.min(getValues(set, expression));
            }
        };
    }

    public static <C, T extends Comparable<T>> Aggregate<C, Long> count(Function<C, T> expression) {
        return new Aggregate<C, Long>() {

            @Override
            public Long apply(C element) {
                return Long.valueOf(1);
            }

            @Override
            public Long forGroup(Set<C> set) {
                return (long) set.size();
            }
        };
    }

    public static <C, T extends Number & Comparable<T>> Aggregate<C, T> avg(Function<C, T> expression) {
        return new Aggregate<C, T>() {

            @Override
            public T apply(C element) {
                return expression.apply(element);
            }

            @Override
            public T forGroup(Set<C> set) throws ClassCastException, NoSuchElementException {
                if (set.isEmpty()) {
                    throw new NoSuchElementException("empt");
                }
                T sample = expression.apply(set.iterator().next());
                if (sample instanceof Long || sample instanceof Integer || sample instanceof Short) {
                    long average = 0;
                    for (C element : set) {
                        average += (Long) (expression.apply(element));
                    }
                    return (T) Long.valueOf(average / set.size());
                } else if (sample instanceof Float || sample instanceof Double) {
                    double average = 0;
                    for (C element : set) {
                        average += (Double) (expression.apply(element));
                    }
                    return (T) Double.valueOf(average / set.size());
                }
                throw new ClassCastException("wrong type");
            }
        };
    }

}