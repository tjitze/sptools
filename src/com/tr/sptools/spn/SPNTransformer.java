package com.tr.sptools.spn;

import com.tr.sptools.semiring.SemiRing;

/**
 * Abstract class for SPN transformer.
 * 
 * Recursively transforms an SPN using the transformProduct and 
 * transformSum methods.
 * 
 * Use it by calling the convert method of the input SPN's root node,
 * passing this transformer as argument.
 *
 * @param <V> Type of weights of output SPN
 * @param <V2> Type of weights of input SPN
 */
public abstract class SPNTransformer<V, V2> {

	private SemiRing<V2> sr;
	
	/**
	 * Construct transformer
	 * 
	 * @param sr SemiRing used for output SPN
	 */
	public SPNTransformer(SemiRing<V2> sr) {
		this.sr = sr;
	}

	/**
	 * Transforms indicator nodes. This default implementation
	 * performs no changes except for resetting the semiring.
	 */
	public SPNIndicator<V2> transformIndicator(SPNIndicator<V> i) {
		return new SPNIndicator<V2>(sr, i.getVariable(), i.getValue());
	}
	
	/**
	 * Transform a product node
	 */
	public abstract SPNProductNode<V2> transformProduct(SPNProductNode<V> product);

	/**
	 * Transform a sum node
	 */
	public abstract SPNSumNode<V2> transformSum(SPNSumNode<V> sum);

}
