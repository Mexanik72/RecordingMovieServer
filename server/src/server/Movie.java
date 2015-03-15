package server;

public class Movie implements java.io.Serializable {
	private int id;
	private int owner;
	private String name;
	private int word;

	public Movie(int id, int owner, String name, int word) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.word = word;
	}

	public Movie() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getWord() {
		return word;
	}

	public void setWord(int word) {
		this.word = word;
	}
}
