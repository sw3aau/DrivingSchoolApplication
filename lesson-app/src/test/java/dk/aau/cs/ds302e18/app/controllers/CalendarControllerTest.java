package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.DBConnector;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class CalendarControllerTest {
    public Connection conn;

    @Test
    public void updateTest() {
        String hah = "Hellow world";
        try{
            conn = new DBConnector().createConnectionObject();
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE `account` SET `address`='Herningvej' WHERE `username` = 'test10'");
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}