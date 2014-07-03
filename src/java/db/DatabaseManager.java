/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael A. Osho
 */
public class DatabaseManager {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String url = "jdbc:derby:";//"jdbc:derby://localhost:1527/sr_accountDB";    
    private static String userName = "admin", userPassword = "admin";

    public DatabaseManager() {
        initDbDriver();
    }
    
    
    
    public void createDatabase() {
        initDbDriver();
        Connection c = null;
        
        try {
            System.out.println("before connected");
            c = DriverManager.getConnection("jdbc:derby:C:/webapps/KingHotelsDB;create=true;", getUserName(), getUserPassword());
            System.out.println("connected");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Statement st;
        try {
            st = c.createStatement();
            st.addBatch("CREATE TABLE CUSTOMER "
                    + "("
                    + "CUSTOMER_ID int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY, "
                    + "TITLE varchar(20), "                    
                    + "FIRST_NAME varchar(150), "
                    + "LAST_NAME varchar(150), "
                    + "ADDRESS varchar(200), "
                    + "PHONE_NUMBER varchar(150), "
                    + "EMAIL varchar(150) "
                    + ")");
            st.addBatch("CREATE TABLE ROOM_PRICING "
                    + "("
                    + "ROOM_TYPE varchar(150), "
                    + "PRICE_PER_ROOM double, "
                    + "NUMBER_OF_ROOMS int "
                    + ")");
            st.addBatch("CREATE TABLE BOOKING "
                    + "("
                    + "BOOK_ID varchar(150) PRIMARY KEY NOT NULL, "
                    + "TIME_STAMP timestamp, "
                    + "CUSTOMER_ID int, "
                    + "ROOM_TYPE varchar(150), "
                    + "CHECK_IN_DATE date, "
                    + "CHECK_OUT_DATE date, "
                    + "NUMBER_OF_DAYS INT, "
                    + "NUMBER_OF_ROOMS varchar(150), "                    
                    + "TOTAL_COST varchar(200), "
                    + "PAID varchar(10) "                    
                    + ")");
            st.addBatch("CREATE TABLE EXPIRED_BOOKING "
                    + "("
                    + "BOOK_ID varchar(150), "
                    + "TIME_STAMP timestamp, "
                    + "CUSTOMER_ID int, "
                    + "ROOM_TYPE varchar(150), "
                    + "CHECK_IN_DATE date, "
                    + "CHECK_OUT_DATE date, "
                    + "NUMBER_OF_DAYS INT, "
                    + "NUMBER_OF_ROOMS varchar(150), "                    
                    + "TOTAL_COST varchar(200), "
                    + "PAID varchar(10) "                    
                    + ")");
            st.executeBatch();
            st.addBatch("INSERT INTO ROOM_PRICING VALUES ('STANDARD', 20000, 20)");
            st.addBatch("INSERT INTO ROOM_PRICING VALUES ('VIP', 45000, 5)");
            st.addBatch("INSERT INTO ROOM_PRICING VALUES ('VVIP', 60000, 2)");
            st.executeBatch();
            
            // shutdown the created db
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException sQLException) {
            }
            // reinit db driver for further use
            initDbDriver();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }
    
    private void initDbDriver() {
        try {
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:derby:C:/webapps/KingHotelsDB", getUserName(), getUserPassword());
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserPassword() {
        return userPassword;
    }
    
   
}
