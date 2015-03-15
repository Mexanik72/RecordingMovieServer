package server;

public class User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -671523514427504421L;
	private int id;
	private String name;
	private String username;
	private String password;
	private int score;
	private String img;

	public User(int id, String name, String username, String password, int score, String img) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.score = score;
		this.img = img;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}

