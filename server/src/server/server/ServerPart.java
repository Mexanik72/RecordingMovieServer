package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServerPart {

	private DataInputStream din = null;
	private DataOutputStream outD = null;
	private Socket soket = null;

	ServerPart() {
		connect();
	}

	public void connect() {
		int port = 2154;
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				soket = ss.accept();
				InputStream in = soket.getInputStream();
				din = new DataInputStream(in);
				outD = new DataOutputStream(soket.getOutputStream());
				int flag = din.readInt();

				if (flag == 0)
					sendVideo();
//				else if (flag == 1)
//					sendButtonIcon();
//				else if (flag == 2)
//					sendUserIcon();
				else if (flag == 3)
					getVideo();
				else if (flag == 4)
					sendCategories();
				else if (flag == 5)
					sendIdByCategories();
				else if (flag == 6)
					setImg();
				else if (flag == 7)
					authentication();
				else if (flag == 8)
					addUser();
				else if (flag == 9)
					getKeyWords();
				else if (flag == 10)
					getMovieByCategor();
				else if (flag == 11)
					getMoviesIds();
				else if (flag == 12)
					addScoreAndMovie();
				else if (flag == 13)
					getWordsWhereC();
				else if (flag == 14)
					getIdByWords();
				else if (flag == 15)
					getScoreById();
				else if (flag == 16)
					getFiveTopUsers();
				else if (flag == 17)
					addRateToMovie();
				else
					break;
			}
			ss.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getFiveTopUsers() throws Exception {
		DataBaseScore dbs = new DataBaseScore();
		List<Score> listScores = dbs.getFiveTopUser();
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(listScores);
        out.close();
	}

	private void getScoreById() throws Exception {
		DataBaseScore dbs = new DataBaseScore();
		int userNow = din.readInt();
		int scoreInt = dbs.getScoreByUser(userNow);
		outD.writeInt(scoreInt);
	}

	private void getIdByWords() throws IOException {
		Word wordNow = null;
		DataBaseWord dw = new DataBaseWord();
		String word = din.readUTF();
		try {
			wordNow = dw.getIdByWords(word);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(wordNow);
        out.close();
	}

	private void getWordsWhereC() throws IOException {
		DataBaseWord dw = new DataBaseWord();
		ArrayList<String> Words = null;
		int categoryNow = din.readInt();
		
		try {
			Words = (ArrayList<String>) dw.getWordsWhereC(categoryNow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(Words);
        out.close();
	}

	private void addScoreAndMovie() throws IOException, ClassNotFoundException {
		Movie mv = new Movie();
		DataBaseMovies db = new DataBaseMovies();
		
		String fileName = din.readUTF();
		ObjectInputStream in = new ObjectInputStream(soket.getInputStream());
		User userNow = (User) in.readObject();
		Word wordNow = (Word) in.readObject();
		
		mv.setOwner(userNow.getId());
		mv.setName(fileName);
		mv.setWord(wordNow.getId());
		
		DataBaseScore dbs = new DataBaseScore();
		try {
			int scoreNow = dbs.getScoreByUser(userNow.getId());
			scoreNow = scoreNow + (wordNow.getRate() * 5);
			java.util.Date someDate = Calendar.getInstance().getTime();
			java.sql.Date sqlDate = new java.sql.Date(someDate.getTime());
			Score score = new Score();
			score.setDate(sqlDate);
			score.setUser(userNow.getId());
			score.setRate(scoreNow);
			DataBaseScore dbs1 = new DataBaseScore();
			dbs1.addScore(score);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			db.addMovie(mv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getMoviesIds() throws IOException {
		
		DataBaseMovies dbmv = new DataBaseMovies();
		List<Integer> ListIdmovies = null;
		try {
			ListIdmovies = dbmv.getMoviesIds();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(ListIdmovies);
        out.close();
	}

	private void getMovieByCategor() throws IOException {
		
		DataBaseMovies dbm = new DataBaseMovies();
		List<Movie> movies = new ArrayList<Movie>();
		System.out.println("server is ok");
		int categor = din.readInt();
        
		try {
			movies = dbm.getMovieByCategor(categor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(movies);
        out.close();
	}

	private void getKeyWords() throws IOException, ClassNotFoundException {
		DataBaseMovies dbm = new DataBaseMovies();
		  List<String> Key_words = null;
		  
		  int movie = din.readInt();
		  
		  try {
		   Key_words = dbm.getKeyWords(movie);
		  } catch (Exception e1) {
		   e1.printStackTrace();
		  }
		  ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
		        out.writeObject(Key_words);
	}

	private void addRateToMovie() throws IOException, ClassNotFoundException {
		  DataBaseMovies dbm = new DataBaseMovies();
			ObjectInputStream in = new ObjectInputStream(soket.getInputStream());
		  Movie movieNow = (Movie) in.readObject();
		  java.sql.Date sqlDate = (Date) in.readObject();
		  int rate = din.readInt();
		  try {
		   dbm.addRatetoMovie(movieNow, sqlDate, rate + 1);
		  } catch (Exception e1) {
		   e1.printStackTrace();
		  }
		 }
	
	private void addUser() throws ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(soket.getInputStream());
		User user = (User) in.readObject();
		in.close();
		DataBaseUsers db = new DataBaseUsers();
		try {
			db.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void authentication() throws IOException {
		DataBaseUsers db = new DataBaseUsers();
		List<String> usersNames = null;
		User User = new User();
		try {
			usersNames = db.getUsersNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String user = din.readUTF();
		String pass = din.readUTF();
		
		boolean flagWrongUser = true;
		boolean flagWrongPassword = true;
		
		for (int i = 0; i < usersNames.size(); i++) {
			if (usersNames.get(i).equals(user)) {
				try {
					User = db.getUserByUsername(usersNames.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (User.getPassword().equals(pass)) {
					flagWrongPassword = false;
				} else {
					flagWrongPassword = true;
				}
				flagWrongUser = false;
			}
		}
		
		outD.writeBoolean(flagWrongUser);
		outD.writeBoolean(flagWrongPassword);
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(User);
        out.close();
	}

	private void setImg() throws IOException {
		DataBaseUsers db = new DataBaseUsers();
		int id = din.readInt();
		String img = din.readUTF();
		try {
			db.setImg(id, img);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void sendIdByCategories() throws Exception {
		Categories categ;
		DataBaseWord dw = new DataBaseWord();
		String str = din.readUTF();
		categ = dw.getIdByCategories(str);
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(categ);
        out.close();
	}

	private void sendCategories() throws IOException {
		ArrayList<String> Categories = null;
		DataBaseWord dw = new DataBaseWord();
		try {
			Categories = (ArrayList<String>) dw.getCategories();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(Categories);
        out.close();
	}

	private void getVideo() throws IOException {
		long fileSize = din.readLong();
		String fileName = din.readUTF();
		byte[] buffer = new byte[64 * 1024];
		FileOutputStream outF = new FileOutputStream(fileName);
		int count, total = 0;
		while ((count = din.read(buffer)) != -1) {
			total += count;
			outF.write(buffer, 0, count);
			if (total == fileSize) {
				break;
			}
		}
		outF.flush();
		outF.close();
	}

	private void sendVideo() {
		try {
			String str = din.readUTF();
			try {
				FileInputStream in = new FileInputStream(str);
				byte[] buffer = new byte[64 * 1024];
				int count;

				File file = new File(str);
				System.out.println(file.length());
				outD.writeLong(file.length());

				while ((count = in.read(buffer)) != -1) {
					outD.write(buffer, 0, count);
				}
				outD.flush();
				in.close();
				soket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] arg) {
		new ServerPart();
	}
}
