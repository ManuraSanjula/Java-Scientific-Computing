package mathLib.fem.shapefun;

import static mathLib.func.symbolic.FMath.*;

import java.util.HashMap;
import java.util.Map;

import mathLib.fem.core.Element;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.Utils;
import mathLib.fem.util.container.ObjList;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.VariableArray;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;

/**
 * Linear shape function in triangle local coordinate system (area coordinate system)
 * 
 * Ni = Ni(r,s,t) = Ni( r(x,y), s(x,y), t(x,y) ), i=1,2,3
 * 
 * where
 *     N1 = r
 *     N2 = s
 *     N3 = t
 * and 
 *   r + s + t = 1
 *
 */
public class SFLinearLocal2D  extends MultiVarFunc 
							  implements ScalarShapeFunction {
	protected int funIndex;
	private MathFunc funOuter = null;
	private MathFunc funCompose = null;
	protected ObjList<String> innerVarNames = null;
	
	protected Element e = null;
	private double area = -1.0;
	private double[] a = new double[3];
	private double[] b = new double[3];
	private double[] c = new double[3];
	
	private double coef = 1.0;
	
	//r, s, t
	class SF123 extends MultiVarFunc {
		public SF123() {
			super("SF"+funIndex, SFLinearLocal2D.this.varNames);
			this.setArgIdx(Utils.getIndexMap(this.getVarNames()));
		}
		@Override
		public MathFunc diff(String var) {
			if(varNames[funIndex].equals(var)) { 
				//d(N1)/dr = 1.0;  d(N2)/ds = 1.0;  d(N3)/dt = 1.0
				return C1;
			} else if(funIndex == 2){ 
				//N3 = t = 1 - r - s, t is not free variable
				//d(N3)/dr = -1.0;  d(N3)/ds = -1.0
				return Cm1;
			} else {
				return C0;
			}
		}
		@Override
		public double apply(Variable v) {
			return v.get(varNames[funIndex]);
		}
		
		public String getExpr() {
			return varNames[funIndex];
		}
		
		public String toString() {
			return varNames[funIndex];
		}
		
		@Override
		public double apply(double... args) {
			return args[argIdx[funIndex]];
		}
 	}
	
	/**
	 * 
	 * N1 = L1 = r
	 * N2 = L2 = s
	 * N3 = L3 = t
	 * @param funID = 1,2,3
	 * 
	 */
	public void Create(int funID, double coef) {
		funIndex = funID - 1;
		if( funID<1 || 3<funID ) {
			throw new FutureyeException("ERROR: funID should be 1,2 or 3.");
		}
		
		this.varNames = new String[]{"r","s","t"};
		innerVarNames = new ObjList<String>("x","y");
		
		//Compose function: r = r(x,y), s = s(x,y), t = t(x,y)
		Map<String, MathFunc> fInners = new HashMap<String, MathFunc>();
		final String varName = varNames[funIndex];
		fInners.put(varName, new MultiVarFunc(varName, innerVarNames.toList()) {	
			public MathFunc diff(String var) {
				if(area < 0.0) {
					throw new FutureyeException("Check nodes order: area < 0.0");
				} else {
					if(varName.equals("r")) {//r
						if(var.equals("x"))
							return new FC(b[0] / (2 * area));
						if(var.equals("y"))
							return new FC(c[0] / (2 * area));
					} else if(varName.equals("s")) {//s
						if(var.equals("x"))
							return new FC(b[1] / (2 * area));
						if(var.equals("y"))
							return new FC(c[1] / (2 * area));
					} else if(varName.equals("t")) {//t
						if(var.equals("x"))
							return new FC(b[2] / (2 * area));
						if(var.equals("y"))
							return new FC(c[2] / (2 * area));
					}
				}
				return null;
			}
			
			@Override
			public double apply(double... args) {
				throw new UnsupportedOperationException();
			}
		});
		
		//ä½¿ç”¨å¤�å�ˆå‡½æ•°æž„é€ å½¢å‡½æ•°
		funOuter = new SF123();
		this.coef = coef;
		funCompose = FC.c(this.coef).M(
				funOuter.compose(fInners)
				);
		funCompose.setActiveVarByNames(funOuter.getVarNames());
	}
	
	public SFLinearLocal2D(int funID) {
		this.Create(funID, 1.0);
	}
	
	public SFLinearLocal2D(int funID, double coef) {
		this.Create(funID, coef);
	}
	
	@Override
	public MathFunc diff(String varName) {
		return funCompose.diff(varName);
	}

	@Override
	public double apply(Variable v) {
		return funCompose.apply(v);
	}
	
	@Override
	public double[] applyAll(VariableArray v, Map<Object,Object> cache) {
		return funCompose.applyAll(v,cache);
	}

	@Override
	public void assignElement(Element e) {
		this.e = e;
		//ç”±nodeæ”¹ä¸ºvertexï¼Œå› ä¸ºElement.adjustVerticeToCounterClockwise()ç»“ç‚¹é¡ºåº�å�ªè°ƒæ•´äº†vertex
		VertexList vList = e.vertices();
		double x1 = vList.at(1).coord(1) , y1 =  vList.at(1).coord(2) ;
		double x2 = vList.at(2).coord(1) , y2 =  vList.at(2).coord(2) ;
		double x3 = vList.at(3).coord(1) , y3 =  vList.at(3).coord(2) ;
		
		area = ( (x2*y3 - x3*y2) - (x1*y3 - x3*y1) + (x1*y2 - x2*y1) ) / 2.0;
		a[0] = x2*y3 - x3*y2;
		b[0] = y2 - y3;
		c[0] = x3 - x2;
		a[1] = x3*y1 - x1*y3;
		b[1] = y3 - y1;
		c[1] = x1 - x3;
		a[2] = x1*y2 - x2*y1;
		b[2] = y1 - y2;
		c[2] = x2 - x1;
	}

	public String getExpr() {
		return "N"+(funIndex+1)+"(r,s,t)";
	}
	
	public String toString() {
		return "N"+(funIndex+1)+"(r,s,t) = "+funOuter.getExpr();
	}
	
	ScalarShapeFunction sf1d1 = new SFLinearLocal1D(1);
	ScalarShapeFunction sf1d2 = new SFLinearLocal1D(2);
	@Override
	public ScalarShapeFunction restrictTo(int funIndex) {
		if(funIndex == 1) return sf1d1;
		else return sf1d2;
	}

	@Override
	public ObjList<String> innerVarNames() {
		return innerVarNames;
	}
	
	@Override
	public double apply(double... args) {
		return this.funCompose.apply(args);
	}
	
}
