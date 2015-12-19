package ru.fizteh.fivt.students.preidman.CollectionsQLEx.impl;

import java.util.Collection;

public class UnionStmt<R> {
    private Collection<R> previousPart;
    private Class<R> previousOutputClass;

    UnionStmt(SelectStmt<?, R> previousStmt) throws CollectionQueryExecuteException {
        previousOutputClass = previousStmt.getOutputClass();
        previousPart = previousStmt.execute();
    }

    public final <T> FromStmt<T> from(Iterable<T> list) {
        FromStmt<T> stmt = FromStmt.from(list);
        stmt.setPreviousPart(previousOutputClass, previousPart);
        return stmt;
    }
}
