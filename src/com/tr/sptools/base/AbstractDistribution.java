package com.tr.sptools.base;

import java.util.Collection;
import java.util.Set;

import com.tr.sptools.semiring.SemiRing;

/**
 * Interface for objects that represent some distribution, meaning
 * that it defines a set of variables and maps each configuration of
 * (subsets) of these variables to a weight (e.g. probability or rank).
 *
 * @param <V>
 */
public abstract interface AbstractDistribution<V> {

	public SemiRing<V> getSemiRing();

	/**
	 * @return The variables over which this thing is a distribution.
	 */
	public Collection<String> getVariables();
	
	/**
	 * @return Weight (e.g. probability or rank) of given total configuration.
	 */
	public V getWeight(Configuration cfg);

	/**
	 * @return Sum weight of set of configurations
	 */
	public default V getWeight(Collection<Configuration> cfgs) {
		V w = getSemiRing().zero();
		for (Configuration cfg: cfgs) {
			w = getSemiRing().sum(w, getWeight(cfg));
		}
		return w;
	}
		
}
