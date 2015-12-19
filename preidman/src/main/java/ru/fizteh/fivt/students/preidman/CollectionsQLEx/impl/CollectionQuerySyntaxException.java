package ru.fizteh.fivt.students.preidman.CollectionsQLEx.impl;

public class CollectionQuerySyntaxException extends Exception {

    CollectionQuerySyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    CollectionQuerySyntaxException(String message) {
        super(message);
    }
}