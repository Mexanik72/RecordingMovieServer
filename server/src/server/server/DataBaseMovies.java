package server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseMovies {
	
	private Connection con;
	
	public DataBaseMovies ()
	{
		con = GetConnection.getConnection();
	}

	public List<Integer> getMoviesIds() throws Exception {
		List<Integer> moviesIds = new ArrayList<Integer>();
		ResultSet rs = con.createStatement().executeQuery(
				"Select id From movies");
		while (rs.next()) {
			moviesIds.add(rs.getInt(1));
		}
		rs.close();
		con.close();
		return moviesIds;
	}

	public void addMovie(Movie movie) throws Exception {
		PreparedStatement st = con.prepareStatement("Insert into movies"
				+ "(owner, name, word) " + "values ( ?, ?, ?)");
		st.setInt(1, movie.getOwner());
		st.setString(2, movie.getName());
		st.setInt(3, movie.getWord());
		st.executeUpdate();
		con.close();
	}
	
	public Movie getPathByWord (int wordId) throws Exception {
		Movie mov = new Movie();
		PreparedStatement st = con.prepareStatement("SELECT owner, name, word FROM movies WHERE word = ?");
		st.setInt(1, wordId);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			mov = new Movie(wordId, rs.getInt(1), rs.getString(2), rs.getInt(3));
		}
		
		rs.close();
		con.close();
		return mov;		
	}
	
	public List<Movie> getMovieByCategor (int categor) {
		List<Movie> mov = new ArrayList<Movie>();
		PreparedStatement st;
		try {
			st = con.prepareStatement("Select movies.id, movies.owner, movies.name, movies.word from movies inner join words on movies.word = words.id and words.categor = ?");
		
		st.setInt(1, categor);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			mov.add( new Movie(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4)));
		}
		
		rs.close();
		con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mov;		
	}
	
	public List<String> getKeyWords(int word) throws Exception {
		List<String> moviesIds = new ArrayList<String>();
		PreparedStatement st = con.prepareStatement("Select key From key_words Where word = ?");
		st.setInt(1, word);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			moviesIds.add(rs.getString(1));
		}
		rs.close();
		con.close();
		return moviesIds;
	}
	
	public void addRatetoMovie(Movie movie, Date date, int rate) throws Exception {
		PreparedStatement st = con.prepareStatement("Insert into rate_of_video"
				+ "(user_owner, movie, date, rate) " + "values ( ?, ?, ?, ?)");
		
		st.setInt(1, movie.getOwner());
		st.setInt(2, movie.getId());
		st.setDate(3, date);
		st.setInt(4, rate);
		st.executeUpdate();

		con.close();
	}
}
