import java.awt.Point;
import java.util.ArrayList;

public class TestRegions {

	public static void main(String[] args) {
		ArrayList<ArrayList<Point>> Buree = new ArrayList<ArrayList<Point>>();
		
		ArrayList<Point> a = new ArrayList<Point>();
		a.add(new Point(1,2));
		a.add(new Point(3,4));
		Buree.add(a);
		
		ArrayList<Point> b = new ArrayList<Point>();
		b.add(new Point(1,2));
		b.add(new Point(3,4));
		b.add(new Point(3,4));
		b.add(new Point(3,4));
		Buree.add(b);
		
		ArrayList<Point> c = new ArrayList<Point>();
		c.add(new Point(1,2));
		c.add(new Point(3,4));
		c.add(new Point(3,4));
		Buree.add(c);
		
		for(ArrayList<Point> test: Buree) {
			for(int i = 0; i < test.size(); i++) {
				System.out.print("(" + test.get(i).x + "," + test.get(i).y+") ");
			}
			System.out.println();
		}
		
		
	}
}
