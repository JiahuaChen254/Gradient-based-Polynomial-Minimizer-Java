package poly;

import java.util.ArrayList;
import java.util.TreeSet;

import util.Vector;
import util.VectorException;

/** Implements an individual term in a polynomial.  If 5x^2 + 3xy is a polynomial,
 *  it has two terms 5x^2 and 2xy, each of which would be represented by a different
 *  instance of this class.
 *
 */
public class Term {
	
	// For term 2.1*x^4*y*z^2, the data members would take values as follows:
	private double _coef; // = 2.1
	private ArrayList<String>  _vars; // = ["x", "y", "z"]
	private ArrayList<Integer> _pows; // = [4, 1, 2]

	/** This constructor has been implemented for you.
	 * 
	 * @param coef -- sets the _coef member
	 */
	public Term(double coef) {
		_coef = coef;
		_vars = new ArrayList<String>();
		_pows = new ArrayList<Integer>();
	}
	
	/** This constructor has been implemented for you -- it parses a term 
	 *  representation from a String into the format required by this class.
	 *  You need to understand the following code.
	 * 
	 * @param s -- String to parse
	 * @throws PolyException if s is malformed
	 */
	public Term(String s) throws PolyException {
		
		if (s == null || s.trim().equals("")) // Trimming takes away leading and following spaces
			throw new PolyException("Empty Term, cannot read");
		
		// Initialize this term
		_coef = 1.0d; // Will multiply any constants by this
		_vars = new ArrayList<String>();
		_pows = new ArrayList<Integer>();

		// You need to understand all lines of the following code
		String[] factors = s.split("\\*"); 
		// For s=" 2.1*x^4*y*z^2 ", spit by *: factors=[" 2.1","x^4","y","z^2 "]
		for (String factor : factors) { // Iterate through factors split string array and pointer is the variables
			factor = factor.trim(); // Get rid of leading and trailing whitespace
			// factor=["2.1","x^4","y","z^2"]
			try {
				// If successful, multiplies in a constant (multiple constants in a product allowed)
				_coef *= Double.parseDouble(factor); 					
			} catch (NumberFormatException e) {
				// If not a coefficient, must be a factor "<var>^<pow>"
				// Must be a variable to a power -- parse the factor and add to list
				int pow = 1; // If no power, defaults to 1
				String[] var_pow = factor.split("\\^");
				String var = var_pow[0];
				if (var_pow.length == 2) {
					try { // Second part must be exponent
						pow = Integer.parseInt(var_pow[1]);
					} catch (NumberFormatException f) {
						throw new PolyException("ERROR: could not parse " + factor);
					}
				} else if (var_pow.length > 2) 
					throw new PolyException("ERROR: could not parse " + factor);
				
				// Successfully parsed variable and power, add to list
				if (_vars.contains(var))
					throw new PolyException("ERROR: " + var + " appears twice in " + s);
				_vars.add(var);
				_pows.add(pow);
			}
		}
	}
	
	/** Produce a re-parseable representation of this Term as a String.  This
	 *  has been done for you.
	 * 
	 */
	public String toString() {
		// Using "+" to append Strings involves a lot of String copies since Strings are 
		// immutable.  StringBuilder is much more efficient for append.
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%01.3f", _coef));
		for (int i = 0; i < _vars.size(); i++) {
			String var = _vars.get(i);
			int pow = _pows.get(i);
			sb.append("*" + var + (pow == 1 ? "" : "^" + pow)); //???
		}
		return sb.toString();
	}

	/** Returns all of the variables used in this Term as a sorted set (TreeSet).
	 *  This has been implemented for you, but you need to understand how it works
	 *  since you'll write a similar method in Polynomial that uses this method.
	 * 
	 * @return
	 */
	public TreeSet<String> getAllVars() {
		// TreeSets are like HashSets but sorted alphabetically (lookup and insertion are
		// a little less efficient than HashSets, but this won't matter for our sizes).
		return new TreeSet<String>(_vars);
	}
		
	///////////////////////////////////////////////////////////////////////////////
	// TODO: Your methods here!  You should add some helper methods that facilitate
	//       the implementation of the methods below.
	///////////////////////////////////////////////////////////////////////////////

	/** If Term defines a function f(x,y) = 2xy^2 and assignments is { x=2.0 y=3.0 } 
	 *  then this method returns 36.0, which is the evaluation of f(2.0,3.0). 
	 * 
	 * @param assignments
	 * @return
	 * @throws PolyException
	 */
	public double evaluate(Vector assignments) throws PolyException, VectorException {

		// TODO: Should not return 0!
		for (String key : _vars) {
			if (!assignments.containKey(key)) {
				throw new PolyException("PolyException: missing key");
			}
		}
		try {
		double result = _coef;
		for (int i = 0; i < _vars.size(); i++) {
			result *= Math.pow(assignments.getkeyvalue(_vars.get(i)), _pows.get(i));
		}
		return result;
		}
		catch (VectorException e) {
			throw new VectorException("error");
		}
	}		

	/** If Term defines a function f(.) then this method returns the **symbolic**
	 *  partial derivative (which you can verify from calculus is still a Term):
	 *  
	 *    partial f(1.0,2.0) / partial var.
	 * 
	 *  Specifically, if Term defines a function f(x,y) = 2xy^2 and var = "x"
	 *  then this method returns a **new** Term 2y^2 and if var = "y" then it
	 *  instead returns a **new** Term 4xy.
	 * 
	 * @param var
	 * @return partial derivative of this w.r.t. var as a new Term
	 */
	public Term differentiate(String var) {

		// TODO: Should not return null!
		Term t = new Term(_coef); 
		t._pows.addAll(_pows);
		t._vars.addAll(_vars);
		int index = t._vars.indexOf(var);
		if (t._vars.contains(var)) {
			t._coef = t._coef * t._pows.get(index);
			if (t._pows.get(index) == 1) {
				t._pows.remove(index);
				t._vars.remove(index);
				return t;
			}
			else {
				t._pows.set(index, t._pows.get(index) - 1);
				return t;
			}
		}
		else { // if does not contain var
			t._coef = 0; 
			t._pows.clear();
			t._vars.clear();
			return t; 
		}
	}
	
	// Added for Polynomial
	public double getCoef(){
		return _coef;
	}
	
	/*
	// My test code
	public static void main(String[] args) throws Exception {
		Term t = new Term("2*x*y^2");
		System.out.println(t);
		Vector v = new Vector ("{ x=2 y=3 z=2 }");
		System.out.println(v);
		
		// Test Vector's getkeyvalue(String s)
		System.out.println(v.getkeyvalue("x")); // Should print: 2
		
		// Test differentiate(String var)
		Term tt = t.differentiate("y");
		System.out.println(tt); // Should print: 4*x*y
		
		// Test evaluate(Vector assignments)
		double result = t.evaluate(v); 
		System.out.println(result); // Should print: 36
		double result1 = tt.evaluate(v);
		System.out.println(result1); // Should print: 24		
	}
	*/
}
