package mathLib.fem.triangulation.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import mathLib.fem.core.Node;
import mathLib.fem.triangulation.DelaunayTriangulator;
import mathLib.fem.triangulation.NotEnoughPointsException;
import mathLib.fem.triangulation.Triangle2D;
import mathLib.fem.triangulation.Vector2D;

public class Test4 extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Node p1 = new Node(1, 5, 5) ;
		Node p2 = new Node(2, 0, 0) ;
		Node p3 = new Node(3, 10, 0) ;
		Node p4 = new Node(4, 1, 2) ;
		Node p5 = new Node(5, 1, -4) ;
		List<Vector2D> pointSet = new ArrayList<>() ;
		pointSet.add(p1) ;
		pointSet.add(p2) ;
		pointSet.add(p3) ;
		pointSet.add(p4) ;
		pointSet.add(p5) ;
		DelaunayTriangulator mesh = new DelaunayTriangulator(pointSet) ;
		try {
			mesh.triangulate();
		} catch (NotEnoughPointsException e) {
			e.printStackTrace();
		}
		
		List<Triangle2D> triangles = mesh.getTriangles() ;
		for(int i=0; i<triangles.size(); i++) {
			System.out.println(triangles.get(i));
			System.out.println(((Node)triangles.get(i).a).globalIndex);
			System.out.println(((Node)triangles.get(i).b).globalIndex);
			System.out.println(((Node)triangles.get(i).c).globalIndex);
		}
			

				
		Set<Line> edges = new HashSet<>() ;
		for(Triangle2D t : triangles) {
			Line l1 = new Line(t.a.x*5+100, t.a.y*5+100, t.b.x*5+100, t.b.y*5+100) ;
			l1.setStroke(Color.RED);
			edges.add(l1) ;
			
			Line l2 = new Line(t.b.x*5+100, t.b.y*5+100, t.c.x*5+100, t.c.y*5+100) ;
			l2.setStroke(Color.RED);
			edges.add(l2) ;
			
			Line l3 = new Line(t.c.x*5+100, t.c.y*5+100, t.a.x*5+100, t.a.y*5+100) ;
			l3.setStroke(Color.RED);
			edges.add(l3) ;
			
		}
		
		List<Circle> points = new ArrayList<>() ;
		for(Vector2D v : pointSet) {
			Circle c = new Circle(v.x*5+100, v.y*5+100, 2) ;
			c.setFill(Color.BLUE);
			points.add(c) ;
		}
		
		
		AnchorPane pane = new AnchorPane() ;
		pane.getChildren().addAll(points) ;
		pane.getChildren().addAll(edges) ;
		Scene scene = new Scene(pane, 600, 400) ;
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
}
