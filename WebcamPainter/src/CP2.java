import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam processing program that uses multiple colors and pausing features to paint a painting with a paintbrush region.
 * 
 * @author Sudharsan Balasubramani, Dartmouth CS 10, Spring 2019
 */
public class CP2 extends Webcam {
	private char displayMode = 'w'; // what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RF2 finder; // handles the finding
	private Color targetColor; // color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue; // the color to put into the painting from the "brush"
	private BufferedImage painting; // the resulting masterpiece
	boolean pause = false;

	/**
	 * Initializes the region finder and the drawing
	 */
	public CP2() {
		finder = new RF2();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or
	 * painting, depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		if (displayMode == 'w') {
			g.drawImage(image, 0, 0, null);
		} else if (displayMode == 'r') {
			g.drawImage(finder.getRecoloredImage(), 0, 0, null);
		} else if (displayMode == 'p') {
			g.drawImage(painting, 0, 0, null);
		}
		repaint();
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		// TODO: YOUR CODE HERE
		ArrayList<Point> brush;
		if (targetColor != null) {
			finder.setImage(image);
			finder.findRegions(targetColor);
			if (finder.largestRegion() != null) {
				brush = finder.largestRegion();
				finder.recolorImage();
				if (!pause) {
					for (int i = 0; i < brush.size(); i++) {
						painting.setRGB(brush.get(i).x, brush.get(i).y, paintColor.getRGB());
					}
				}
			}
		}

	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE
		if (image != null) {
			targetColor = new Color(image.getRGB(x, y));
			System.out.println("Target Color: " + targetColor);

		}
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
			switch (k) {
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
		} else if (k == 'c') { // clear
			clearPainting();
		} else if (k == ' ') {
			pause = !pause;
		} else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		} else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		} 
		else if (k >= 48 && k <= 57){
			switch(k) {
			case '1':
				paintColor = Color.black;
				break;
			case '2':
				paintColor = Color.blue;
				break;
			case '3':
				paintColor = Color.cyan;
				break;
			case '4':
				paintColor = Color.gray;
				break;
			case '5':
				paintColor = Color.green;
				break;
			case '6':
				paintColor = Color.magenta;
				break;
			case '7':
				paintColor = Color.orange;
				break;
			case '8':
				paintColor = Color.pink;
				break;
			case '9':
				paintColor = Color.red;
				break;
			case '0':
				paintColor = Color.white;
				break;
			
			}
		}else {
			System.out.println("unexpected key " + k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}