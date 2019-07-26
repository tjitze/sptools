package com.tr.sptools.spn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.tr.sptools.base.Configuration;
import com.tr.sptools.semiring.SemiRing;

public class SPNSumNode<V> extends SPNElement<V> {

	private Map<SPNElement<V>, V> subs = new LinkedHashMap<SPNElement<V>, V>();

	public SPNSumNode(SemiRing<V> ops) {
		super(ops);
	}

	public void add(SPNElement<V> e, V weight) {
		subs.put(e, weight);
	}

	public V getWeight(SPNElement<V> e) {
		return subs.get(e);
	}
	
	@Override
	public V getWeight(Configuration config) {
		if (!isNormalized()) {
			System.err.println("Warning: unnormalized sum node");
		}
		V res = getSemiRing().zero();
		for (SPNElement<V> e: subs.keySet()) {
			res = getSemiRing().sum(res, getSemiRing().product(subs.get(e), e.getWeight(config)));
		}
		return res;
	}

	@Override
	public String toString() {
		if (subs.isEmpty()) {
			return "0";
		}
		String res = "(";
		for (SPNElement<V> e: subs.keySet()) res += e.toString() + "*" + subs.get(e) + "+";
		return res.substring(0, res.length()-1) + ")";
	}
	
	public boolean isNormalized() {
		V f = getSemiRing().zero();
		for (V v: subs.values()) {
			f = getSemiRing().sum(f, v);
		}
		return f.equals(getSemiRing().one());
	}

	@Override
	public boolean isComplete() {
		List<Set<String>> childVars = new ArrayList<Set<String>>();
		for (SPNElement<V> e: subs.keySet()) {
			if (!e.isComplete()) return false;
			childVars.add(e.getVariablesCovered().stream().collect(Collectors.toSet()));
		}
		for (Set<String> s1: childVars) {
			for (Set<String> s2: childVars) {
				if (!s1.equals(s2)) return false;
			}
		}
		return true;
	}

	@Override
	public boolean isConsistent() {
		for (SPNElement<V> e: subs.keySet()) {
			if (!e.isConsistent()) return false;
		}
		return true;
	}

	public List<String> getVariablesCovered() {
		return subs.keySet().stream()
				.map(e -> e.getVariablesCovered())
				.flatMap(l -> l.stream())
				.collect(Collectors.toList());
	}

	@Override
	public List<SPNIndicator<V>> getIndicatorsCovered() {
		return subs.keySet().stream()
				.map(e -> e.getIndicatorsCovered())
				.flatMap(l -> l.stream())
				.collect(Collectors.toList());
	}
	
	@Override
	public <V2> SPNElement<V2> convert(SPNTransformer<V, V2> trans) {
		return trans.transformSum(this);
	}

	public Set<SPNElement<V>> getSubs() {
		return subs.keySet();
	}
	
	


}
