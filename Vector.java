package util;

import java.util.HashMap;

import util.VectorException;

/** Implements a vector with *named* indices.  For example { x=1.0 y=2.0 } is a 2D
 *  vector with the first dimension named "x" and the second dimension named "y"
 *  and having respective values 1.0 and 2.0 in these dimensions.
 *  
 *  TODO: Implement all methods required to support the functionality of the project
 *        and that described in Vector.main(...) below.
 *
 */
public class Vector {

	private HashMap<String,Double> _hmVar2Value; // This maps dimension variable names to values
	
	/** Constructor of an initially empty Vector
	 * 
	 */
	public Vector() {
		// TODO: this method should not be empty! 
		// Hint: is there any memory you want to allocate?
		_hmVar2Value = new HashMap<String,Double>();
	}

	/** Constructor that parses a String s like "{ x=-1 y=-2.0 z=3d }" into 
	 *  the internal HashMap representation of the Vector.  See usage in main().
	 * 
	 * @param s
	 */
	public Vector(String s) throws VectorException {
		// TODO: this method should not be empty! 
		// Hint: you're going to have use String.split used in Project2.
		if (s == null || s.trim().equals("")) {
			// The java string trim() method eliminates leading and trailing spaces.
			throw new VectorException("Empty, cannot read");
		}
		try {
			_hmVar2Value = new HashMap<String,Double>();
			String[] space = s.split("[\\s=]");
			//if I pass in { x=1.0 y=2.0}, space = ['{','x','1.0','y','2.0','}']
			if (space[0].equals("{") == false || space[(space.length)-1].equals("}") == false) {
				throw new VectorException("Missing { or }");
			}
			int count = 0;		
			while (count < (space.length - 2)) {
				if (count % 2 == 0) {
					_hmVar2Value.put(space[count+1], Double.parseDouble(space[count+2]));
				}
				count += 2;
			}
		}
		catch (VectorException e) {
			throw new VectorException("error");
		}
	}

	/** Removes (clears) all (key,value) pairs from the Vector representation
	 * 
	 */
	public void clear() {
		// TODO: this method should not be empty! 
		// Hint: look very carefully at the available methods of HashMap... this is a one liner!
		_hmVar2Value.clear();
	}

	/** Sets a specific var to the value val in *this*, i.e., var=val
	 * 
	 * @param var - label of Vector index to change
	 * @param val - value to change it to
	 */
	public void set(String var, double val) {
		// TODO: this method should not be empty! 
		// Hint: look very carefully at the available methods of HashMap... this is a one liner!
		_hmVar2Value.put(var, val);
	}

	/** Sets all entries in *this* Vector to match entries in x
	 *  (if additional variables are in *this*, they remain unchanged) 
	 * 
	 * @param x
	 */
	public void setAll(Vector x) {
		// TODO: this method should not be empty! 
		// Hint: look very carefully at the available methods of HashMap... this is a one liner!
		_hmVar2Value.putAll(x._hmVar2Value);
	}

	///////////////////////////////////////////////////////////////////////////////
	// TODO: Add your methods here!  You'll need more than those above to make
	//       main() work below.
	///////////////////////////////////////////////////////////////////////////////
	public Vector sum(Vector x) throws VectorException {
		// Does not modify Vector x
		// Do we have to throw Exceptions? If this key does not match Vector x's key?
		Vector hmSum = new Vector();
		for (String key : _hmVar2Value.keySet()) {
			if (!x._hmVar2Value.containsKey(key)) {
				throw new VectorException("key does not match");
			}
		}
		if (x._hmVar2Value.size() != _hmVar2Value.size()) {
			throw new VectorException("Vectors' hashmaps' sizes should be equal");
		}
		for (String thiskey : x._hmVar2Value.keySet()) {
			hmSum._hmVar2Value.put(thiskey, (x._hmVar2Value.get(thiskey) + _hmVar2Value.get(thiskey)));
		}
		return hmSum;
	}
	
	public Vector scalarMult(double x) {
		// Does not modify Vector x
		Vector hmMult = new Vector();
		for (String thiskey : _hmVar2Value.keySet()) {
			hmMult._hmVar2Value.put(thiskey, (_hmVar2Value.get(thiskey))*x);
		}
		return hmMult;
	}

	public double computeL2Norm() {
	//	Vector hmCom = new Vector();
		double square = 0;
		for (String thiskey : _hmVar2Value.keySet()) {
			square += Math.pow(_hmVar2Value.get(thiskey), 2);
		}		
		return Math.sqrt(square);
	}

	public String toString() {
		// We could just repeatedly append to an existing String, but that copies the String each
		// time, whereas a StringBuilder simply appends new characters to the end of the String
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for (String thiskey : _hmVar2Value.keySet()) {
			// Append each vector value in order
			sb.append(thiskey + "=" + String.format("%6.4f ", _hmVar2Value.get(thiskey)));
		}
		sb.append("}");
		return sb.toString(); 
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector) {
			Vector v = (Vector)o; // This is called a cast (or downcast)... we can do it since we
			                      // know from the if statement that o is actually of subtype Vector
			                      // Makes o Vector type that has the content of Vector v
			if (_hmVar2Value.size() != v._hmVar2Value.size()) {
				 // Two Vectors cannot be equal if they don't have same number of key-value pairings
				return false;
			}
			for (String thiskey : _hmVar2Value.keySet()) {
				// use containsKey and containsValue to compare value
				if (v._hmVar2Value.containsKey(thiskey) == false || v._hmVar2Value.containsValue(_hmVar2Value.get(thiskey)) == false) {
					return false;
				}
			}
			return true; // Everything matched... objects are equal!
		}
		else { // if we get here "(o instanceof Vector)" was false
			return false; // Two objects cannot be equal if they don't have the same class type
		}
	}		
	
	// Created for Term.java
	// public double getkeyvalue(String s) throws VectorException { return _hmVar2Value.get(s); }
	public double getkeyvalue(String s) throws VectorException {
		if (!_hmVar2Value.containsKey(s)) {
			throw new VectorException("VectorException: does not contain key");
		}
		return _hmVar2Value.get(s);
	}
	
	public boolean containKey(String s) throws VectorException {
			return _hmVar2Value.containsKey(s); 
	}


	
	/** Your Vector class should implement the core functionality below and produce
	 *  **all** of the expected outputs below.  **These will be tested for grading.**
	 * 
	 *  When initially developing the code, comment out lines below that you have
	 *  not implemented yet.  This will allow your code to compile for incremental
	 *  testing.
	 *  
	 * @param args (unused -- ignore)
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Make vector: vec1[x y z] = [1 2 3]
		Vector vec1 = new Vector("{ x=1.0000 y=2.0000 z=3.0000 }");
		
		vec1.set("x", 1.0);
		vec1.set("y", 2.0);
		vec1.set("z", 3.0);
		
		// Make vector: vec2[x y z] = [-3 -2 -1]
		Vector vec2 = new Vector();
		vec2.set("x", -3.0);
		vec2.set("y", -2.0);
		vec2.set("z", -1.0);
		
		// Make vector: vec3[x y z] = vec4[x y z] = [-1 -2 -3]
		Vector vec3 = new Vector("{ x=-1 y=-2.0 z=3d }");
		Vector vec4 = new Vector(vec3.toString());
		
		// Hint: all numbers below are formatted with String.format("%s=%6.4f ", var, val)
		//       ... you may want to use this in your Vector.toString() implementation!
		
		// Test cases: 
//		System.out.println("1. Test for Vector(String s)");
		System.out.println("vec1: " + vec1); // Should print: { x=1.0000 y=2.0000 z=3.0000 }
		System.out.println("vec1 values: " + vec1._hmVar2Value.values());
		System.out.println(vec2); // Should print: { x=-3.0000 y=-2.0000 z=-1.0000 }
		System.out.println(vec3); // Should print: { x=-1.0000 y=-2.0000 z=3.0000 }
		System.out.println(vec4); // Should print: { x=-1.0000 y=-2.0000 z=3.0000 }
//		System.out.println("2. Test for sum(Vector x)");
		System.out.println(vec1.sum(vec1));        // Should print: { x=2.0000 y=4.0000 z=6.0000 }
		System.out.println(vec1.sum(vec2));        // Should print: { x=-2.0000 y=0.0000 z=2.0000 }
		System.out.println(vec1.sum(vec3));        // Should print: { x=0.0000 y=0.0000 z=6.0000 }
		System.out.println(vec1.scalarMult(0.5));  // Should print: { x=0.5000 y=1.0000 z=1.5000 }
		System.out.println(vec2.scalarMult(-1.0)); // Should print: { x=3.0000 y=2.0000 z=1.0000 }
		System.out.println(vec1.sum(vec2.scalarMult(-1.0))); // Should print: { x=4.0000 y=4.0000 z=4.0000 }
//		System.out.println("3. Test for computeL2Norm()");
		System.out.format("%01.3f\n", vec1.computeL2Norm());           // Should print: 3.742
		System.out.format("%01.3f\n", vec2.sum(vec3).computeL2Norm()); // Should print: 6.000
		
		// If the following don't work, did you override equals()?  See Project 2 Vector and Matrix.
		System.out.println(vec3.equals(vec1)); // Should print: false
		System.out.println(vec3.equals(vec3)); // Should print: true
		System.out.println(vec3.equals(vec4)); // Should print: true
	/*	System.out.println(vec3._hmVar2Value.values() + " compared to " + vec4._hmVar2Value.values());
		System.out.println(vec3._hmVar2Value.size() + " compared to " + vec4._hmVar2Value.size()); */
		System.out.println(vec1.sum(vec2).equals(vec2.sum(vec1))); // Should print: true
	}	
}
