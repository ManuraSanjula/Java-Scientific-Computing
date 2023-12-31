package mathLib.fem.mesh.mesh2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;

import mathLib.fem.core.Node;
import mathLib.fem.triangulation.Vector2D;
import mathLib.geometry.algebra.Point;
import mathLib.util.MathUtils;

public class Mesh2DRectangleElement extends AbstractMesh2DElement {

	/**
	 * p2-------p3
	 * |		|
	 * p1-------p4
	 */

	int defaultDensity = 10;
	double x1, y1, x2, y2, x3, y3, x4, y4  ;
	Point p1, p2, p3, p4;

	ArrayList<Point> points;
	ArrayList<XYAnnotation> annotations;

	public Mesh2DRectangleElement(String name, double xLL, double yLL, double xUR, double yUR) {
		this.elemName = name;
		this.x1 = xLL;
		this.y1 = yLL;
		this.x3 = xUR;
		this.y3 = yUR;
		this.x2 = xLL ;
		this.y2 = yUR ;
		this.x4 = xUR ;
		this.y4 = yLL ;
		this.p1 = Point.getInstance(x1, y1);
		this.p2 = Point.getInstance(x2, y2);
		this.p3 = Point.getInstance(x3, y3);
		this.p4 = Point.getInstance(x4, y4);
		this.points = new ArrayList<>();
		this.annotations = new ArrayList<>();
		createMeshElement();
		createAnnotations();

		// default priority
		meshPriority = "0" ;
	}
	
	public Mesh2DRectangleElement(String name, double xLL, double yLL, double xUR, double yUR, int density) {
		this.elemName = name;
		this.x1 = xLL;
		this.y1 = yLL;
		this.x3 = xUR;
		this.y3 = yUR;
		this.x2 = xLL ;
		this.y2 = yUR ;
		this.x4 = xUR ;
		this.y4 = yLL ;
		this.p1 = Point.getInstance(x1, y1);
		this.p2 = Point.getInstance(x2, y2);
		this.p3 = Point.getInstance(x3, y3);
		this.p4 = Point.getInstance(x4, y4);
		this.points = new ArrayList<>();
		this.annotations = new ArrayList<>();
		this.defaultDensity = density ;
		createMeshElement();
		createAnnotations();

		// default priority
		meshPriority = "0" ;
	}

	public void setMeshPriority(String priority) {
		this.meshPriority = priority ;
	}

	@Override
	public boolean isInside(Vector2D point) {
		Mesh2DTriangleElement tri1 = new Mesh2DTriangleElement("tri1", x1, y1, x2, y2, x3, y3) ;
		Mesh2DTriangleElement tri2 = new Mesh2DTriangleElement("tri1", x1, y1, x3, y3, x4, y4) ;
		if(tri1.isInside(point) || tri2.isInside(point))
			return true ;
		else
			return false ;
	}

	private void createMeshElement() {
		double[] r = MathUtils.linspace(0, 1, defaultDensity);
		double[] s = MathUtils.linspace(0, 1, defaultDensity);
		Point p = p4-p1 ;
		Point q = p2-p1 ;
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < s.length; j++) {
				Point prs = r[i]*p + s[j]*q + p1 ;
				points.add(prs) ;
			}
		}

	}

	private void createAnnotations() {
		XYAnnotation ab = new XYLineAnnotation(p1.getX(), p1.getY(), p2.getX(), p2.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation bc = new XYLineAnnotation(p2.getX(), p2.getY(), p3.getX(), p3.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation cd = new XYLineAnnotation(p3.getX(), p3.getY(), p4.getX(), p4.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation da = new XYLineAnnotation(p4.getX(), p4.getY(), p1.getX(), p1.getY(), new BasicStroke(3f),
				Color.BLACK);
		annotations.add(ab) ;
		annotations.add(bc) ;
		annotations.add(cd) ;
		annotations.add(da) ;
	}

	@Override
	public ArrayList<Vector2D> getNodes() {
		Set<Vector2D> vecSet = new HashSet<>() ;
		ArrayList<Vector2D> vecs = new ArrayList<>();

		for(int i=0; i<points.size(); i++)
			vecSet.add(new Node(i+1, points.get(i).getX(), points.get(i).getY())) ;

		for (Vector2D p : vecSet)
			vecs.add(p) ;

		return vecs ;
	}

	@Override
	public ArrayList<XYAnnotation> getDrawings() {
		return annotations ;
	}

	@Override
	public void refine() {
		defaultDensity = (int) (2 * defaultDensity); // multiply by 2
		// clear previous points
		this.points = new ArrayList<>();
		createMeshElement();
		createAnnotations();
	}

	@Override
	public void refine(int order) {
		defaultDensity = (int) (order * defaultDensity); // multiply by 2
		// clear previous points
		this.points = new ArrayList<>();
		createMeshElement();
		createAnnotations();
	}

}
