package mathLib.numbers;

import mathLib.util.StringUtils;

public class Complex { // immutable class

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex plusJ = new Complex(0, 1);
	public static final Complex minusJ = new Complex(0, -1);
	public static final Complex j = new Complex(0, 1);

	private final double re; // the real part
	private final double im; // the imaginary part

	public Complex(double real, double imag) {
		re = real;
		im = imag;
	}

	@Override
	public String toString() {
		String stReal = String.format("%.4f", re) ;
		String stImag = String.format("%.4f", Math.abs(im)) ;
		if (im == 0)
			return stReal + "";
		if (re == 0 && im > 0)
			return "i" + stImag;
		if (re == 0 && im < 0)
			return "-i" + stImag;
		if (im < 0)
			return stReal + "-" + "i" + stImag;
		return stReal + "+" + "i" + stImag;
	}

	public double abs() {
		return Math.hypot(re, im); // Math.sqrt(re*re + im*im)
	}

	public double absSquared() {
		return (Math.hypot(re, im) * Math.hypot(re, im));
	}

	public double phase() {
		double restrictedPhase = Math.atan2(im, re);
		if (restrictedPhase >= 0) {
			return restrictedPhase;
		} else {
			return (restrictedPhase + 2 * Math.PI);
		}
	}

	public double phaseMinusPiToPi() {
		return Math.atan2(im, re);
	}

	public double phaseDegree() {
		return phase() * 180 / Math.PI;
	}

	public Complex plus(Complex b) {
		Complex a = this; // invoking object
		double real = a.re + b.re;
		double imag = a.im + b.im;
		return new Complex(real, imag);
	}

	public Complex plus(double b) {
		Complex a = this; // invoking object
		double real = a.re + b;
		double imag = a.im + 0;
		return new Complex(real, imag);
	}

	public Complex minus(double b) {
		Complex a = this;
		double real = a.re - b;
		double imag = a.im - 0;
		return new Complex(real, imag);
	}

	public Complex minus(Complex b) {
		Complex a = this;
		double real = a.re - b.re;
		double imag = a.im - b.im;
		return new Complex(real, imag);
	}

	public Complex times(Complex b) {
		Complex a = this;
		double real = a.re * b.re - a.im * b.im;
		double imag = a.re * b.im + a.im * b.re;
		return new Complex(real, imag);
	}

	public Complex times(double alpha) {
		return new Complex(alpha * re, alpha * im);
	}

	public Complex conjugate() {
		return new Complex(re, -im);
	}

	public Complex reciprocal() {
		double scale = re * re + im * im;
		return new Complex(re / scale, -im / scale);
	}

	public double re() {
		return re;
	}

	public double im() {
		return im;
	}

	public Complex divides(Complex b) {
		Complex a = this;
		return a.times(b.reciprocal());
	}

	public Complex divides(double alpha) {
		Complex a = this;
		return a.times(1 / alpha);
	}

	public Complex exp() {
		return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
	}

	public Complex sin() {
		return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
	}

	public Complex cos() {
		return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
	}

	public Complex tan() {
		return sin().divides(cos());
	}

	public Complex pow(double p) {
		double magPowered = Math.pow(this.abs(), p);
		double phasePowered = this.phase() * p;
		double real = magPowered * Math.cos(phasePowered);
		double imag = magPowered * Math.sin(phasePowered);
		Complex powResult = new Complex(real, imag);
		return powResult;
	}

	public boolean equals(Complex b) {
		Complex a = this;
		if (a.re() == b.re() && a.im() == b.im())
			return true;
		else
			return false;
	}
	
	public boolean equals(double b) {
		Complex a = this;
		if (a.im() == 0.0 && a.re() == b)
			return true;
		else
			return false;
	}
	
	public boolean equals(Complex b, double tol) {
		Complex a = this;
		if (ComplexMath.abs(a-b)/ComplexMath.abs(b)<tol)
			return true;
		else
			return false;
	}

	public static Complex plus(Complex a, Complex b) {
		double real = a.re + b.re;
		double imag = a.im + b.im;
		Complex sum = new Complex(real, imag);
		return sum;
	}

	public Complex sqrt() {
		Complex a = this;
		double real = Math.sqrt(a.abs()) * Math.cos(a.phase() / 2);
		double imag = Math.sqrt(a.abs()) * Math.sin(a.phase() / 2);
		return new Complex(real, imag);
	}
	
	public static flanagan.complex.Complex toFlanaganComplex(Complex v) {
		return new flanagan.complex.Complex(v.re, v.im) ;
	}
	
	public flanagan.complex.Complex getFlanaganComplex() {
		return new flanagan.complex.Complex(this.re, this.im) ;
	}
	
	public static Complex toNativeComplex(flanagan.complex.Complex v) {
		return new Complex(v.getReal(), v.getImag()) ;
	}

	public static boolean isNumeric(String st) {
		st = st.trim();
		st = st.replaceAll("\\s+", "");
		boolean testNull = st != null;
		boolean test1 = st.matches("[-+]?\\d*\\.?\\d*[+-]i?\\d*\\.?\\d*");
		boolean test2 = st.matches("[-+]?\\d*\\.?\\d*[+-]?\\d*\\.?\\d*?i");
		boolean test3 = st.matches("[-+]?\\d*\\.?\\d*[+-]j?\\d*\\.?\\d*");
		boolean test4 = st.matches("[-+]?\\d*\\.?\\d*[+-]?\\d*\\.?\\d*?j");
		boolean test5 = st.matches("[-+]?\\d*\\.?\\d*");
		boolean test6 = st.matches("[-+]?i\\d*\\.?\\d*");
		boolean test7 = st.matches("[-+]?j\\d*\\.?\\d*");
		boolean test8 = st.matches("[-+]?\\d*\\.?\\d*i");
		boolean test9 = st.matches("[-+]?\\d*\\.?\\d*j");
		boolean result = testNull && (test1 || test2 || test3 || test4 || test5 || test6 || test7 || test8 || test9);
		return result;
	}

	public static Complex parseComplex(String st) {
		boolean hasJ = st.contains("j");
		boolean hasI = st.contains("i");
		st = st.trim();
		st = st.replaceAll("\\s+", "");
		if (/*isNumeric(st) && */ (hasI || hasJ)) {
			double realPart = 0;
			double imagPart = 0;
			String[] num = null;
			if (!hasJ) {
				num = st.split("i");
			} else {
				num = st.split("j");
			}
			String real = num[0];
			String imag = num[1];
			// the correct format is "a + j b" or " a + i b"
			if (real.charAt(real.length() - 1) == '+') {
				imagPart = StringUtils.evaluate(imag);
			} else {
				imagPart = -StringUtils.evaluate(imag);
			}
			if (real.charAt(0) == '-') {
				realPart = -StringUtils.evaluate(real.replaceAll("[+-]", ""));
			} else {
				realPart = StringUtils.evaluate(real.replaceAll("[+-]", ""));
			}
			return new Complex(realPart, imagPart);
		} else if (StringUtils.isNumeric(st)) {
			return new Complex(StringUtils.evaluate(st), 0);
		} else {
			return null;
		}
	}

	// ************ operator overloading **********************

	/**
	 * Operator overloading support (must be implemented as static)
	 *
	 * Object a = 5;
	 *
	 */
	public static Complex valueOf(int v) {
		return new Complex(v, 0);
	}

	public static Complex valueOf(long v) {
		return new Complex(v, 0);
	}

	public static Complex valueOf(float v) {
		return new Complex(v, 0);
	}

	public static Complex valueOf(double v) {
		return new Complex(v, 0);
	}

	public static Complex valueOf(Complex v) {
		return new Complex(v.re, v.im);
	}

	/**
	 * Operator overload support: a+b
	 */
	public Complex add(Complex v) {
		return new Complex(this.re + v.re, this.im + v.im);
	}

	public Complex addRev(Complex v) {
		return new Complex(this.re + v.re, this.im + v.im);
	}

	public Complex add(int v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(int v) {
		return new Complex(v + this.re, this.im);
	}

	public Complex add(long v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(long v) {
		return new Complex(v + this.re, this.im);
	}

	public Complex add(float v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(float v) {
		return new Complex(v + this.re, this.im);
	}

	public Complex add(double v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(double v) {
		return new Complex(v + this.re, this.im);
	}

	/**
	 * Operator overload support: a-b
	 */
	public Complex subtract(Complex v) {
		return new Complex(this.re - v.re, this.im - v.im);
	}

	public Complex subtractRev(Complex v) {
		return new Complex(v.re - this.re, v.im - this.im);
	}

	public Complex subtract(int v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(int v) {
		return new Complex(v - this.re, -this.im);
	}

	public Complex subtract(long v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(long v) {
		return new Complex(v - this.re, -this.im);
	}

	public Complex subtract(float v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(float v) {
		return new Complex(v - this.re, -this.im);
	}

	public Complex subtract(double v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(double v) {
		return new Complex(v - this.re, -this.im);
	}

	/**
	 * Operator overload support: a*b
	 */
	public Complex multiply(Complex v) {
		return this.times(v);
	}

	public Complex multiplyRev(Complex v) {
		return v.times(this);
	}

	public Complex multiply(int v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(int v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiply(long v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(long v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiply(float v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(float v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiply(double v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(double v) {
		return new Complex(this.re * v, this.im * v);
	}

	/**
	 * Operator overload support: a/b
	 */
	public Complex divide(Complex v) {
		return this.divides(v);
	}

	public Complex divideRev(Complex v) {
		return v.divides(this);
	}

	public Complex divide(int v) {
		return this.divides(v);
	}

	public Complex divideRev(int v) {
		return this.reciprocal().times(v);
	}

	public Complex divide(long v) {
		return this.divides(v);
	}

	public Complex divideRev(long v) {
		return this.reciprocal().times(v);
	}

	public Complex divide(float v) {
		return this.divides(v);
	}

	public Complex divideRev(float v) {
		return this.reciprocal().times(v);
	}

	public Complex divide(double v) {
		return this.divides(v);
	}

	public Complex divideRev(double v) {
		return this.reciprocal().times(v);
	}

	/**
	 * Operator overload support: -a
	 */
	public Complex negate() {
		return new Complex(-this.re, -this.im);
	}

	// for test
	public static void main(String[] args) {
		Complex t = 1 ;
		System.out.println(t);
		Complex u = 2.1 - j + 6;
		Complex v = -1 + u;
		Complex w = 1 / u;
		System.out.println(u);
		System.out.println(v);
		System.out.println(w);
		System.out.println(parseComplex("0-i3.3166247903554"));
		System.out.println(u.equals(w));
		System.out.println(u.equals(v));
		System.out.println(u.equals(new Complex(u.re(), u.im())));
		System.out.println(u.equals(8.1-j));
	}

}
