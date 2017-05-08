package classes;

public class MovieRecommendation implements Comparable<MovieRecommendation>{
	private Movie movie;
	private double rating;
	
	public MovieRecommendation(Movie movie, double rating) {
		super();
		this.movie = movie;
		this.rating = rating;
	}

	public Movie getMovie() {
		return movie;
	}

	public double getRating() {
		return rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movie == null) ? 0 : movie.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rating);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		MovieRecommendation other = (MovieRecommendation) obj;
		if (movie == null) {
			if (other.movie != null)
				return false;
		} else if (!movie.equals(other.movie))
			return false;
		if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MovieRecommendation [movie=" + movie.getTitle() + ", rating=" + rating + "]";
	}

	@Override
	public int compareTo(MovieRecommendation o) {
		if(this.getRating() == o.getRating())
			return Integer.compare(this.getMovie().getId(), o.getMovie().getId());
		return -Double.compare(this.getRating(), o.getRating());
	}
}
