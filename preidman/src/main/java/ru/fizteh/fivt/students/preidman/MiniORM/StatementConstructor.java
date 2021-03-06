package ru.fizteh.fivt.students.preidman.MiniORM;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class StatementConstructor<T> {

    private String name = null;

    private List<TColumn> columnList = null;
    private TColumn primaryKey = null;

    private Class itemsClass;

    public StatementConstructor(String newName,
                               List<TColumn> newColumnList,
                               TColumn newPrimaryKey,
                               Class newItemsClass) {
        this.name = newName;
        this.columnList = newColumnList;
        this.primaryKey = newPrimaryKey;
        this.itemsClass = newItemsClass;
    }

    public final String buildCreate() {

        StringBuilder createQuery = new StringBuilder();

        createQuery.append("CREATE TABLE IF NOT EXISTS ")
                .append(name)
                .append(" (");

        List<String> columns = new ArrayList<>();

        for (TColumn column : columnList) {

            StringBuilder columnsBuilder = new StringBuilder();

            columnsBuilder.append(column.getName())
                    .append(" ")
                    .append(column.getType());

            if (column == primaryKey) columnsBuilder.append(" NOT NULL");

            columns.add(columnsBuilder.toString());

        }

        createQuery.append(columns.stream().collect(joining(", "))).append(")");

        return createQuery.toString();
    }

    public final String buildInsert(T newItem) {

        StringBuilder insertQuery = new StringBuilder();

        insertQuery.append("INSERT INTO ")
                .append(name)
                .append(" VALUES (");

        List<String> columns = new ArrayList<>();

        for (TColumn column : columnList) {

            Field field = column.getField();
            field.setAccessible(true);

            try {

                columns.add(DatabaseServiceUtils.getSqlValue(field.get(newItem)));

            } catch (IllegalAccessException e) {

                e.printStackTrace();

            }
        }

        insertQuery.append(columns.stream().collect(joining(", "))).append(")");

        return insertQuery.toString();
    }

    public final String buildUpdate(T item) {

        StringBuilder updateStatement = new StringBuilder();

        updateStatement.append("UPDATE ")
                .append(name)
                .append(" SET ");

        List<String> columns = new ArrayList<>();

        for (TColumn column : columnList) {

            StringBuilder columnBuilder = new StringBuilder();
            columnBuilder.append(column.getName())
                    .append("=");

            Field field = column.getField();
            field.setAccessible(true);

            try {

                columnBuilder.append(DatabaseServiceUtils.getSqlValue(field.get(item)));

            } catch (IllegalAccessException e) {

                e.printStackTrace();

            }
            columns.add(columnBuilder.toString());
        }

        updateStatement.append(columns.stream().collect(joining(", ")));

        try {

            updateStatement.append(" WHERE ")
                    .append(primaryKey.getName())
                    .append("=")
                    .append(DatabaseServiceUtils.getSqlValue(primaryKey.getField().get(item)));

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }
        
        return updateStatement.toString();
    }
}