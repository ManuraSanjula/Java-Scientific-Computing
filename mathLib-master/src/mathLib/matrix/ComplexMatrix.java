package mathLib.matrix;

import static mathLib.numbers.Complex.ZERO;
import static mathLib.numbers.Complex.j;

import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;

public class ComplexMatrix {

	int M;             // number of rows
    int N;             // number of columns
    Complex[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public ComplexMatrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new Complex[M][N];
        for(int i=0; i<M; i++){
        	for(int j=0; j<N; j++){
        		data[i][j] = new Complex(0,0) ;
        	}
        }
    }

    // create matrix based on 2d array --> if data is complex
    public ComplexMatrix(Complex[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new Complex[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // create matrix based on 2d array --> if data is real
    public ComplexMatrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new Complex[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = new Complex(data[i][j],0) ;
    }

    // create matrix based on 1d array --> if data is complex
    public ComplexMatrix(Complex[] data) {
        M = 1; // default is row matrix
        N = data.length;
        this.data = new Complex[M][N];
            for (int j = 0; j < N; j++)
                    this.data[0][j] = data[j];
    }

    public ComplexMatrix(double[] data) {
        M = 1; // default is row matrix
        N = data.length;
        this.data = new Complex[M][N];
            for (int j = 0; j < N; j++)
                    this.data[0][j] = new Complex(data[j], 0.0);
    }

    public int[] getSize() {
    	return new int[] {M, N} ;
    }

    public int getRowDim() {
    	return M ;
    }

    public int getColumnDim() {
    	return N ;
    }

    public Complex[][] getData() {
    	return this.data ;
    }

    public static ComplexMatrix random(int M, int N) {
        ComplexMatrix A = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = new Complex(Math.random(), Math.random()) ;
        return A;
    }

    // create and return the N-by-N identity matrix
    public static ComplexMatrix identity(int N) {
        ComplexMatrix I = new ComplexMatrix(N, N);
        for (int i = 0; i < N; i++){
            I.data[i][i] = new Complex(1,0);
        }
        return I;
    }

    // create and return the transpose of the invoking matrix
    public ComplexMatrix transpose() {
        ComplexMatrix A = new ComplexMatrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public ComplexMatrix plus(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].plus(B.data[i][j]) ;
        return C;
    }

    public ComplexMatrix plus(Matrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].plus(B.data[i][j]) ;
        return C;
    }

    // return C = A - B
    public ComplexMatrix minus(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].minus(B.data[i][j]) ;
        return C;
    }

    public ComplexMatrix minus(Matrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].minus(B.data[i][j]) ;
        return C;
    }

    // does A = B exactly?
    public boolean equals(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (!A.data[i][j].equals(B.data[i][j])) return false;
        return true;
    }

    public boolean equals(ComplexMatrix B, double tol) {
    	ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (ComplexMath.abs(A.data[i][j]-B.data[i][j]) > tol) return false;
        return true;
    }

    // return C = A * B
    public ComplexMatrix times(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] = C.data[i][j].plus(A.data[i][k].times(B.data[k][j]));
        return C;
    }

    public ComplexMatrix times(Matrix B) {
        ComplexMatrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] = C.data[i][j].plus(A.data[i][k].times(B.data[k][j]));
        return C;
    }

    // returns multiplication by a complex scalar
    public ComplexMatrix times(Complex a) {
        ComplexMatrix A = this;
        ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].times(a) ;
        	}
        }
        return C;
    }

    // returns multiplication by a double scalar
    public ComplexMatrix times(double a) {
        ComplexMatrix A = this;
        ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].times(a) ;
        	}
        }
        return C;
    }

//     returns conjugate of a matrix
    public ComplexMatrix conjugate() {
        ComplexMatrix A = this;
        ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].conjugate() ;
        	}
        }
        return C;
    }

    public Matrix re() {
        ComplexMatrix A = this;
        Matrix C = new Matrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].re() ;
        	}
        }
        return C;
    }

    public Matrix im() {
        ComplexMatrix A = this;
        Matrix C = new Matrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].im() ;
        	}
        }
        return C;
    }

    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++)
                System.out.printf("" + data[i][j] +  "           ");
            System.out.println();
        }
        System.out.println();
    }

    @Override
	public String toString() {
    	StringBuilder st = new StringBuilder() ;
		return st ;
	}

	// get the i,j element of the matrix (i=0,1,...  j=0,1,...)
    public Complex getElement(int i, int j){
    	return this.data[i][j] ;
    }

    public ComplexMatrix plus(double a) {
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].plus(a) ;
        return C;
    }

    public ComplexMatrix plus(Complex a) {
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].plus(a) ;
        return C;
    }

    public ComplexMatrix minus(double a) {
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].minus(a) ;
        return C;
    }

    public ComplexMatrix minus(Complex a) {
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].minus(a) ;
        return C;
    }

    // create and return the M-by-N constant matrix
    public static ComplexMatrix constant(int M, int N, double c) {
    	ComplexMatrix C = new ComplexMatrix(M, N);
        for(int i=0; i<M; i++){
        	for(int j=0; j<N; j++){
        		C.data[i][j] = new Complex(c, 0.0) ;
        	}
        }
        return C;
    }

    public static ComplexMatrix constant(int M, int N, Complex c) {
    	ComplexMatrix C = new ComplexMatrix(M, N);
        for(int i=0; i<M; i++){
        	for(int j=0; j<N; j++){
        		C.data[i][j] = c ;
        	}
        }
        return C;
    }

    public ComplexMatrix timesElement(ComplexMatrix B){
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].times(B.data[i][j]) ;
        	}
        }
        return C;
    }

    public ComplexMatrix timesElement(Matrix B){
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].times(B.data[i][j]) ;
        	}
        }
        return C;
    }

    public ComplexMatrix divideElement(ComplexMatrix B){
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].divides(B.data[i][j]) ;
        	}
        }
        return C;
    }

    public ComplexMatrix divideElement(Matrix B){
    	ComplexMatrix A = this;
    	ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].divides(B.data[i][j]) ;
        	}
        }
        return C;
    }

    public Complex trace() {
    	if (M != N)
    		throw new IllegalArgumentException("must be a square matrix!") ;
    	Complex sum = ZERO ;
    	for(int i=0; i<M; i++)
    		sum = sum + data[i][i] ;
    	return sum ;
    }

    /**
     * sub-Blocks of the matrix
     */

    public ComplexMatrix getRow(int row) {
    	int rowSize = N ;
    	Complex[][] selectedRow = new Complex[1][rowSize] ;
    	for(int i=0; i<rowSize; i++) {
    		selectedRow[0][i] = data[row][i] ;
    	}
    	return new ComplexMatrix(selectedRow) ;
    }

    public ComplexMatrix getColumn(int column) {
    	int columnSize = M ;
    	Complex[][] selectedColumn = new Complex[columnSize][1] ;
    	for(int i=0; i<columnSize; i++) {
    		selectedColumn[i][0] = data[i][column] ;
    	}
    	return new ComplexMatrix(selectedColumn) ;
    }

    public static PowerIterationMatrix toPowerIterationMatrix(ComplexMatrix A){
    	return new PowerIterationMatrix(A.data) ;
    }

    // ************ operator overloading **********************

    public static ComplexMatrix valueOf(double[][] v) {
    	return new ComplexMatrix(v) ;
    }

    public static ComplexMatrix valueOf(int[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	double[][] data = new double[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new ComplexMatrix(data) ;
    }

    public static ComplexMatrix valueOf(float[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	double[][] data = new double[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new ComplexMatrix(data) ;
    }

    public static ComplexMatrix valueOf(long[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	double[][] data = new double[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new ComplexMatrix(data) ;
    }

    public static ComplexMatrix valueOf(Complex[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	Complex[][] data = new Complex[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new ComplexMatrix(data) ;
    }

    public static ComplexMatrix valueOf(Matrix v) {
    	int M = v.getRowDim() ;
    	int N = v.getColumnDim() ;
    	Complex[][] data = new Complex[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = new Complex(v.getElement(i, j), 0.0) ;
    	return new ComplexMatrix(data) ;
    }

    public static ComplexMatrix valueOf(ComplexMatrix v) {
    	int M = v.getRowDim() ;
    	int N = v.getColumnDim() ;
    	Complex[][] data = new Complex[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v.getElement(i, j) ;
    	return new ComplexMatrix(data) ;
    }

 	/**
 	 * Operator overload support: a+b
 	 */
 	public ComplexMatrix add(ComplexMatrix v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix addRev(ComplexMatrix v) {
 		return v.plus(this) ;
 	}

 	public ComplexMatrix add(int v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix addRev(int v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix add(long v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix addRev(long v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix add(float v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix addRev(float v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix add(double v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix addRev(double v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix add(Complex v) {
 		return this.plus(v) ;
 	}

 	public ComplexMatrix addRev(Complex v) {
 		return this.plus(v) ;
 	}

 	/**
 	 * Operator overload support: a-b
 	 */
 	public ComplexMatrix subtract(ComplexMatrix v) {
 		return this.minus(v) ;
 	}

 	public ComplexMatrix subtractRev(ComplexMatrix v) {
 		return this.times(-1).plus(v) ;
 	}

 	public ComplexMatrix subtract(int v) {
 		return this.minus(v) ;
 	}

 	public ComplexMatrix subtractRev(int v) {
 		return this.times(-1).plus(v) ;
 	}

 	public ComplexMatrix subtract(long v) {
 		return this.minus(v) ;
 	}

 	public ComplexMatrix subtractRev(long v) {
 		return this.times(-1).plus(v) ;
 	}

 	public ComplexMatrix subtract(float v) {
 		return this.minus(v) ;
 	}

 	public ComplexMatrix subtractRev(float v) {
 		return this.times(-1).plus(v) ;
 	}

 	public ComplexMatrix subtract(double v) {
 		return this.minus(v) ;
 	}

 	public ComplexMatrix subtractRev(double v) {
 		return this.times(-1).plus(v) ;
 	}

 	public ComplexMatrix subtract(Complex v) {
 		return this.minus(v) ;
 	}

 	public ComplexMatrix subtractRev(Complex v) {
 		return this.times(-1).plus(v) ;
 	}

 	/**
 	 * Operator overload support: a*b
 	 */
 	public ComplexMatrix multiply(ComplexMatrix v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(ComplexMatrix v) {
 		return v.times(this);
 	}

 	public ComplexMatrix multiply(Matrix v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(Matrix v) {
 		ComplexMatrix temp = new ComplexMatrix(v.getData()) ;
 		return temp.times(this) ;
 	}

 	public ComplexMatrix multiply(int v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(int v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiply(long v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(long v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiply(float v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(float v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiply(double v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(double v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiply(Complex v) {
 		return this.times(v);
 	}

 	public ComplexMatrix multiplyRev(Complex v) {
 		return this.times(v);
 	}

 	/**
 	 * Operator overload support: a/b
 	 */
 	public ComplexMatrix divide(ComplexMatrix v) {
 		return this.divideElement(v) ;
 	}

 	public ComplexMatrix divideRev(ComplexMatrix v) {
 		return v.divideElement(this) ;
 	}

 	public ComplexMatrix divide(int v) {
 		return this.times(1/v) ;
 	}

 	public ComplexMatrix divideRev(int v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	public ComplexMatrix divide(long v) {
 		return this.times(1/v) ;
 	}

 	public ComplexMatrix divideRev(long v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	public ComplexMatrix divide(float v) {
 		return this.times(1/v) ;
 	}

 	public ComplexMatrix divideRev(float v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	public ComplexMatrix divide(double v) {
 		return this.times(1/v) ;
 	}

 	public ComplexMatrix divideRev(double v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	/**
 	 * Operator overload support: -a
 	 */
 	public ComplexMatrix negate() {
 		return this.times(-1) ;
 	}

 	// for test ************************************
 	public static void main(String[] args) {
 		Complex[][] d = new Complex[][] {{1, 1}, {-5+j, -j+1}} ;
 		ComplexMatrix A = new ComplexMatrix(d) ;
		A.show();

		System.out.println(A.trace());

//		// operator overloading
//		ComplexMatrix B = d ;
//		B.show();
//
//		ComplexMatrix C = new int[][]{{1, 2, 3, 4}, {3, 5, 7, -2}, {3, 5, 7, -2}, {3, 5, 7, -2}} ;
//		C.show();
//
//		ComplexMatrix D = C + 2*identity(C.getRowDim()) ;
//		D.show();

 		double[][] f = {{1, 1}, {-5, 1}} ;
 		Matrix E = new Matrix(f) ;
		E.show();

//		(E*A).show();
//		(A*E).show() ;

//		E.getColumn(0).show();
//		E.getColumn(1).show();
//		E.getRow(0).show();
//		E.getRow(1).show();

		(A*E.getRow(0).transpose()).show();

	}

}
