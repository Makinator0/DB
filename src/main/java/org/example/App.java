package org.example;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/kn_1022V";
        String username = "root";
        String password = "root";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            // Вывод содержимого таблицы до заполнения
            System.out.println("Начальное содержание таблицы:");
            printTableContent(statement, "SELECT * FROM kn_1022v.disciplines");

            // Заполнение столбца Influence случайными числами
            fillInfluenceColumn(statement, "UPDATE kn_1022v.disciplines SET Influence = ? WHERE Id = ?");

            // Вывод содержимого таблицы после заполнения
            System.out.println("Содержание таблицы после заполнения столбца 'Influence':");
            printTableContent(statement, "SELECT * FROM kn_1022v.disciplines");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     private static void printTableContent(Statement statement, String query) throws SQLException {
         ResultSet resultSet = statement.executeQuery(query);
         ResultSetMetaData metaData = resultSet.getMetaData();
         int columnCount = metaData.getColumnCount();

         for (int i = 1; i <= columnCount; i++) {
             System.out.print(metaData.getColumnName(i) + "\t");
         }
         System.out.println();

         while (resultSet.next()) {
             for (int i = 1; i <= columnCount; i++) {
                 System.out.print(resultSet.getString(i) + "\t");
             }
             System.out.println();
         }
         resultSet.close();
     }

     private static void fillInfluenceColumn(Statement statement, String query) throws SQLException {
         PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query);
         ResultSet resultSet = statement.executeQuery("SELECT Id FROM kn_1022v.disciplines");
         while (resultSet.next()) {
             int Id = resultSet.getInt("Id");
             int randomInfluence = (int) (Math.random() * 41) + 60; // Случайное число от 60 до 100
             preparedStatement.setInt(1, randomInfluence);
             preparedStatement.setInt(2, Id);
             preparedStatement.addBatch();
         }
         preparedStatement.executeBatch();
         preparedStatement.close();
         resultSet.close();
     }



}

