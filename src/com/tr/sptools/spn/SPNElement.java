package com.tr.sptools.spn;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.Transformer;

import com.tr.sptools.base.AbstractDistribution;
import com.tr.sptools.base.Configuration;
import com.tr.sptools.semiring.SemiRing;

/**
 * Abstract class for an element of an SPN
 *
 * @param <V> The value type used by the SPN (e.g. double for probability or integer for ranks)
 */
public abstract class SPNElement<V> implements AbstractDistribution<V> {

	/** The semiring in use for calculations */
	private final SemiRing<V> semiRing;
	
	/**
	 * Construct the element using the given semiring
	 */
	public SPNElement(SemiRing<V> semiRing) {
		this.semiRing = semiRing;
	}
	
	/**
	 * Return weight (e.g. probabiltiy or rank) of given configuration.
	 */
	public abstract V getWeight(Configuration config);
	
	/** 
	 * @return True iff all children of sum node cover the same set of variables
	 */
	public abstract boolean isComplete();

	/** 
	 * @return True iff no variable appear negated and non-negated in different children of the same product node
	 */
	public abstract boolean isConsistent();
	
	/**
	 * @return List of variables below this node, with duplicates if a variable appears more than once
	 */
	public abstract List<String> getVariablesCovered();
		
	/**
	 * @return List of indicators below this node, with duplicates if an indicator appears more than once
	 */
	public abstract List<SPNIndicator<V>> getIndicatorsCovered();

	/**
	 * @return Semiring used by this element
	 */
	public final SemiRing<V> getSemiRing() {
		return semiRing;
	}
	
	public abstract <V2> SPNElement<V2> convert(SPNTransformer<V, V2> trans);

	@Override
	public Collection<String> getVariables() {
		Set<String> vars = new LinkedHashSet<String>();
		vars.addAll(getVariablesCovered());
		return vars;
	}
	
	public abstract String toString();

		
}
