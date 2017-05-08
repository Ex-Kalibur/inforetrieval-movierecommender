package modules;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import classes.Movie;
import classes.MovieRating;
import classes.MovieRecommendation;
import managers.DataManager;
import ui.MoviePanel;
import ui.RatingPanel;
import ui.Utils;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.Color;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class RecommenderSystem {
	private static final DataManager dm = DataManager.getInstance();
	
	public static String userName = "Gianluca";
	public static int id = 1;
	public static JLabel nameLabel;
	
	public static JPanel panel;
	public static JPanel panel_1;
	
	public static JComboBox<Movie> movieComboBox;
	public static JSpinner ratingSpinner;

	public static JFrame frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        initUI();
		        
		        populateRecommendations();
		        populateMovies();
		        populateRatings();
		    }
		});
	}
	
	public static void initUI(){
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		    
		    UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial", Font.PLAIN, 16));
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		try {
			dm.getConnection();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		frame = new JFrame();
		frame.setTitle("RecommenderSystem");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
				
		JPanel contentPanel = new JPanel();
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel welcomePanel = new JPanel();
		contentPanel.add(welcomePanel, BorderLayout.NORTH);
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.LINE_AXIS));
		
		
		Component rigidArea_4 = Box.createRigidArea(new Dimension(20, 20));
		welcomePanel.add(rigidArea_4);
		
		JLabel welcomeLabel = new JLabel("Welcome back ");
		welcomePanel.add(welcomeLabel);
		
		Component rigidArea = Box.createRigidArea(new Dimension(5, 0));
		welcomePanel.add(rigidArea);
		
		nameLabel = new JLabel(userName);
		welcomePanel.add(nameLabel);
		
		JPanel userButtonWrapPanel = new JPanel();
		userButtonWrapPanel.setLayout(new BorderLayout());
		JButton userButton = new JButton("Change User");
		userButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				userPopUp();
			}
		});
		userButtonWrapPanel.add(userButton,BorderLayout.EAST);
		welcomePanel.add(userButtonWrapPanel,BorderLayout.EAST);
				
		Component horizontalGlue = Box.createHorizontalGlue();
		welcomePanel.add(horizontalGlue);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setSelectedIndex(-1);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel recommendTab = new JPanel();
		recommendTab.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 4, 4), new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Based on your ratings, here are some recommended movies:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8))));
		tabbedPane.addTab("My Recommendations", null, recommendTab, null);
		recommendTab.setLayout(new BoxLayout(recommendTab, BoxLayout.PAGE_AXIS));
		
		JPanel panel_2 = new JPanel();
		recommendTab.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.LINE_AXIS));
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				populateRecommendations();
				frame.revalidate();
				frame.repaint();
			}
		});
		panel_2.add(refreshButton);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel_2.add(horizontalGlue_1);
		
		JScrollPane recommendationScrollPane = new JScrollPane();
		recommendationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		recommendationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		recommendTab.add(recommendationScrollPane);
		
		JPanel recommendationPanel = new JPanel();
		recommendationScrollPane.setViewportView(recommendationPanel);
		recommendationPanel.setLayout(new BoxLayout(recommendationPanel, BoxLayout.PAGE_AXIS));
		
		panel = new JPanel();
		recommendationPanel.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		recommendationPanel.add(verticalGlue);
		
		JPanel ratingTab = new JPanel();
		tabbedPane.addTab("My Ratings", null, ratingTab, null);
		ratingTab.setLayout(new BorderLayout(0, 0));
		
		JPanel addRatingPanel = new JPanel();
		addRatingPanel.setBorder(new CompoundBorder(new EmptyBorder(4, 4, 0, 4), new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Add Rating:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8))));
		ratingTab.add(addRatingPanel, BorderLayout.NORTH);
		addRatingPanel.setLayout(new BoxLayout(addRatingPanel, BoxLayout.LINE_AXIS));
		
		JLabel movieTitleLabel = new JLabel("Movie Title:");
		addRatingPanel.add(movieTitleLabel);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		addRatingPanel.add(rigidArea_2);
		
		movieComboBox = new JComboBox<Movie>();
		addRatingPanel.add(movieComboBox);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		addRatingPanel.add(horizontalGlue_2);
		
		JLabel ratingLabel = new JLabel("Rating:");
		addRatingPanel.add(ratingLabel);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		addRatingPanel.add(rigidArea_3);
		
		ratingSpinner = new JSpinner();
		ratingSpinner.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		addRatingPanel.add(ratingSpinner);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		addRatingPanel.add(rigidArea_1);
		
		JButton addButton = new JButton("Add Rating");
		addRatingPanel.add(addButton);
		
		JPanel ratingsPanel = new JPanel();
		ratingsPanel.setBorder(new CompoundBorder(new EmptyBorder(4, 4, 0, 4), new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rated Movies:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8))));
		ratingTab.add(ratingsPanel);
		ratingsPanel.setLayout(new BoxLayout(ratingsPanel, BoxLayout.LINE_AXIS));
		
		addButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Movie movie = (Movie) movieComboBox.getSelectedItem();
				int rating = (Integer) ratingSpinner.getValue();
				addRating(movie, rating);
			}	
		});
		
		
		JScrollPane ratingsScrollPane = new JScrollPane();
		ratingsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ratingsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ratingsPanel.add(ratingsScrollPane);
		
		JPanel ratingPanel = new JPanel();
		ratingsScrollPane.setViewportView(ratingPanel);
		ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.PAGE_AXIS));
		
		panel_1 = new JPanel();
		ratingPanel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		ratingPanel.add(verticalGlue_1);
		
		//frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void populateRecommendations(){
		System.out.println("Populating Recommendations");
		panel.removeAll();
		ArrayList<MovieRecommendation> recommendations = dm.calculateRecommendations(id);
		
		System.out.println("Final Recommendations:");
		for(int i = 0; i < 15; i++){
			if(i < recommendations.size()){
				MovieRecommendation recommendation = recommendations.get(i);
				MoviePanel moviePanel = new MoviePanel(recommendation.getMovie(), recommendation.getRating());
				panel.add(moviePanel);
				//panel.add(new JSeparator());
				System.out.println("\t" + recommendation.getMovie().getTitle() + "   " + recommendation.getRating());
			}
		}
		
		frame.invalidate();
		frame.repaint();
	}
	
	public static void populateMovies(){
		ArrayList<Movie> movies = dm.queryMovies();
		
		Collections.sort(movies, new Comparator<Object>(){

			@Override
			public int compare(Object arg0, Object arg1) {
				
				return ((Movie)arg0).getTitle().compareTo(((Movie)arg1).getTitle());
			}
			
		});
		
		for(int i = 0; i < movies.size(); i++){
			movieComboBox.addItem(movies.get(i));
		}
		
		movieComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Movie){
                    Movie movie = (Movie) value;
                    setText(movie.getTitle());
                }
                return this;
            }
        });
	}

	public static void populateRatings(){
		panel_1.removeAll();
		ArrayList<MovieRating> ratings = dm.queryRatingsByUserId(id);
		
		for(MovieRating rating : ratings){
			RatingPanel newRatingPanel = new RatingPanel(rating.getMovieId(), rating.getRating());
			panel_1.add(newRatingPanel);
			//panel_1.add(new JSeparator());
		}
		frame.invalidate();
		frame.repaint();
	}
	
	public static void addRating(Movie movie, int rating){
		if(movie != null){
			dm.addRatingForMovieByUser(id, movie.getId(), rating);
			RatingPanel newRatingPanel = new RatingPanel(movie, rating);
			panel_1.add(newRatingPanel);
			//panel_1.add(new JSeparator());
			frame.invalidate();
			frame.repaint();
		}
	}
	public static JComboBox<String> makeUserComboBox(){
		JComboBox<String> tempBox = new JComboBox<String>();
		tempBox.addItem("Gianluca");
		tempBox.addItem("[Blank User]");
		tempBox.addItem("Jocelyn");
		tempBox.addItem("Brock");
		tempBox.addItem("Belle");
		return tempBox;
	}
	public static void userPopUp(){
		JFrame userFrame = new JFrame();
		userFrame.setTitle("Choose User");
		userFrame.setSize(500,100);
		userFrame.setLocationRelativeTo(frame);;
		//userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel userPanel = new JPanel(); 
		userFrame.getContentPane().add(userPanel, BorderLayout.CENTER);
		JLabel userLabel = new JLabel("Please choose user");
		JComboBox<String> userComboBox =  makeUserComboBox();
		JButton changeButton = new JButton("OK");
		changeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				id = userComboBox.getSelectedIndex()+1;
				userName = userComboBox.getSelectedItem().toString();
				System.out.println(userName + " " + id);
				userFrame.dispose();
				nameLabel.setText(userName);
				frame.repaint();
				populateRecommendations();
		        populateRatings();
			}
		});
		userPanel.add(userLabel);
		userPanel.add(userComboBox);
		userPanel.add(changeButton);
		userFrame.setVisible(true);
	}
}
