package ru.fizteh.fivt.students.preidman.CollectionsQLEx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Sources {

    @SafeVarargs
    public static <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }

    @SafeVarargs
    public static <T> Set<T> set(T... items) {
        Set<T> result = new HashSet<>();
        for (T element : items) {
            result.add(element);
        }
        return result;
    }


    public static <T> Stream<T> lines(InputStream inputStream) throws IOException, ClassCastException {
        StringBuilder builder = new StringBuilder();
        byte[] data = new byte[100];
        int readed = inputStream.read(data);
        while (readed > 0) {
            builder.append(data);
            readed = inputStream.read(data);
        }
        inputStream.close();
        String[] lines = builder.toString().split("[\n]");
        Stream.Builder<T> stream = Stream.builder();
        for (String line : lines) {
            stream.add((T) line);
        }
        return stream.build();
    }

    public static <T> Stream<T> lines(Path file) throws ClassCastException, IOException {
        InputStream input = new FileInputStream(file.toFile());
        return lines(input);
    }

}