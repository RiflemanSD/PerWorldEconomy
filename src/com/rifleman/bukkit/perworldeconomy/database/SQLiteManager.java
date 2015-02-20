package com.rifleman.bukkit.perworldeconomy.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>SQLite database</h1>
 * <p>This class use sqlite-jdbc library to
 * haddle a SQLite database</p>
 * 
 * <p>You need to have last version of sqlite-jdbc,
 * donwload here: https://bitbucket.org/xerial/sqlite-jdbc/downloads</p>
 * 
 * <p>Created: 20-02-2015
 * Last Update: 20-02-2015</p>
 * 
 * <p>Author: https://github.com/RiflemanSD</p>
 * 
 * @version 1.0.0 
 * @author rifleman
 */
public class SQLiteManager {
    private Connection c;
    private String[] gColumns;
    
    public SQLiteManager() {
        gColumns = new String[3];
        gColumns[0] = "id";
        gColumns[1] = "playername";
        gColumns[2] = "money";
        
        String g1 = "group1";
        String g2 = "group2";
        String g3 = "group3";
        
        openDB("EcoDB", false);
        
        String sql;
        
        /*sql = "INSERT INTO " + g1 + " (ID,playerName,money) "
                    + "VALUES (1, 'Paul', 10.0);";
        insert(sql);
        sql = "INSERT INTO " + g1 + " (ID,playerName,money) "
                    + "VALUES (2, 'lakis', 51.5);";
        insert(sql);
        sql = "INSERT INTO " + g1 + " (ID,playerName,money) "
                    + "VALUES (3, 'rifleman', 67);";
        insert(sql);
        
        sql = "UPDATE " + g1 + " set money = 5000.00 where playerName='lakis';";
        insert(sql);*/
        
        String r = selectALL(g1);
        
        System.out.println(r);
        
        commit();
        close();
    }

// ||||||||||||||||||||||| - Rifleman - |||||||||||||||||||||||
    
    public int openDB(String db, boolean autoCommit) {
        c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + db + ".db");
            c.setAutoCommit(autoCommit);
            
            return DBData.NO_ERROR;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            
            return DBData.ERROR_CONNECTION;
        }
    }
    
    public int close() {
        try {
            c.close();
            
            return DBData.NO_ERROR;
        } catch (SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            return DBData.ERROR_CLOSE_CONNECTION;
        }
    }

    public int commit() {
        try {
            c.commit();
            return DBData.NO_ERROR;
        } catch (SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            
            return DBData.ERROR_COMMIT;
        }
    }
    
    /*
    INSERT, UPDATE, DELETE
    */
    public int executeUpdates(String... querys) {
        try {
            Statement stmt = null;
            stmt = c.createStatement();
            
            for (String query: querys) {
                stmt.executeUpdate(query);
            }
            
            stmt.close();
            
            return DBData.NO_ERROR;
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteManager.class.getName()).log(Level.SEVERE, null, ex);
            
            return DBData.ERROR_Statement;
        }
    }
    
    public String executeGroupQuery(String query) {
        String result = "";
        try {
            Statement stmt = null;
            stmt = c.createStatement();
            stmt.executeUpdate(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(gColumns[0]);
                String name = rs.getString(gColumns[1]);
                float money = rs.getFloat(gColumns[2]);
                
                result += id + "," + name + "," + money + "\n";
            }
            rs.close();
            
            stmt.close();
            
            //return DBData.NO_ERROR;
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteManager.class.getName()).log(Level.SEVERE, null, ex);
            
            //return DBData.ERROR_Statement;
        }
        
        return result;
    }
    
    public void createTable(String tableName) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + tableName
                    + " (ID INT PRIMARY KEY NOT NULL,"
                    + " playerName TEXT NOT NULL, "
                    + " money REAL NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() + ", " + e.hashCode());
        }
    }

    
    public String selectALL(String tableName) {
        
        return this.executeGroupQuery("SELECT * FROM " + tableName + ";");
    }
    
    
    public static void main(String args[]) {
        new SQLiteManager();
    }

}
