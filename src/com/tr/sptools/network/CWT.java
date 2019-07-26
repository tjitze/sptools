package com.tr.sptools.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tr.sptools.base.Configuration;
import com.tr.sptools.base.Distribution;
import com.tr.sptools.semiring.SemiRing;

/**
 * A conditional weight table (CWT) stores weights of values for a variable 
 * given all configurations of its parent variables.
 * 
 * A CWT can represent a conditional probability table (CWT) of a 
 * Bayesian network but also a conditional ranking table (CRT) of 
 * a ranking network.
 *
 * @param <V> Type of value to use for weights (e.g. Double for probabilities)
 */
public class CWT<V> {
	
	public final SemiRing<V> semiRing;
	public final String var;
	public final Set<String> parents;
	public final Map<Configuration, Distribution<V>> table;
	
	public CWT(SemiRing<V> semiRing, String var, Set<String> parents) {
		this.semiRing = semiRing;
		this.var = var;
		this.parents = parents;
		this.table = new HashMap<Configuration, Distribution<V>>();
	}
	
	public CWT(SemiRing<V> semiRing, String var, String ... parents) {
		this.semiRing = semiRing;
		this.var = var;
		this.parents = new LinkedHashSet<String>();
		for (String p: parents) this.parents.add(p);
		this.table = new HashMap<Configuration, Distribution<V>>();
	}
	
	/**
	 * @return The variable of this CWT
	 */
	public String getVariable() {
		return var;
	}
	
	/**
	 * @return Parent variables of this CWT
	 */
	public List<String> getParents() {
		List<String> vars = new ArrayList<String>();
		vars.addAll(parents);
		Collections.sort(vars);
		return vars;
	}

	/**
	 * Set weights for the values of this CWT's variable, for the given parent configuration.
	 */
	public void setVarEntry(Configuration parentConfig, V trueWeight, V falseWeight) {
		setVarEntry(parentConfig, Distribution.singleton(semiRing, var, trueWeight, falseWeight));
	}

	/**
	 * Set weight distribution of this CWT's variable
	 * for the given parent configuration.
	 */
	public void setVarEntry(Configuration parentConfig, Distribution<V> varDist) {
		if (!parentConfig.getVariables().equals(parents)) {
			throw new IllegalArgumentException("Incorrect parent variables");
		}
		table.put(parentConfig, varDist);
	}

	/**
	 * Return weight of the given value, given a parent configuraiton
	 */
	public V getWeight(boolean value, Configuration parentConfig) {
		if (!isComplete()) throw new IllegalStateException("CWT incomplete");
		if (!parentConfig.getVariables().equals(parents)) {
			throw new IllegalArgumentException("Incorrect parent variables");
		}
		if (!table.containsKey(parentConfig)) {
			throw new IllegalStateException("Table incomplete for variable " + var + " and parent config " + parentConfig);
		}
		return table.get(parentConfig).getWeight(Configuration.singleton(var, value));
	}

	public boolean isComplete() {
		return (table.size() == (int)Math.pow(2, parents.size()));
	}

}
