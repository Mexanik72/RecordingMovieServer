package server;

public class Word implements java.io.Serializable {

	private int id;
	private String word;
	private int rate;
	private String description;
	private String img;
	private int categor;
	
	public Word(int id, String word, int rate, String description, String img, int categor) {
		this.id = id;
		this.word = word;
		this.rate = rate;
		this.description = description;
		this.img = img;
		this.categor = categor;
	}

	public Word() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public int getCategor() {
		return categor;
	}

	public void setCategor(int categor) {
		this.categor = categor;
	}
}
