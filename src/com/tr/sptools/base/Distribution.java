package com.tr.sptools.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import com.tr.sptools.semiring.SemiRing;

public class Distribution<V> implements AbstractDistribution<V> {

	private final SemiRing<V> semiRing;

	private final Set<String> vars;
	
	private final HashMap<Configuration, V> map = new LinkedHashMap<Configuration, V>();
	
	public Distribution(SemiRing<V> semiRing, Set<String> vars) {
		this.semiRing = semiRing;
		this.vars = vars;
	}

	public void set(Configuration c, V value) {
		if (!c.getVariables().equals(vars)) {
			throw new IllegalArgumentException("Wrong set of variables");
		}
		map.put(c, value);
	}
	
	@Override
	public V getWeight(Configuration c) {
		if (!c.getVariables().equals(vars)) {
			throw new IllegalArgumentException("Wrong set of variables");
		}
		if (map.containsKey(c)) {
			return map.get(c);
		}
		V sum = semiRing.zero();
		for (Configuration cc: map.keySet()) {
			if (cc.isConsistent(c)) {
				sum = semiRing.sum(sum, map.get(cc));
			}
		}
		return sum;
	}
	
	public boolean isNormalized() {
		V sum = semiRing.zero();
		for (Configuration cc: map.keySet()) {
			sum = semiRing.sum(sum, map.get(cc));
		}
		return (sum.equals(semiRing.one()));
	}

	public static <V> Distribution<V> singleton(SemiRing<V> ops, String var, V tv, V fv) {
		Distribution<V> dist = new Distribution<V>(ops, Collections.singleton(var));
		dist.set(Configuration.singleton(var, true), tv);
		dist.set(Configuration.singleton(var, false), fv);
		return dist;
	}

	@Override
	public Set<String> getVariables() {
		return vars;
	}
	
	public V getNormalizationFactor() {
		V s = semiRing.zero();
		for (Configuration cfg: map.keySet()) {
			s = semiRing.sum(s, map.get(cfg));
		}
		return s;
	}
	
	public String toString() {
		String s = "";
		for (Configuration cfg: map.keySet()) {
			s += cfg + ": " + map.get(cfg) + "\n";
		}
		return s;
	}

	public Set<Configuration> getConfigurations() {
		return map.keySet();
	}

	@Override
	public SemiRing<V> getSemiRing() {
		return semiRing;
	}
}
