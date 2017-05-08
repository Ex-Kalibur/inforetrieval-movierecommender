package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import classes.Movie;
import classes.MovieRating;
import classes.MovieRecommendation;
import classes.User;

public class DataManager {
	private static DataManager instance = null;
	private static Connection con = null;
	
	private DataManager(){}
	
	public static DataManager getInstance(){
		if(instance == null){
			instance = new DataManager();
			
		}
		
		return instance;
	}
	
	public void getConnection() throws SQLException
    {	
		if(con == null)
		{
			String host = "jdbc:sqlite:res/RecommenderSystem.db";
			try{
				Class.forName("org.sqlite.JDBC");
			} catch(ClassNotFoundException e){
				System.err.println("Could not find SQLite JDBC Driver");
			}
			con = DriverManager.getConnection(host);
			System.out.println("Successfully Connected to database");
		}
		else
			System.err.println("Error: Already Connected");
    }
    
    public void closeConnection(){
        try{
            if(con != null){
            	con.close();
            }
        } catch(SQLException err) {
            System.out.println(err.getMessage());
            err.printStackTrace();
        }
    	System.out.println("Successfully Closed the database");
    }
    
	private ResultSet runQuery(String query){
		//System.out.println("Running query:" + query);
        Statement statement;
        ResultSet rs = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return rs;
	}
	
	private ArrayList<Integer> runUpdateQuery(String query){
		//System.out.println("Running query:" + query);
		ArrayList<Integer> ids = new ArrayList<>();
		Statement statement;
		try {
			statement = con.createStatement();
			statement.executeUpdate(query);
			
			ResultSet rs = statement.getGeneratedKeys();
			ids = new ArrayList<Integer>();
			
			while(rs.next()){
				ids.add(rs.getInt("last_insert_rowid()"));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return ids;
	}
	
	/*---------------------------------------------------------------\
	| QUERY HANDELERS                                                |
	\---------------------------------------------------------------*/
	
	//MOVIE MANAGER
	public ArrayList<Movie> queryMovies(){
		ArrayList<Movie> movies = new ArrayList<>();
		try {
			movies = doQueryMovies();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return movies;
	}
	
	public Movie queryMovieById(int id){
		Movie movie = null;
		try {
			movie = doQueryMovieById(id);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return movie;
	}
	
	//USER MANAGER
	public ArrayList<User> queryUsers(){
		ArrayList<User> users = new ArrayList<>();
		try {
			users = doQueryUsers();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return users;
	}
	
	public User queryUserById(int id){
		User user = null;
		try {
			user = doQueryUserById(id);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return user;
	}
	
	//RATING MANAGER
	public ArrayList<MovieRating> queryRatingsByUserId(int userId){
		ArrayList<MovieRating> ratings = new ArrayList<>();
		try {
			ratings = doQueryRatingsByUserId(userId);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return ratings;
	}
	
	public ArrayList<MovieRating> queryRatingsByMovieId(int movieId){
		ArrayList<MovieRating> ratings = new ArrayList<>();
		try {
			ratings = doQueryRatingsByMovieId(movieId);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return ratings;
	}
	
	public void addRatingForMovieByUser(int userId, int movieId, int rating){
		try {
			doAddRatingForMovieByUser(userId, movieId, rating);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*---------------------------------------------------------------\
	| SQL QUERIES                                                    |
	\---------------------------------------------------------------*/
	
	//Movies
	private Movie getMovie(ResultSet rs) throws SQLException{
		Movie movie = new Movie();
		
		movie.setId(rs.getInt(Movie.COLUMN_MOVIE_ID));
		movie.setTitle(rs.getString(Movie.COLUMN_MOVIE_TITLE));
		
		return movie;
	}
	
	private ArrayList<Movie> doQueryMovies() throws SQLException{
		ArrayList<Movie> movies = new ArrayList<Movie>();
		String query = "SELECT * FROM " + Movie.TABLE_MOVIES;
		
		ResultSet rs = runQuery(query);
		while(rs.next()){
			movies.add(getMovie(rs));
		}
		
		return movies;
	}
	
	private Movie doQueryMovieById(int id) throws SQLException{
		Movie movie = null;
		String query = "SELECT * FROM " + Movie.TABLE_MOVIES + " WHERE " + Movie.COLUMN_MOVIE_ID + " = " + id;
		
		ResultSet rs = runQuery(query);
		if(rs.next()){
			movie = getMovie(rs);
		}
		
		return movie;
	}
	
	//Users
	private User getUser(ResultSet rs) throws SQLException{
		User user = new User();
		
		user.setId(rs.getInt(User.COLUMN_USER_ID));
		user.setName(rs.getString(User.COLUMN_USER_NAME));
		
		return user;
	}
	
	private ArrayList<User> doQueryUsers() throws SQLException{
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM " + User.TABLE_USERS;
		
		ResultSet rs = runQuery(query);
		while(rs.next()){
			users.add(getUser(rs));
		}
		
		return users;		
	}
	
	private User doQueryUserById(int id) throws SQLException{
		User user = null;
		String query = "SELECT * FROM " + User.TABLE_USERS + " WHERE " + User.COLUMN_USER_ID +  " = " + id;
		
		ResultSet rs = runQuery(query);
		if(rs.next()){
			user = getUser(rs);
		}
		
		return user;
	}
	
	//MovieRating
	private MovieRating getMovieRating(ResultSet rs) throws SQLException{
		MovieRating rating = new MovieRating();
		
		rating.setUserId(rs.getInt(MovieRating.COLUMN_RATING_USER_ID));
		rating.setMovieId(rs.getInt(MovieRating.COLUMN_RATING_MOVIE_ID));
		rating.setRating(rs.getShort(MovieRating.COLUMN_RATING_RATING));
		
		return rating;
	}
	
	private ArrayList<MovieRating> doQueryRatingsByUserId(int id) throws SQLException{
		ArrayList<MovieRating> ratings = new ArrayList<>();
		String query = "SELECT * FROM " + MovieRating.TABLE_RATINGS + " WHERE " + MovieRating.COLUMN_RATING_USER_ID + " = " + id + " ORDER BY " + MovieRating.COLUMN_RATING_MOVIE_ID;
		
		ResultSet rs = runQuery(query);
		while(rs.next()){
			ratings.add(getMovieRating(rs));
		}
		
		return ratings;		
	}
	
	private ArrayList<MovieRating> doQueryRatingsByMovieId(int id) throws SQLException{
		ArrayList<MovieRating> ratings = new ArrayList<>();
		String query = "SELECT * FROM " + MovieRating.TABLE_RATINGS + " WHERE " + MovieRating.COLUMN_RATING_MOVIE_ID + " = " + id + " ORDER BY " + MovieRating.COLUMN_RATING_USER_ID;;
		
		ResultSet rs = runQuery(query);
		while(rs.next()){
			ratings.add(getMovieRating(rs));
		}
		
		return ratings;		
	}
	
	private void doAddRatingForMovieByUser(int userId, int movieId, int rating) throws SQLException{
		String query = "INSERT INTO " + MovieRating.TABLE_RATINGS + " ( " + MovieRating.COLUMN_RATING_USER_ID + ", " 
				+ MovieRating.COLUMN_RATING_MOVIE_ID + ", " + MovieRating.COLUMN_RATING_RATING + " ) VALUES ( " 
				+ userId + ", " + movieId + ", " + rating + ")";
		
		runUpdateQuery(query);
	}
	
	/*---------------------------------------------------------------\
	| RECOMMENDER SYSTEM                                             |
	\---------------------------------------------------------------*/
	
	private static final double SIM_SCORE_THRESHOLD = 0.3;
	private static final double UTILITY_SCORE_THRESHOLD = 4.0;
	
	public ArrayList<MovieRecommendation> calculateRecommendations(int userId){
		ArrayList<MovieRecommendation> recommendations = new ArrayList<>();

		//movieId, movierating
		Multimap<Integer, MovieRating> unratedMovieMap = HashMultimap.create();
		
		ArrayList<User> users = queryUsers();
		ArrayList<MovieRating> myRatings = queryRatingsByUserId(userId);
		
		System.out.println("Similarity Scores:");
		for(User user : users){
			if(user.getId() != userId){
				ArrayList<MovieRating> unratedUserMovies = new ArrayList<>();
				
				ArrayList<MovieRating> userRatings = queryRatingsByUserId(user.getId());
				ArrayList<MovieRating> userSimRatings = new ArrayList<>();
				ArrayList<MovieRating> mySimRatings = new ArrayList<>();
				for(MovieRating userRating : userRatings){
					
					MovieRating ra = findRating(myRatings, userRating);
					if(ra != null){
						userSimRatings.add(userRating);
						mySimRatings.add(ra);
					} else {
						unratedUserMovies.add(userRating);
					}
				}
				
				double simScore = calculateSimScore(mySimRatings, userSimRatings);
				if(Double.isNaN(simScore)) simScore = 0;
				
				System.out.println("\t" + user.getName() + "   " + simScore);
				if(simScore > SIM_SCORE_THRESHOLD){
										
					for(MovieRating r : unratedUserMovies){
						r.setUserSim(simScore);
						unratedMovieMap.put(r.getMovieId(), r);
					}
				}
			}
		}
		
		//Calculate predicted movie scores
		System.out.println("Predicted Ratings:");
		for(Integer movieId : unratedMovieMap.keySet()){
			Collection<MovieRating> ratings = unratedMovieMap.get(movieId);
			
			double utilityScore = calculateUtilityScore(ratings);
			if(Double.isNaN(utilityScore)) utilityScore = 0;
			if(utilityScore >= UTILITY_SCORE_THRESHOLD){
				Movie movie = queryMovieById(movieId);
				
				System.out.println("\t" + movie.getTitle() + "   " + utilityScore);
				recommendations.add(new MovieRecommendation(movie, utilityScore) );
			}
		}

		Collections.sort(recommendations);
		return recommendations;
	}
	
	private MovieRating findRating(ArrayList<MovieRating> ratings, MovieRating rating){
		for(MovieRating r : ratings){
			if(r.getMovieId() == rating.getMovieId()) return r;
		}
		
		return null;
	}
	
	private double calculateSimScore(ArrayList<MovieRating> myRatings, ArrayList<MovieRating> userRatings){
		double myAverageScore = 0;
		for(MovieRating r : myRatings){
			myAverageScore += r.getRating();
		} 
		myAverageScore /= myRatings.size();
		
		double userAverageScore = 0;
		for(MovieRating r : userRatings){
			userAverageScore += r.getRating();
		} 
		userAverageScore /= userRatings.size();
		
		double myLength = 0;
		double userLength = 0;
		double acc = 0;
		for(int i = 0; i < myRatings.size(); i++){
			MovieRating myRating = myRatings.get(i);
			MovieRating userRating = userRatings.get(i);
			
			acc += (myRating.getRating() - myAverageScore)*(userRating.getRating() - userAverageScore);
			myLength += Math.pow((myRating.getRating() - myAverageScore), 2);
			userLength += Math.pow((userRating.getRating() - userAverageScore), 2);
		}
		myLength = Math.sqrt(myLength);
		userLength = Math.sqrt(userLength);
		
		return acc/(myLength*userLength);
	}
	
	private double calculateUtilityScore(Collection<MovieRating> ratings){
		double sumSim = 0;
		double weightedScores = 0;
	
		
		for(MovieRating rating : ratings){
			sumSim += rating.getUserSim();
			weightedScores += rating.getUserSim() * rating.getRating();
		}
		return weightedScores / sumSim;
	}
}
