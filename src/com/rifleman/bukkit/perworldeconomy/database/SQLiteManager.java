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
        
        String[] sql = new String[5];
        
        sql[0] = this.insertPlayerQuery(g2, 1, "rifleman", 10);
        sql[1] = this.insertPlayerQuery(g2, 2, "Rifleman", 10);
        sql[2] = this.insertPlayerQuery(g2, 3, "kotes", 10);
        sql[3] = this.insertPlayerQuery(g2, 4, "pezo", 10);
        sql[4] = this.updateMoneyQuery(g2, "rifleman", 40);
        
        this.executeUpdates(sql);
        
        String eq = this.getMoneyQuery(g2, "rifleman");
        
        System.out.println(this.executeGetMoneyQ(eq));
        
        System.out.println(this.selectALL(g1));
        System.out.println(this.selectALL(g2));
        
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
        for (String query: querys) {
            try {
                Statement stmt = null;
                stmt = c.createStatement();
                stmt.executeUpdate(query);

                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(SQLiteManager.class.getName()).log(Level.SEVERE, null, ex);

                //return DBData.ERROR_Statement;
            }   
        }
        return DBData.NO_ERROR;
    }
    public float executeGetMoneyQ(String query) {
        float money = -1;
        try {
            Statement stmt = null;
            stmt = c.createStatement();
            //stmt.executeUpdate(query);
            ResultSet rs = stmt.executeQuery(query);
            money = rs.getFloat(gColumns[2]);
            rs.close();
            
            stmt.close();
            
            //return DBData.NO_ERROR;
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteManager.class.getName()).log(Level.SEVERE, null, ex);
            
            //return DBData.ERROR_Statement;
        }
        
        return money;
    }
    public String executeGroupQuery(String query) {
        String result = "";
        try {
            Statement stmt = null;
            stmt = c.createStatement();
            //stmt.executeUpdate(query);
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
    
    public int createTable(String tableName, String... columns) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + tableName
                    + " (" + columns[0] + " INT PRIMARY KEY NOT NULL,"
                    + " " + columns[1] + " TEXT NOT NULL, "
                    + " " + columns[2] + " REAL NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            
            return DBData.NO_ERROR;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            
            return DBData.ERROR_Statement;
        }
    }

    
    public String selectALL(String tableName) {
        
        return this.executeGroupQuery("SELECT * FROM " + tableName + ";");
    }
    
    public String insertPlayerQuery(String tableName, int id, String player, double money) {
        return "INSERT INTO " + tableName + " (" + this.gColumns[0] + "," + this.gColumns[1] + "," + this.gColumns[2] + ") "
                    + "VALUES (" + id + ", '" + player + "', " + money + ");";
    }
    
    public String updateMoneyQuery(String tableName, String player, double money) {
        return "UPDATE " + tableName + " set money = " + money + " where playerName='" + player + "';";
    }
    
    public String getMoneyQuery(String tableName, String player) {
        return "SELECT money FROM " + tableName + " WHERE playerName='" + player + "';";
    }
    
    public static void main(String args[]) {
        new SQLiteManager();
    }

}
