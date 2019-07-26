import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Modified with multiple colors, and pause feature
 * 
 * @author Sudharsan Balasubramani, CS 10, Spring 2019
 */
public class RF2 {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RF2() {
		this.image = null;
	}

	public RF2(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		regions = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> toVisit = new ArrayList<Point>();
		
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				if(visited.getRGB(x, y) == 0 && colorMatch(targetColor, new Color(image.getRGB(x, y)))) {
					ArrayList<Point> region = new ArrayList<Point>();
					toVisit.add(new Point(x,y));
					while(!toVisit.isEmpty()) {
						Point pixel = toVisit.get(toVisit.size()-1);
						region.add(pixel);
						visited.setRGB(pixel.x, pixel.y, 1);
						toVisit.remove(toVisit.size()-1);
						for(int py = pixel.y-1; py <= pixel.y+1; py++) {
							for(int px = pixel.x-1; px <= pixel.x+1; px++) {
								if(py >= 0 && py < image.getHeight() && px >= 0 && px < image.getWidth()) {
									if(visited.getRGB(px, py) == 0 && colorMatch(targetColor, new Color(image.getRGB(px, py)))) {
										toVisit.add(new Point(px,py));
									}
								}
							}
						}
					}
					if(region.size() >= minRegion) {
						regions.add(region);
					}
				}
			}
		}		
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		boolean red = Math.abs(c1.getRed()-c2.getRed())<=maxColorDiff;
		boolean green = Math.abs(c1.getGreen()-c2.getGreen())<=maxColorDiff;
		boolean blue = Math.abs(c1.getBlue()-c2.getBlue())<=maxColorDiff;
		
		return red && green && blue;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		ArrayList<Point> largest = null;
		
		if(regions.size() != 0) {
			largest = regions.get(0);
			if(regions.size() > 1) {
				for(ArrayList<Point> large : regions) {
					if(large.size() > largest.size()) {
						largest = large;
					}
				}
			}
		}
		
		return largest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
		for(ArrayList<Point> region: regions) {
			Color recolor = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
			for(Point p: region) {
				recoloredImage.setRGB(p.x, p.y, recolor.getRGB());
			}
		}
	}
}