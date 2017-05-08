package classes;

public class MovieRating {

	public static final String TABLE_RATINGS = "ratings";
	public static final String COLUMN_RATING_USER_ID = "user_id";
	public static final String COLUMN_RATING_MOVIE_ID = "movie_id";
	public static final String COLUMN_RATING_RATING = "rating";
	
	private int movieId;
	private int userId;
	private short rating;
	private double userSim;
	
	public MovieRating(){
		this.movieId = -1;
		this.userId = -1;
		this.rating = 0;
		this.userSim = 0;
	}
	
	public MovieRating(int userId, int movieId, short rating){
		this.movieId = movieId;
		this.userId = userId;
		this.rating = rating;
		this.userSim = 0;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public short getRating() {
		return rating;
	}

	public void setRating(short rating) {
		this.rating = rating;
	}

	public double getUserSim() {
		return userSim;
	}

	public void setUserSim(double userSim) {
		this.userSim = userSim;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + movieId;
		result = prime * result + userId;
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
		MovieRating other = (MovieRating) obj;
		if (movieId != other.movieId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "MovieRating [userId=" + userId + ", movieId=" + movieId + ", rating=" + rating + "]";
	}
}
