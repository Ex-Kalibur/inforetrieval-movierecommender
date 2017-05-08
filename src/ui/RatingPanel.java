package ui;

import javax.swing.JPanel;

import classes.Movie;
import managers.DataManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import javax.swing.Box;

public class RatingPanel extends JPanel {
	
	public RatingPanel(int movieId, int rating){		
		this(DataManager.getInstance().queryMovieById(movieId), rating);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public RatingPanel(Movie movie, int rating){
		setBorder(new EmptyBorder(4, 4, 4, 4));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel movieTitleLabel = new JLabel(movie.getTitle());
		add(movieTitleLabel);

		Component horizontalGlue = Box.createHorizontalGlue();
		add(horizontalGlue);

		JLabel ratingLabel = new JLabel(Utils.getRatingString(rating));
		add(ratingLabel);
	}
}
