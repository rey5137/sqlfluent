package com.rey.sqlfluent;

/**
 * Created by Rey on 3/14/2016.
 */
public class Value {

    public static final int TRUE                            = 1;
    public static final int FALSE                           = 0;

    protected static final String SELECT                    = "SELECT";
    protected static final String SELECT_DISTINCT           = "SELECT DISTINCT";
    protected static final String SELECT_ALL                = "SELECT *";
    protected static final String SELECT_COUNT_ALL          = "SELECT COUNT(*)";

    protected static final String AS                        = "AS";
    protected static final String FROM                      = "FROM";
    protected static final String WHERE                     = "WHERE";
    protected static final String ORDER_BY                  = "ORDER BY";
    protected static final String GROUP_BY                  = "GROUP BY";
    protected static final String ASC                       = "ASC";
    protected static final String DESC                      = "DESC";
    protected static final String LIMIT                     = "LIMIT";
    protected static final String OFFSET                    = "OFFSET";
    protected static final String HAVING                    = "HAVING";

    protected static final String DELETE                    = "DELETE FROM";
    protected static final String UPDATE                    = "UPDATE";
    protected static final String SET                       = "SET";
    protected static final String INSERT                    = "INSERT INTO";
    protected static final String VALUES                    = "VALUES";

    protected static final String JOIN_CROSS                = "CROSS JOIN";
    protected static final String JOIN_INNER                = "JOIN";
    protected static final String JOIN_OUTER_LEFT           = "LEFT OUTER JOIN";
    protected static final String JOIN_OUTER_RIGHT          = "RIGHT OUTER JOIN";
    protected static final String JOIN_OUTER_FULL           = "FULL OUTER JOIN";
    protected static final String ON                        = "ON";
    protected static final String USING                     = "USING";

    protected static final String UNION                     = "UNION";
    protected static final String UNION_ALL                 = "UNION ALL";

    protected static final String CREATE_INDEX              = "CREATE INDEX";
    protected static final String DROP_INDEX                = "DROP INDEX";
    protected static final String INDEXED_BY                = "INDEXED BY";

    protected static final String CREATE_TABLE              = "CREATE TABLE IF NOT EXISTS";
    protected static final String CONFLICT                  = "ON CONFLICT";
    protected static final String ROLLBACK                  = "ROLLBACK";
    protected static final String ABORT                     = "ABORT";
    protected static final String FAIL                      = "FAIL";
    protected static final String IGNORE                    = "IGNORE";
    protected static final String REPLACE                   = "REPLACE";
    protected static final String PRIMARY_KEY               = "PRIMARY KEY";
    protected static final String UNIQUE                    = "UNIQUE";
    protected static final String NOT_NULL                  = "NOT NULL";
    protected static final String DEFAULT                   = "DEFAULT";
    protected static final String CHECK                     = "CHECK";
    protected static final String DROP_TABLE                = "DROP TABLE IF EXISTS";

    protected static final String MAX                       = "MAX";
    protected static final String MIN                       = "MIN";
    protected static final String AVG                       = "AVG";
    protected static final String SUM                       = "SUM";
    protected static final String ABS                       = "ABS";
    protected static final String UPPER                     = "UPPER";
    protected static final String LOWER                     = "LOWER";
    protected static final String LENGTH                    = "LENGTH";
    protected static final String RANDOM                    = "RANDOM()";

    protected static final String EQUAL                     = "=";
    protected static final String NOT_EQUAL                 = "<>";
    protected static final String LESS                      = "<";
    protected static final String LESS_OR_EQUAL             = "<=";
    protected static final String GREAT                     = ">";
    protected static final String GREAT_OR_EQUAL            = ">=";

    protected static final String ADD                       = "+";
    protected static final String SUBTRACT                  = "-";
    protected static final String MULTIPLE                  = "*";
    protected static final String DIVIDE                    = "/";
    protected static final String MODULE                    = "%";

    protected static final String BIT_AND                   = "&";
    protected static final String BIT_OR                    = "|";
    protected static final String BIT_FLIP                  = "~";
    protected static final String BIT_SHIFT_LEFT            = "<<";
    protected static final String BIT_SHIFT_RIGHT           = ">>";

    protected static final String AND                       = "AND";
    protected static final String OR                        = "OR";
    protected static final String NOT                       = "NOT";
    protected static final String ALL                       = "ALL";
    protected static final String IN                        = "IN";
    protected static final String NOT_IN                    = "NOT IN";
    protected static final String LIKE                      = "LIKE";
    protected static final String GLOB                      = "GLOB";
    protected static final String BETWEEN                   = "BETWEEN";
    protected static final String IS                        = "IS";
    protected static final String IS_NOT                    = "IS NOT";
    protected static final String IS_NULL                   = "IS NULL";
    protected static final String MATCH                     = "MATCH";
    protected static final String REGEXP                    = "REGEXP";
    protected static final String EXISTS                    = "EXISTS";
    protected static final String ANY                       = "ANY";

    protected static final String INT                       = "INT";
    protected static final String INTEGER                   = "INTEGER";
    protected static final String TINYINT                   = "TINYINT";
    protected static final String SMALLINT                  = "SMALLINT";
    protected static final String MEDIUMINT                 = "MEDIUMINT";
    protected static final String BIGINT                    = "BIGINT";
    protected static final String UNSIGNED_BIG_INT          = "UNSIGNED BIG INT";
    protected static final String INT2                      = "INT2";
    protected static final String INT8                      = "INT8";

    protected static final String CHARACTER                 = "CHARACTER";
    protected static final String VARCHAR                   = "VARCHAR";
    protected static final String VARYING_CHARACTER         = "VARYING CHARACTER";
    protected static final String NCHAR                     = "NCHAR";
    protected static final String NATIVE_CHARACTER          = "NATIVE CHARACTER";
    protected static final String NVARCHAR                  = "NVARCHAR";
    protected static final String TEXT                      = "TEXT";
    protected static final String CLOB                      = "CLOB";

    protected static final String REAL                      = "REAL";
    protected static final String DOUBLE                    = "DOUBLE";
    protected static final String DOUBLE_PRECISION          = "DOUBLE PRECISION";
    protected static final String FLOAT                     = "FLOAT";
    protected static final String NUMERIC                   = "NUMERIC";
    protected static final String BLOB                      = "BLOB";
}
