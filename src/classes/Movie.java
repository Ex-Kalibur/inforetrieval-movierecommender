package classes;

public class Movie {
	public static final String TABLE_MOVIES = "movies";
	public static final String COLUMN_MOVIE_ID = "_id";
	public static final String COLUMN_MOVIE_TITLE = "title";
	
	private int id;
	private String title;
	
	public Movie(int id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	public Movie(){
		this.id = -1;
		this.title = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + "]";
	}
}
