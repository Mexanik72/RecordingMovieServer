package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBaseUsers {
	
	private Connection con;
	
	public DataBaseUsers ()
	{
		con = GetConnection.getConnection();
	}

	public List<Integer> getUsersIds() throws Exception {
		List<Integer> usersIds = new ArrayList<Integer>();
		ResultSet rs = con.createStatement().executeQuery(
				"Select id From users");
		while (rs.next()) {
			usersIds.add(rs.getInt(1));
		}
		rs.close();
		con.close();
		return usersIds;
	}
	
	public List<String> getUsersNames() throws Exception {
		List<String> usersNames = new ArrayList<String>();
		ResultSet rs = con.createStatement().executeQuery(
				"Select username From users");
		while (rs.next()) {
			usersNames.add(rs.getString(1));
		}
		rs.close();
		con.close();
		return usersNames;
	}
	
	public User getUserByUsername(String username) throws Exception {
		User users = new User();
		Connection con = GetConnection.getConnection();
		PreparedStatement st = con.prepareStatement("Select id, name, password, score, img "
				+ "From users " + "Where username = ?");
		st.setString(1, username);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			users = new User(rs.getInt(1), rs.getString(2), username,
					rs.getString(3),rs.getInt(4),rs.getString(5));
		}
		rs.close();
		con.close();
		return users;
	}
	
	public List<User> getUserById(int id) throws Exception {
		List<User> users = new ArrayList<User>();
		PreparedStatement st = con.prepareStatement("Select name, username, password, score, img "
				+ "From users " + "Where id = ?");
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();

		User user = null;
		while (rs.next()) {
			user = new User(id, rs.getString(1), rs.getString(2),
					rs.getString(3),rs.getInt(4), rs.getString(5));
			users.add(user);
		}
		rs.close();
		con.close();
		return users;
	}
	
	public void addUser(User user) throws Exception {
		PreparedStatement st = con.prepareStatement("Insert into users"
				+ "(name, username, password) " + "values (?, ?, ?)");
		st.setString(1, user.getName());
		st.setString(2, user.getUsername());
		st.setString(3, user.getPassword());
		st.executeUpdate();

		con.close();
	}
	
	public void setImg(int id, String img) throws Exception {
		PreparedStatement st = con.prepareStatement(
				"Update users " +
				"Set img=?" +
				"Where id=?");
		st.setInt(2, id);
		st.setString(1, img);
		st.executeUpdate();
		con.close();
	}
}
