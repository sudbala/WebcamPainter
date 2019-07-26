import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * 
 * @author Sudharsan Balasubramani, Spring 2019
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder();		// instantiates regionFinder, clears any current painging
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);		//resets painting
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting, 
	 * depending on display variable ('w', 'r', or 'p')
	 * 
	 * @param g		graphics module used to draw image and painting
	 */
	@Override
	public void draw(Graphics g) {
		if(displayMode == 'w') {
			g.drawImage(image, 0, 0, null);												//live webcam image	
		}	
		else if(displayMode == 'r') {
			g.drawImage(finder.getRecoloredImage(), 0, 0, null);						//draws the image with the paintbrush indicated
		}
		else if(displayMode == 'p') {													//draws the image with the painting made by brush
			g.drawImage(painting, 0, 0, null);
		}
		repaint();
	}

	/**
	 * Webcam method, finds the regions, finds the largest one, uses it as a brush, and makes a painting
	 */
	@Override
	public void processImage() {
		ArrayList<Point> brush;															//creates a brush
		if(targetColor != null) {														//checks whether a color has been selected, if not wont work
			finder.setImage(image);														//sets image
			finder.findRegions(targetColor);											//finds regions
			if(finder.largestRegion() != null) {										//checks if largest regions exists within those regions
				brush = finder.largestRegion();
				finder.recolorImage();													//recolors regions
				for(int i = 0; i < brush.size(); i++) {									//only uses largest region as the brush, paints trail wherever brush is
					painting.setRGB(brush.get(i).x, brush.get(i).y, paintColor.getRGB());
				}
			}
		}
		
		
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 * 
	 * @param x		x coordinate of mouse click
	 * @param y		y coordinate of mouse click
	 */
	@Override
	public void handleMousePress(int x, int y) {										//Sets target color based on mouseclick to find regions
		// TODO: YOUR CODE HERE
		if(image != null) {
			targetColor = new Color(image.getRGB(x, y));
			System.out.println("Target Color: " + targetColor);
			
		}
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 * 
	 * @param k		key used to clear, save painting, or choose display mode
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
			switch(k) {
			case 'p':
				System.out.println("Painting");
				break;
			case 'r':
				System.out.println("Recolored Image");
				break;
			case 'w':
				System.out.println("Live Image");
				break;
			}
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {			//Main method
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}