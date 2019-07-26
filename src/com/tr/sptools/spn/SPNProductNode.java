package com.tr.sptools.spn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.tr.sptools.base.Configuration;
import com.tr.sptools.semiring.SemiRing;

public class SPNProductNode<V> extends SPNElement<V> {
	
	private List<SPNElement<V>> subs = new ArrayList<SPNElement<V>>();

	public SPNProductNode(SemiRing<V> ops) {
		super(ops);
	}

	public SPNProductNode(SemiRing<V> ops, List<SPNElement<V>> subs) {
		super(ops);
		this.subs.addAll(subs);
	}

	public void add(SPNElement<V> e) {
		subs.add(e);
	}

	@Override
	public V getWeight(Configuration config) {
		V res = getSemiRing().one();
		for (SPNElement<V> e: subs) {
			V v = e.getWeight(config);
			if (v.equals(getSemiRing().zero())) return getSemiRing().zero();
			res = getSemiRing().product(res, v);
		}
		return res;
	}

	@Override
	public String toString() {
		if (subs.isEmpty()) {
			return "1";
		}
		String res = "(";
		for (SPNElement<V> e: subs) res += e.toString() + "*";
		return res.substring(0, res.length()-1) + ")";
	}
	
	@Override
	public List<String> getVariablesCovered() {
		return subs.stream()
				.map(e -> e.getVariablesCovered())
				.flatMap(l -> l.stream())
				.collect(Collectors.toList());
	}

	@Override
	public boolean isComplete() {
		for (SPNElement<V> e: subs) {
			if (!e.isComplete()) return false;
		}
		return true;
	}

	@Override
	public boolean isConsistent() {
		for (SPNElement<V> e1: subs) {
			for (SPNElement<V> e2: subs) {
				if (e1 != e2) {
					for (SPNIndicator<V> i1: e1.getIndicatorsCovered()) {
						for (SPNIndicator<V> i2: e2.getIndicatorsCovered()) {
							if (i1.getVariable().equals(i2.getVariable()) &&
									i1.getValue() != i2.getValue()) {
								return false;
							}
						}
					}

				}
			}
		}
		return true;		
	}

	@Override
	public List<SPNIndicator<V>> getIndicatorsCovered() {
		return subs.stream()
				.map(e -> e.getIndicatorsCovered())
				.flatMap(l -> l.stream())
				.collect(Collectors.toList());
	}
	
	@Override
	public <V2> SPNElement<V2> convert(SPNTransformer<V, V2> trans) {
		return trans.transformProduct(this);
	}

	public List<SPNElement<V>> getSubs() {
		return subs;
	}
}
