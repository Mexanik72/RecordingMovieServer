package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWord {
	
	private Connection con;
	
	public DataBaseWord ()
	{
		con = GetConnection.getConnection();
	}

	public List<Integer> getWordsIds() throws Exception {
		List<Integer> wordsIds = new ArrayList<Integer>();
		ResultSet rs = con.createStatement().executeQuery(
				"Select id From words");
		while (rs.next()) {
			wordsIds.add(rs.getInt(1));
		}
		rs.close();
		con.close();
		return wordsIds;
	}

	public List<String> getWords() throws Exception {
		List<String> Words = new ArrayList<String>();
		ResultSet rs = con.createStatement().executeQuery(
				"Select word From words");
		while (rs.next()) {
			Words.add(rs.getString(1));
		}
		rs.close();
		con.close();
		return Words;
	}

	public List<String> getWordsWhereC(int category) throws Exception {
		List<String> Words = new ArrayList<String>();
		PreparedStatement st = con.prepareStatement(
				"Select word " + "From words " + "Where categor = ?");
		st.setInt(1, category);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			Words.add(rs.getString(1));
		}
		st.close();
		rs.close();
		con.close();
		return Words;
	}

	public List<String> getCategories() throws Exception {
		List<String> Categories = new ArrayList<String>();
		ResultSet rs = con.createStatement().executeQuery(
				"Select name From categories");
		while (rs.next()) {
			Categories.add(rs.getString(1));
		}
		rs.close();
		con.close();
		return Categories;
	}

	public Categories getIdByCategories(String name) throws Exception {
		Categories categ = new Categories();
		PreparedStatement st = con.prepareStatement("Select id "
				+ "From categories " + "Where name = ?");
		st.setString(1, name);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			categ = new Categories(rs.getInt(1), name);

		}
		rs.close();
		con.close();
		return categ;
	}

	public Word getIdByWords(String word) throws Exception {
		Word words = new Word();
		PreparedStatement st = con
				.prepareStatement("Select id, rate, description, img, categor "
						+ "From words " + "Where word = ?");
		st.setString(1, word);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			words = new Word(rs.getInt(1), word, rs.getInt(2), rs.getString(3),
					rs.getString(4), rs.getInt(5));

		}
		rs.close();
		con.close();
		return words;
	}

	public void addWord(Word word) throws Exception {
		PreparedStatement st = con.prepareStatement("Insert into words"
				+ "(word, rate, description, img) " + "values (?, ?, ?, ?)");
		st.setString(1, word.getWord());
		st.setInt(2, word.getRate());
		st.setString(3, word.getDescription());
		st.setString(4, word.getImg());
		st.executeUpdate();
		con.close();
	}
	
	public List<Integer> getIdByCategories (int category) throws Exception {
		List<Integer> words = new ArrayList<Integer>();
		PreparedStatement st = con.prepareStatement("SELECT id FROM words WHERE categor = ?");
		st.setInt(1, category);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			words.add(rs.getInt(1));
		}
		
		rs.close();
		con.close();
		return words;
	}

	public int getCount() throws Exception {
		PreparedStatement st = con
				.prepareStatement("SELECT COUNT(*) FROM words;");
		ResultSet rs = st.executeQuery();

		int count = 0;
		while (rs.next()) {
			count = rs.getInt(1);
		}

		rs.close();
		con.close();

		return count;
	}
	
	public int getWordById(int id) throws Exception {
		  int rate = 0;

		  PreparedStatement st = con.prepareStatement(
		    "Select  rate " + "From words " + "Where id = ?");
		  st.setInt(1, id);
		  
		  ResultSet rs = st.executeQuery();
		  
		  while (rs.next()) {
		   rate = rs.getInt(1);
		  }
		  st.close();
		  rs.close();
		  con.close();
		  return rate;
		 }
}
