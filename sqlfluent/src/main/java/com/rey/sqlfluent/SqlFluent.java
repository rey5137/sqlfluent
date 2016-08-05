package com.rey.sqlfluent;

import java.util.Collection;

import static com.rey.sqlfluent.Value.*;

/**
 * Created by Rey on 6/29/2015.
 * A builder class help build SQL query in fluent style.
 */
public class SqlFluent {

    StringBuilder mBuilder;

    private static final char DOT               = '.';
    private static final char COMMA             = ',';
    private static final char QUOTE             = '\"';
    private static final char SPACE             = ' ';
    private static final char OPEN_BRACKET      = '(';
    private static final char CLOSE_BRACKET     = ')';
    private static final char SEMI_COLON        = ';';

    public interface ValueProvider<M>{
        String getValue(M m);
    }

    public SqlFluent(){
        mBuilder = new StringBuilder();
    }

    public SqlFluent(StringBuilder builder){
        mBuilder = builder;
        mBuilder.delete(0, mBuilder.length());
    }

    private SqlFluent add(String word){
        if(mBuilder.length() != 0)
            mBuilder.append(' ');
        mBuilder.append(word);
        return this;
    }

    private SqlFluent addWithoutSpace(char c){
        mBuilder.append(c);
        return this;
    }

    private SqlFluent addWithoutSpace(String c){
        mBuilder.append(c);
        return this;
    }

    //region select methods

    public SqlFluent select() {
        return add(SELECT);
    }

    public SqlFluent selectDistinct() {
        return add(SELECT_DISTINCT);
    }

    public SqlFluent selectAll() {
        return add(SELECT_ALL);
    }

    public SqlFluent selectCount(){
        return selectCount(null);
    }

    public SqlFluent selectCount(String asName){
        add(SELECT_COUNT_ALL);
        if(asName != null)
            add(AS).add(asName);
        return this;
    }

    //endregion

    //region column methods

    public SqlFluent column(String column){
        return column(null, column, null);
    }

    public SqlFluent column(String table, String column){
        return column(table, column, null);
    }

    public SqlFluent column(String table, String column, String asName){
        if(table != null)
            add(table).addWithoutSpace(DOT);
        add(column);
        if(asName != null)
            add(AS).add(asName);
        return this;
    }

    public SqlFluent andColumn(String column){
        return andColumn(null, column, null);
    }

    public SqlFluent andColumn(String table, String column){
        return andColumn(table, column, null);
    }

    public SqlFluent andColumn(String table, String column, String asName){
        addWithoutSpace(COMMA);
        return column(table, column, asName);
    }

    public SqlFluent columns(String... columns){
        if(columns == null || columns.length == 0)
            return this;
        for(int i = 0; i < columns.length; i++)
            if(i == 0)
                column(columns[i]);
            else
                andColumn(columns[i]);
        return this;
    }

    //endregion

    //region from methods

    public SqlFluent from(){
        return from(null, null);
    }

    public SqlFluent from(String table){
        return from(table, null);
    }

    public SqlFluent from(String table, String asName){
        add(FROM);
        if(table != null)
            return table(table, asName);
        return this;
    }

    public SqlFluent table(String table){
        return table(table, null);
    }

    public SqlFluent table(String table, String asName){
        add(table);
        if(asName != null)
            add(AS).add(asName);
        return this;
    }

    public SqlFluent andTable(String table){
        return andTable(table, null);
    }

    public SqlFluent andTable(String table, String asName){
        addWithoutSpace(COMMA);
        return table(table, asName);
    }

    public SqlFluent tables(String... tables){
        if(tables == null || tables.length == 0)
            return this;
        for(int i = 0; i < tables.length; i++)
            if(i == 0)
                table(tables[i]);
            else
                andTable(tables[i]);
        return this;
    }

    //endregion

    //region value methods

    public SqlFluent value(int value){
        return add(String.valueOf(value));
    }

    public SqlFluent value(long value){
        return add(String.valueOf(value));
    }

    public SqlFluent value(float value){
        return add(String.valueOf(value));
    }

    public SqlFluent value(double value){
        return add(String.valueOf(value));
    }

    public SqlFluent value(boolean value){
        return add(String.valueOf(value ? Value.TRUE : Value.FALSE));
    }

    public SqlFluent value(String value){
        return addWithoutSpace(SPACE)
                .addWithoutSpace(QUOTE)
                .addWithoutSpace(value)
                .addWithoutSpace(QUOTE);
    }

    public SqlFluent value(Object value){
        if(value instanceof Integer)
            return value(((Integer)value).intValue());
        if(value instanceof Long)
            return value(((Long)value).longValue());
        if(value instanceof Float)
            return value(((Float) value).floatValue());
        if(value instanceof Double)
            return value(((Double) value).doubleValue());
        if(value instanceof Boolean)
            return value(((Boolean)value).booleanValue());
        if(value instanceof String)
            return value((String) value);
        return this;
    }

    public <M extends Object> SqlFluent value(M value, ValueProvider<M> provider){
        if(provider == null)
            return value(value);
        return add(provider.getValue(value));
    }

    public SqlFluent values(int... values){
        if(values == null || values.length == 0)
            return this;
        openBracket();
        for(int i = 0; i < values.length; i++) {
            if(i > 0)
                comma();
            value(values[i]);
        }
        closeBracket();
        return this;
    }

    public SqlFluent values(long... values){
        if(values == null || values.length == 0)
            return this;
        openBracket();
        for(int i = 0; i < values.length; i++) {
            if(i > 0)
                comma();
            value(values[i]);
        }
        closeBracket();
        return this;
    }

    public SqlFluent values(float... values){
        if(values == null || values.length == 0)
            return this;
        openBracket();
        for(int i = 0; i < values.length; i++) {
            if(i > 0)
                comma();
            value(values[i]);
        }
        closeBracket();
        return this;
    }

    public SqlFluent values(double... values){
        if(values == null || values.length == 0)
            return this;
        openBracket();
        for(int i = 0; i < values.length; i++) {
            if(i > 0)
                comma();
            value(values[i]);
        }
        closeBracket();
        return this;
    }

    public SqlFluent values(boolean... values){
        if(values == null || values.length == 0)
            return this;
        openBracket();
        for(int i = 0; i < values.length; i++) {
            if(i > 0)
                comma();
            value(values[i]);
        }
        closeBracket();
        return this;
    }

    public SqlFluent values(String... values){
        return values(null, values);
    }

    public SqlFluent values(Object... values){
        return values(null, values);
    }

    public <M extends Object> SqlFluent values(ValueProvider<M> provider, M... values){
        if(values == null || values.length == 0)
            return this;
        openBracket();
        boolean firstItem = true;
        for (M value : values) {
            if (value != null) {
                if (!firstItem)
                    comma();
                else
                    firstItem = false;
                value(value, provider);
            }
        }
        closeBracket();
        return this;
    }

    public <M extends Object> SqlFluent values(ValueProvider<M> provider, Collection<M> values){
        if(values == null || values.size() == 0)
            return this;
        openBracket();
        boolean firstItem = true;
        for (M value : values) {
            if (value != null) {
                if (!firstItem)
                    comma();
                else
                    firstItem = false;
                value(value, provider);
            }
        }
        closeBracket();
        return this;
    }

    //endregion

    //region operators methods

    public SqlFluent equal(){
        return add(EQUAL);
    }

    public SqlFluent notEqual(){
        return add(NOT_EQUAL);
    }

    public SqlFluent lessThan(){
        return add(LESS);
    }

    public SqlFluent lessOrEqual(){
        return add(LESS_OR_EQUAL);
    }

    public SqlFluent greaterThan(){
        return add(GREAT);
    }

    public SqlFluent greaterOrEqual(){
        return add(GREAT_OR_EQUAL);
    }

    public SqlFluent add(){
        return add(ADD);
    }

    public SqlFluent subtract(){
        return add(SUBTRACT);
    }

    public SqlFluent multiple(){
        return add(MULTIPLE);
    }

    public SqlFluent divide(){
        return add(DIVIDE);
    }

    public SqlFluent module(){
        return add(MODULE);
    }

    public SqlFluent bitAnd(){
        return add(BIT_AND);
    }

    public SqlFluent bitOR(){
        return add(BIT_OR);
    }

    public SqlFluent bitFlip(){
        return add(BIT_FLIP);
    }

    public SqlFluent bitShift(boolean shiftLeft){
        return add(shiftLeft ? BIT_SHIFT_LEFT : BIT_SHIFT_RIGHT);
    }

    public SqlFluent and(){
        return add(AND);
    }

    public SqlFluent or(){
        return add(OR);
    }

    public SqlFluent not(){
        return add(NOT);
    }

    public SqlFluent all(){
        return add(ALL);
    }

    public SqlFluent in(){
        return add(IN);
    }

    public SqlFluent notIn(){
        return add(NOT_IN);
    }

    public SqlFluent like(){
        return add(LIKE);
    }

    public SqlFluent glob(){
        return add(GLOB);
    }

    public SqlFluent between(){
        return add(BETWEEN);
    }

    public SqlFluent is(){
        return add(IS);
    }

    public SqlFluent isNot(){
        return add(IS_NOT);
    }

    public SqlFluent isNull(){
        return add(IS_NULL);
    }

    public SqlFluent match(){
        return add(MATCH);
    }

    public SqlFluent regexp(){
        return add(REGEXP);
    }

    public SqlFluent exists(){
        return add(EXISTS);
    }

    public SqlFluent any(){
        return add(ANY);
    }

    public SqlFluent openBracket(){
        return add(String.valueOf(OPEN_BRACKET));
    }

    public SqlFluent closeBracket(){
        return addWithoutSpace(CLOSE_BRACKET);
    }

    //endregion

    //region function methods

    public SqlFluent max(String column){
        return add(MAX)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent min(String column){
        return add(MIN)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent avg(String column){
        return add(AVG)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent sum(String column){
        return add(SUM)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent abs(String column){
        return add(ABS)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent upper(String column){
        return add(UPPER)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent lower(String column){
        return add(LOWER)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent length(String column){
        return add(LENGTH)
                .addWithoutSpace(OPEN_BRACKET)
                .addWithoutSpace(column)
                .addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent random(){
        return add(RANDOM);
    }

    //endregion

    public SqlFluent comma(){
        return addWithoutSpace(COMMA);
    }

    public SqlFluent semiColon(){
        return addWithoutSpace(SEMI_COLON);
    }

    public SqlFluent where(){
        return add(WHERE);
    }

    public SqlFluent orderBy(){
        return add(ORDER_BY);
    }

    public SqlFluent groupBy(){
        return add(GROUP_BY);
    }

    public SqlFluent asc(){
        return add(ASC);
    }

    public SqlFluent desc(){
        return add(DESC);
    }

    public SqlFluent limit(int value){
        return add(LIMIT)
                .add(String.valueOf(value));
    }

    public SqlFluent offset(int value){
        return add(OFFSET)
                .add(String.valueOf(value));
    }

    public SqlFluent having(){
        return add(HAVING);
    }

    public SqlFluent delete(String table){
        return add(DELETE).add(table);
    }

    public SqlFluent update(String table){
        return add(UPDATE).add(table);
    }

    public SqlFluent set(){
        return add(SET);
    }

    public SqlFluent insert(String table){
        return add(INSERT).add(table);
    }

    public SqlFluent values(){
        return add(VALUES);
    }

    public SqlFluent crossJoin(String table){
        return add(JOIN_CROSS).add(table);
    }

    public SqlFluent join(String table){
        return add(JOIN_INNER).add(table);
    }

    public SqlFluent leftOuterJoin(String table){
        return add(JOIN_OUTER_LEFT).add(table);
    }

    public SqlFluent rightOuterJoin(String table){
        return add(JOIN_OUTER_RIGHT).add(table);
    }

    public SqlFluent fullOuterJoin(String table){
        return add(JOIN_OUTER_FULL).add(table);
    }

    public SqlFluent on(){
        return add(ON);
    }

    public SqlFluent using(){
        return add(USING);
    }

    public SqlFluent union(boolean allowDuplicate){
        return add(allowDuplicate ? UNION_ALL : UNION);
    }

    public SqlFluent createIndex(String indexName, String table, String... columns){
        return add(CREATE_INDEX)
                .add(indexName)
                .add(ON)
                .add(table)
                .values(columns);
    }

    public SqlFluent dropIndex(String indexName){
        return add(DROP_INDEX)
                .add(indexName);
    }

    public SqlFluent indexedBy(String indexName){
        return add(INDEXED_BY)
                .add(indexName);
    }

    private String getCharLength(int... optionals){
        if(optionals == null || optionals.length == 0)
            return "255";
        return String.valueOf(optionals[0]);
    }

    private SqlFluent dataType(@DataType.Type int type, int... optionals){
        switch (type){
            case DataType.INT:
                return add(INT);
            case DataType.INTEGER:
                return add(INTEGER);
            case DataType.TINYINT:
                return add(TINYINT);
            case DataType.SMALLINT:
                return add(SMALLINT);
            case DataType.MEDIUMINT:
                return add(MEDIUMINT);
            case DataType.BIGINT:
                return add(BIGINT);
            case DataType.UNSIGNED_BIG_INT:
                return add(UNSIGNED_BIG_INT);
            case DataType.INT2:
                return add(INT2);
            case DataType.INT8:
                return add(INT8);
            case DataType.CHARACTER:
                return add(CHARACTER)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(getCharLength(optionals))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.VARCHAR:
                return add(VARCHAR)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(getCharLength(optionals))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.VARYING_CHARACTER:
                return add(VARYING_CHARACTER)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(getCharLength(optionals))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.NCHAR:
                return add(NCHAR)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(getCharLength(optionals))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.NATIVE_CHARACTER:
                return add(NATIVE_CHARACTER)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(getCharLength(optionals))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.NVARCHAR:
                return add(NVARCHAR)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(getCharLength(optionals))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.TEXT:
                return add(TEXT);
            case DataType.CLOB:
                return add(CLOB);
            case DataType.REAL:
                return add(REAL);
            case DataType.DOUBLE:
                return add(DOUBLE);
            case DataType.DOUBLE_PRECISION:
                return add(DOUBLE_PRECISION);
            case DataType.FLOAT:
                return add(FLOAT);
            case DataType.NUMERIC:
                return add(NUMERIC);
            case DataType.DECIMAL:
                if(optionals == null || optionals.length < 2)
                    throw new IllegalArgumentException("Decimal type must specific number of digits and decimals.");
                return add(NVARCHAR)
                        .addWithoutSpace(OPEN_BRACKET)
                        .addWithoutSpace(String.valueOf(optionals[0]))
                        .addWithoutSpace(COMMA)
                        .addWithoutSpace(String.valueOf(optionals[1]))
                        .addWithoutSpace(CLOSE_BRACKET);
            case DataType.BLOB:
                return add(BLOB);
        }

        throw new IllegalArgumentException("Type must be one of DataType.");
    }

    public SqlFluent createTable(String table){
        return add(CREATE_TABLE)
                .add(table)
                .openBracket();
    }

    public SqlFluent columnDef(String column, @DataType.Type int type, int... optionals){
        return add(column)
                .dataType(type, optionals);
    }

    public SqlFluent andColumnDef(String column, @DataType.Type int type, int... optionals){
        return addWithoutSpace(COMMA)
                .add(column)
                .dataType(type, optionals);
    }

    private SqlFluent conflict(@Conflict.Type int type){
        if(type == Conflict.NONE)
            return this;
        add(CONFLICT);
        switch (type){
            case Conflict.ROLLBACK:
                return add(ROLLBACK);
            case Conflict.ABORT:
                return add(ABORT);
            case Conflict.FAIL:
                return add(FAIL);
            case Conflict.IGNORE:
                return add(IGNORE);
            case Conflict.REPLACE:
                return add(REPLACE);
        }
        throw new IllegalArgumentException("Type must be one of ConflictType.");
    }

    public SqlFluent primaryKey(@Conflict.Type int conflictType){
        return add(PRIMARY_KEY)
                .conflict(conflictType);
    }

    public SqlFluent primaryKey(@Conflict.Type int conflictType, String... columns){
        if(columns == null || columns.length == 0)
            throw new IllegalArgumentException("Columns must not be empty.");

        addWithoutSpace(COMMA)
                .add(PRIMARY_KEY)
                .openBracket();
        for(int i = 0; i < columns.length; i++){
            if(i == 0)
                addWithoutSpace(columns[i]);
            else
                addWithoutSpace(COMMA)
                        .add(columns[i]);
        }
        return addWithoutSpace(CLOSE_BRACKET).conflict(conflictType);
    }

    public SqlFluent autoIncrement(){
        return add(AUTOINCREMENT);
    }

    public SqlFluent unique(@Conflict.Type int conflictType){
        return add(UNIQUE)
                .conflict(conflictType);
    }

    public SqlFluent unique(@Conflict.Type int conflictType, String... columns){
        if(columns == null || columns.length == 0)
            throw new IllegalArgumentException("Columns must not be empty.");

        addWithoutSpace(COMMA)
                .add(UNIQUE)
                .openBracket();
        for(int i = 0; i < columns.length; i++){
            if(i == 0)
                addWithoutSpace(columns[i]);
            else
                addWithoutSpace(COMMA)
                        .add(columns[i]);
        }
        return addWithoutSpace(CLOSE_BRACKET).conflict(conflictType);
    }

    public SqlFluent notNull(@Conflict.Type int conflictType){
        return add(NOT_NULL)
                .conflict(conflictType);
    }

    public SqlFluent defaultValue(){
        return add(DEFAULT);
    }

    public SqlFluent check(){
        return add(CHECK);
    }

    public SqlFluent endCreate(){
        return addWithoutSpace(CLOSE_BRACKET);
    }

    public SqlFluent dropTable(String table){
        return add(DROP_TABLE)
                .add(table);
    }

    public SqlFluent custom(String sql){
        return add(sql);
    }

    public String build(){
        return mBuilder.toString();
    }
}
