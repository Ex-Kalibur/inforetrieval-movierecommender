package ui;

import javax.swing.JPanel;

import classes.Movie;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class MoviePanel extends JPanel{
	private Movie movie;
	
	public MoviePanel(Movie newMovie, double rating){
		setBorder(new EmptyBorder(4, 4, 4, 4));
		this.movie = newMovie;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JPanel movieInfoPanel = new JPanel();
		add(movieInfoPanel);
		movieInfoPanel.setLayout(new BoxLayout(movieInfoPanel, BoxLayout.PAGE_AXIS));
		
		JPanel movieNamePanel = new JPanel();
		movieInfoPanel.add(movieNamePanel);
		movieNamePanel.setLayout(new BoxLayout(movieNamePanel, BoxLayout.LINE_AXIS));
		
		JLabel movieNameLabel = new JLabel(newMovie.getTitle());
		movieNamePanel.add(movieNameLabel);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		movieNamePanel.add(horizontalGlue);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		add(rigidArea_1);
		
		JLabel ratingLabel = new JLabel(Utils.getRatingString((int)rating));
		add(ratingLabel);
		
		
	}
}
