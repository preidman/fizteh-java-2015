package ru.fizteh.fivt.students.preidman.CollectionsQLEx;

import java.util.function.Function;
import java.util.function.Predicate;

public class Conditions<T> {

    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        return new Predicate<T>() {

            @Override
            public boolean test(T element) {
                return expression.apply(element).matches(regexp);
            }
        };
    }

    public static <T> Predicate<T> like(Function<T, String> expression, String pattern) {
        throw new UnsupportedOperationException();
    }

}