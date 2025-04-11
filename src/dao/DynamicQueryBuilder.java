package dao;

import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.List;

public class DynamicQueryBuilder {

    private Connection getConnection() throws SQLException {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    public void runDynamicQuery(String table, List<String> columns, String condition, String orderBy) {
        StringBuilder query = new StringBuilder("SELECT ");

        if (columns == null || columns.isEmpty()) {
            query.append("*");
        } else {
            query.append(String.join(", ", columns));
        }

        query.append(" FROM ").append(table);

        if (condition != null && !condition.trim().isEmpty()) {
            query.append(" WHERE ").append(condition);
        }

        if (orderBy != null && !orderBy.trim().isEmpty()) {
            query.append(" ORDER BY ").append(orderBy);
        }

        System.out.println("Executing: " + query);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();

            System.out.println("\n📋 Query Results:");
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + rs.getString(i) + " | ");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Error running dynamic query: " + e.getMessage());
        }
    }
}
