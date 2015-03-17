package server;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class GetConnection {
	public static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			String url = "jdbc:postgresql://localhost/alig";
			return DriverManager.getConnection(url, "postgres", "toor123");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Could not connect to database: " + ex.getMessage(),
					"Error", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
}
