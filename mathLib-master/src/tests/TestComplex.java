package tests;

import static mathLib.numbers.Complex.j;
import static mathLib.numbers.ComplexMath.PI;
import static mathLib.numbers.ComplexMath.cos;
import static mathLib.numbers.ComplexMath.sin;

import mathLib.numbers.Complex;

public class TestComplex {
	public static void main(String[] args) {
		Complex u = 2+j ;
		Complex v = sin(u)*sin(u) + cos(u)*cos(u) ;
		System.out.println(v);
		System.out.println(sin(PI/2));
		System.out.println(sin(PI/4));
		Complex w = 1.2 ;
		System.out.println(w);
	}
}
