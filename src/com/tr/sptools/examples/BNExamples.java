package com.tr.sptools.examples;

import java.util.ArrayList;
import java.util.List;

import com.tr.sptools.base.Configuration;
import com.tr.sptools.network.CWT;
import com.tr.sptools.network.Network;
import com.tr.sptools.semiring.ProbSemiRing;
import com.tr.sptools.semiring.RankSemiRing;
import com.tr.sptools.semiring.SemiRing;

/**
 * The burglary bayesian network example 
 */
public class BNExamples {

	/**
	 * Typical burglary bayesian network
	 */
	public static Network<Double> createBurglaryBN() {
 
		SemiRing<Double> prob = new ProbSemiRing();
		
		Configuration cfg;

		// Burglary
		CWT<Double> b = new CWT<Double>(prob, "b");
		cfg = new Configuration();
		b.setVarEntry(cfg, 0.001d, 1 - 0.001d);

		// Earthquake
		CWT<Double> e = new CWT<Double>(prob, "e");
		cfg = new Configuration();
		e.setVarEntry(cfg, 0.002d, 1 - 0.002d);

		// Alarm given Burglary and Earthquake
		CWT<Double> a = new CWT<Double>(prob, "a", "b", "e");
		cfg = new Configuration();
		cfg.putValue("b", true);
		cfg.putValue("e", true);
		a.setVarEntry(cfg, 0.95d, 1 - 0.95d);
		cfg = new Configuration();
		cfg.putValue("b", true);
		cfg.putValue("e", false);
		a.setVarEntry(cfg, 0.94d, 1 - 0.94d);
		cfg = new Configuration();
		cfg.putValue("b", false);
		cfg.putValue("e", true);
		a.setVarEntry(cfg, 0.29d, 1 - 0.29d);
		cfg = new Configuration();
		cfg.putValue("b", false);
		cfg.putValue("e", false);
		a.setVarEntry(cfg, 0.001d, 1 - 0.001d);

		// JohnCalls given Alarm
		CWT<Double> j = new CWT<Double>(prob, "j", "a");
		j.setVarEntry(Configuration.singleton("a", true), 0.90d, 1 - 0.90d);
		j.setVarEntry(Configuration.singleton("a", false), 0.05d, 1 - 0.05d);

		// MaryCalls given Alarm
		CWT<Double> m = new CWT<Double>(prob, "m", "a");
		m.setVarEntry(Configuration.singleton("a", true), 0.70d, 1 - 0.70d);
		m.setVarEntry(Configuration.singleton("a", false), 0.01d, 1 - 0.01d);

		List<CWT<Double>> cpts = new ArrayList<CWT<Double>>();
		cpts.add(b);
		cpts.add(e);
		cpts.add(a);
		cpts.add(j);
		cpts.add(m);
		
		return new Network<Double>(prob, cpts);

	}
	
	/**
	 * Car diagnosis ranking network
	 */
	public static Network<Integer> createCarDiagnosisRN() {
 
		SemiRing<Integer> rank = new RankSemiRing();
		
		Configuration cfg;

		// Headlight left on
		CWT<Integer> h = new CWT<Integer>(rank, "h");
		cfg = new Configuration();
		h.setVarEntry(cfg, 15, 0);
		
		// Fuel tank
		CWT<Integer> f = new CWT<Integer>(rank, "s");
		cfg = new Configuration();
		h.setVarEntry(cfg, 0, 10);

		// Battery dead given Headlights left on
		CWT<Integer> b = new CWT<Integer>(rank, "b", "h");
		cfg = new Configuration();
		cfg.putValue("h", true);
		b.setVarEntry(cfg, 4, 0);
		cfg = new Configuration();
		cfg.putValue("h", false);
		b.setVarEntry(cfg, 0, 8);

		// Car starts given Battery and Fuel tank
		CWT<Integer> s = new CWT<Integer>(rank, "s", "b", "f");
		cfg = new Configuration();
		cfg.putValue("b", true);
		cfg.putValue("f", true);
		b.setVarEntry(cfg, 0, 3);
		cfg = new Configuration();
		cfg.putValue("b", true);
		cfg.putValue("f", false);
		b.setVarEntry(cfg, 13, 0);
		cfg = new Configuration();
		cfg.putValue("b", false);
		cfg.putValue("f", true);
		b.setVarEntry(cfg, 11, 0);
		cfg = new Configuration();
		cfg.putValue("b", false);
		cfg.putValue("f", false);
		b.setVarEntry(cfg, 27, 0);

		List<CWT<Integer>> cpts = new ArrayList<CWT<Integer>>();
		cpts.add(h);
		cpts.add(b);
		cpts.add(f);
		cpts.add(s);
		
		return new Network<Integer>(rank, cpts);

	}

	
	public static void main(String[] args) {
		Network<Double> bn = createBurglaryBN();
		
		System.out.println("Complete distribution:");
		System.out.println(bn.getCompleteDistribution());

		System.out.println("");

		System.out.println("Normalization factor:");
		System.out.println(bn.getCompleteDistribution().getNormalizationFactor());

		Configuration cfg_ea = new Configuration();
		cfg_ea.putValue("e", true);
		cfg_ea.putValue("a", true);
		
		Configuration cfg_a = new Configuration();
		cfg_a.putValue("a", true);

		Configuration cfg_e = new Configuration();
		cfg_e.putValue("e", true);

		System.out.println("");

		System.out.printf("Probability of (e): %.10f\n", bn.getWeight(cfg_e));
		System.out.printf("Probability of (e & a): %.10f\n", bn.getWeight(cfg_ea));
		System.out.printf("Probability of (a): %.10f\n", bn.getWeight(cfg_a));
		System.out.printf("Probability of (e|a): %.10f\n", bn.getWeight(cfg_ea) / bn.getWeight(cfg_a) );

	}
}
