package com.tr.sptools.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.tr.sptools.base.Configuration;
import com.tr.sptools.semiring.ProbSemiRing;
import com.tr.sptools.semiring.RankSemiRing;
import com.tr.sptools.spn.SPNElement;
import com.tr.sptools.spn.SPNProductNode;
import com.tr.sptools.spn.SPNSumNode;
import com.tr.sptools.spn.SPNTransformer;

/**
 * Transforms probability SPN to ranking SPN.
 * 
 * 
 */
public class PR_SPNTransformer extends SPNTransformer<Double, Integer> {
	
	public PR_SPNTransformer() {
		super(RankSemiRing.getInstance());
	}

	@Override
	public SPNProductNode<Integer> transformProduct(SPNProductNode<Double> product) {
		SPNProductNode<Integer> p = new SPNProductNode<Integer>(RankSemiRing.getInstance());
		for (SPNElement<Double> sub: product.getSubs()) {
			p.add(sub.convert(this));
		}
		return p;
	}

	@Override
	public SPNSumNode<Integer> transformSum(SPNSumNode<Double> sum) {
		
		// Sort children from highest probabiltiy to lowest probabilty
		List<SPNElement<Double>> subs = new ArrayList<SPNElement<Double>>();
		subs.addAll(sum.getSubs());
		Collections.sort(subs, new Comparator<SPNElement<Double>>() {
			@Override
			public int compare(SPNElement<Double> o1, SPNElement<Double> o2) {
				return -Double.compare(sum.getWeight(o1), sum.getWeight(o2));
			}
		});

		// Create the new rank-based SPN sum node
		SPNSumNode<Integer> s = new SPNSumNode<Integer>(RankSemiRing.getInstance());

		// Apply procedure to convert probabilities to ranks and add children
		int r = 0;
		Double m = ProbSemiRing.getInstance().one();
		for (SPNElement<Double> el: subs) {
			s.add(el.convert(this), r);
			m = m - sum.getWeight(el);
			if (sum.getWeight(el) > m) r++;
		}
		
		return s;
	}

}
