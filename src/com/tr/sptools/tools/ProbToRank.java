package com.tr.sptools.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.tr.sptools.base.AbstractDistribution;
import com.tr.sptools.base.BaseTools;
import com.tr.sptools.base.Configuration;
import com.tr.sptools.base.Distribution;
import com.tr.sptools.semiring.ProbSemiRing;
import com.tr.sptools.semiring.RankSemiRing;

public class ProbToRank {

	/** 
	 * Convert probability distribution to ranking function.
	 * 
	 * Implements procedure described in "On Transformations between Probability 
	 * and Spohnian Disbelief Functions" by Giang and Shenoy (1999).
	 *  
	 * @param dist Probability distribution to convert
	 * @return Ranking function
	 */
	public static Distribution<Integer> convert(Distribution<Double> dist) {

		// Sort worlds according to probability
		List<Configuration> configs = new ArrayList<Configuration>();
		configs.addAll(dist.getConfigurations());
		Collections.sort(configs, new Comparator<Configuration>() {
			@Override
			public int compare(Configuration c1, Configuration c2) {
				return Double.compare(dist.getWeight(c1), dist.getWeight(c2));
			}
		});

		// Conversion routine
		Distribution<Integer> dist2 = new Distribution<Integer>(RankSemiRing.getInstance(), dist.getVariables());
		int r = 0;

		// m = remaining mass
		Double m = ProbSemiRing.getInstance().one();

		for (Configuration c: configs) {
			dist2.set(c, r);
			m = m - dist.getWeight(c);
			if (dist.getWeight(c) > m) r++;
		}
		
		return dist2;
	}
	
	/**
	 * Test if given probability and ranking functions are congruent.
	 * 
	 * The definition of congruence is given in definition 1 in the 
	 * paper "On Transformations between Probability and Spohnian Disbelief Functions" 
	 * by Giang and Shenoy (1999).
	 * 
	 * If this function returns false then the event which causes 
	 * failure of congruence is printed on the System.out.
	 * 
	 * Note that the complexity of this function is exponential wrt
	 * the size of the set of variables.
	 */
	public static boolean isCongruent(AbstractDistribution<Double> pf, AbstractDistribution<Integer> rf) {
		
		if (!pf.getVariables().equals(rf.getVariables())) {
			throw new IllegalArgumentException("Distributions must be over same set variables");
		}
		
		Set<Configuration> allConfigs = new LinkedHashSet<Configuration>();
		allConfigs.addAll(BaseTools.generateAllTotalConfigs(pf.getVariables()));
		Set<Set<Configuration>> allConfigSets = BaseTools.powerSet(allConfigs);
		
		for (Set<Configuration> A: allConfigSets) {
			for (Set<Configuration> B: allConfigSets) {
				if (pf.getWeight(A) >= pf.getWeight(B)) {
					if (rf.getWeight(A) > rf.getWeight(B)) {
						System.out.println("Not congruent. Events:");
						System.out.println("A: " + A);
						System.out.println("B: " + B);
						System.out.println("P(A) = " + pf.getWeight(A));
						System.out.println("P(B) = " + pf.getWeight(B));
						System.out.println("k(A) = " + rf.getWeight(A));
						System.out.println("k(B) = " + rf.getWeight(B));
						return false;
					}
				}
			}
		}
		return false;
	}
}
