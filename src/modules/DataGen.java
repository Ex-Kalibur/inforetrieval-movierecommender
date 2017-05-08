package modules;

import java.sql.SQLException;
import java.util.Random;

import managers.DataManager;

public class DataGen {

	public static void main(String[] args) {
		DataManager dm = DataManager.getInstance();
		try {
			dm.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Random randChance = new Random();
		Random randRating = new Random();
		
		//for each user
		for(int i = 1; i <= 101; i++){
			//for each movie
			for(int j = 1; j <= 250; j++){
				if(randChance.nextDouble() > 0.8){
					int rating = randRating.nextInt(5) + 1;
					
					//dm.addRatingForMovieByUser(i, j, rating);
				}
			}
			System.out.println("User: " + i);
		}
	}

}
