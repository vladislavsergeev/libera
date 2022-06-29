import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import java.util.ArrayList;
import java.util.List;

import socketmaster.Socketmaster;

class server {
	public static void main(String args[]) {
		System.out.println("LoginController -> createUsersTable() ...");
		
		Connection con = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        String tableCreateSQL = "CREATE TABLE if not EXISTS users ( id INTEGER IDENTITY PRIMARY KEY,"
                + " login VARCHAR(60) NOT NULL , password VARCHAR(60) NOT NULL )";
        try {
            con.prepareStatement(tableCreateSQL).execute();
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("failed to create table");
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("error in close connection");
                System.out.println(ex);
            }
        }
		
		try {
			System.out.println("Starts working");
			ServerSocket ss = new ServerSocket(1111);
		
			Socketmaster sm = new Socketmaster(ss);
		}
		catch (Exception e) {
			
        }
		
		try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
		tableCreateSQL = "CREATE TABLE if not EXISTS tasks ( id INTEGER IDENTITY PRIMARY KEY,"
                + " text LONGVARCHAR NOT NULL , color VARCHAR(25) NOT NULL , user_id numeric(10) NOT NULL )";
        try {
            con.prepareStatement(tableCreateSQL).execute();
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("failed to create table");
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("error in close connection");
                System.out.println(ex);
            }
        }
		
		while (true) {
			
		}
	}
}