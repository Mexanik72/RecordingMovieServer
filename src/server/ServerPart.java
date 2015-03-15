package server;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerPart {
	
	private JTextArea area;
	private JProgressBar jpb;
	private JFrame f;

	private DataInputStream din = null;
	private DataOutputStream outD = null;
	private Socket soket = null;

	ServerPart() {
		f = new JFrame("Server");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(200, 250);
		f.setLayout(new BorderLayout());
		area = new JTextArea();
		f.add(area, BorderLayout.CENTER);
		JScrollPane sp = new JScrollPane(area);
		f.getContentPane().add(sp);
		f.setAlwaysOnTop(true);
		f.setVisible(true);
		area.setBackground(Color.LIGHT_GRAY);
		connect();
	}

	public void connect() {
		int port = 2154;
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				area.append("Ожидание подключения...\n");
				jpb = new JProgressBar();
				jpb.setIndeterminate(true);
				jpb.setForeground(Color.black);
				f.add(jpb, BorderLayout.AFTER_LAST_LINE);
				soket = ss.accept();
				InputStream in = soket.getInputStream();
				din = new DataInputStream(in);
				outD = new DataOutputStream(soket.getOutputStream());
				area.append("Передается файл\n");
				area.append("Прием нового файла: \n");
				int flag = din.readInt();

				if (flag == 0)
					sendVideo();
				else if (flag == 1)
					sendButtonIcon();
				else if (flag == 2)
					sendUserIcon();
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
				else
					throw new Exception();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getFiveTopUsers() throws Exception {
		// TODO Auto-generated method stub
		DataBaseScore dbs = new DataBaseScore();
		List<Score> listScores = dbs.getFiveTopUser();
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(listScores);
        out.close();
	}

	private void getScoreById() throws Exception {
		// TODO Auto-generated method stub
		DataBaseScore dbs = new DataBaseScore();
		int userNow = din.readInt();
		int scoreInt = dbs.getScoreByUser(userNow);
		outD.writeInt(scoreInt);
	}

	private void getIdByWords() throws IOException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		DataBaseWord dw = new DataBaseWord();
		ArrayList<String> Words = null;
		int categoryNow = din.readInt();
		
		try {
			Words = (ArrayList<String>) dw.getWordsWhereC(categoryNow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(Words);
        out.close();
	}

	private void addScoreAndMovie() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Movie mv = new Movie();
		DataBaseMovies db = new DataBaseMovies();
		
		String fileName = din.readUTF();
		ObjectInputStream in = new ObjectInputStream(soket.getInputStream());
		User userNow = (User) in.readObject();
		Word wordNow = (Word) in.readObject();
		in.close();
		
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
			//score.setDate(new Date(System.currentTimeMillis()));
			score.setUser(userNow.getId());
			score.setRate(scoreNow);
			dbs.addScore(score);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			db.addMovie(mv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getMoviesIds() throws IOException {
		// TODO Auto-generated method stub
		DataBaseMovies dbmv = new DataBaseMovies();
		List<Integer> ListIdmovies = null;
		try {
			ListIdmovies = dbmv.getMoviesIds();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(ListIdmovies);
        out.close();
	}

	private void getMovieByCategor() throws IOException {
		// TODO Auto-generated method stub
		DataBaseMovies dbm = new DataBaseMovies();
		List<Movie> movies = new ArrayList<Movie>();
		System.out.println("server is ok");
		int categor = din.readInt();
        
		try {
			movies = dbm.getMovieByCategor(categor);
			// words = (ArrayList<Integer>) dbw.getIdByCategories(categor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(movies);
        out.close();
	}

	private void getKeyWords() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		DataBaseMovies dbm = new DataBaseMovies();
		List<String> Key_words = null;
		ObjectInputStream in = new ObjectInputStream(soket.getInputStream());
		Movie movieNow = (Movie) in.readObject();
		in.close();
		int rate = din.readInt();
		String text = din.readUTF();
		try {
			Key_words = dbm.getKeyWords(movieNow.getWord());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean guess = false;
		for (int i = 0; i < Key_words.size(); i++) {
			if (Key_words.get(i).equals(text)) {
				guess = true;

				java.util.Date someDate = Calendar.getInstance()
						.getTime();
				java.sql.Date sqlDate = new java.sql.Date(someDate
						.getTime());
				
				try {
					dbm.addRatetoMovie(movieNow, sqlDate, rate+1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			} else {
				guess = false;
			}
		}
		
		outD.writeBoolean(guess);
	}

	private void addUser() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		ObjectInputStream in = new ObjectInputStream(soket.getInputStream());
		User user = (User) in.readObject();
		in.close();
		DataBaseUsers db = new DataBaseUsers();
		try {
			db.addUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void authentication() throws IOException {
		// TODO Auto-generated method stub
		DataBaseUsers db = new DataBaseUsers();
		List<String> usersNames = null;
		User User = new User();
		try {
			usersNames = db.getUsersNames();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
		DataBaseUsers db = new DataBaseUsers();
		int id = din.readInt();
		String img = din.readUTF();
		try {
			db.setImg(id, img);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void sendIdByCategories() throws Exception {
		// TODO Auto-generated method stub
		Categories categ;
		DataBaseWord dw = new DataBaseWord();
		String str = din.readUTF();
		categ = dw.getIdByCategories(str);
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(categ);
        out.close();
	}

	private void sendCategories() throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> Categories = null;
		DataBaseWord dw = new DataBaseWord();
		try {
			Categories = (ArrayList<String>) dw.getCategories();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(soket.getOutputStream());
        out.writeObject(Categories);
        out.close();
	}

	private void getVideo() throws IOException {
		// TODO Auto-generated method stub
		long fileSize = din.readLong(); // получаем размер файла
		int fsaize = (int) (fileSize / (1024));
		String fileName = din.readUTF(); // прием имени файла
		area.append("Имя файла: " + fileName + "\n");
		area.append("Размер файла: " + fileSize + " байт\n");
		jpb.setMaximum(fsaize);
		jpb.setIndeterminate(false);
		byte[] buffer = new byte[64 * 1024];
		FileOutputStream outF = new FileOutputStream(fileName);
		int count, total = 0;
		while ((count = din.read(buffer)) != -1) {
			total += count;
			outF.write(buffer, 0, count);
			jpb.setValue(total / 1024);
			if (total == fileSize) {
				break;
			}
		}
		outF.flush();
		outF.close();
		area.append("Файл принят\n---------------------------------\n");
		jpb.setVisible(false);
	}

	private void sendUserIcon() {
		// TODO Auto-generated method stub

	}

	private void sendButtonIcon() {
		// TODO Auto-generated method stub

	}

	private void sendVideo() {
		// TODO Auto-generated method stub
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] arg) {
		new ServerPart();
	}
}
