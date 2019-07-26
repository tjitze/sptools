package com.tr.sptools.semiring;

/**
 * https://en.wikipedia.org/wiki/Semiring
 * 
 * @param <V> Value type used by semi ring (e.g. Double, Integer)
 */
public abstract class SemiRing<V> {

	public abstract V sum(V v1, V v2);
	
	public abstract V product(V v1, V v2);

	public abstract V zero();
	
	public abstract V one();
		
}
