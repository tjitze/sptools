package com.tr.sptools.examples;

import java.util.ArrayList;
import java.util.List;

import com.tr.sptools.base.BaseTools;
import com.tr.sptools.base.Configuration;
import com.tr.sptools.semiring.ProbSemiRing;
import com.tr.sptools.semiring.SemiRing;
import com.tr.sptools.spn.SPNElement;
import com.tr.sptools.spn.SPNIndicator;
import com.tr.sptools.spn.SPNProductNode;
import com.tr.sptools.spn.SPNSumNode;

public class SPNExamples {

	public static SPNElement<Double> createExample1() {

		SemiRing<Double> prob = new ProbSemiRing();
		
		SPNSumNode<Double> a1 = new SPNSumNode<Double>(prob);
		SPNProductNode<Double> a2 = new SPNProductNode<Double>(prob);
		SPNProductNode<Double> b2 = new SPNProductNode<Double>(prob);
	
		SPNSumNode<Double> a3 = new SPNSumNode<Double>(prob);
		SPNSumNode<Double> b3 = new SPNSumNode<Double>(prob);
		SPNSumNode<Double> c3 = new SPNSumNode<Double>(prob);
		SPNSumNode<Double> d3 = new SPNSumNode<Double>(prob);

		SPNIndicator<Double> a4 = new SPNIndicator<Double>(prob, "x1", true);
		SPNIndicator<Double> b4 = new SPNIndicator<Double>(prob, "x1", false);
		SPNIndicator<Double> c4 = new SPNIndicator<Double>(prob, "x2", true);
		SPNIndicator<Double> d4 = new SPNIndicator<Double>(prob, "x2", false);

		a1.add(a2, 0.7);
		a1.add(b2, 0.3);

		a2.add(a3);
		a2.add(c3);
		
		b2.add(b3);
		b2.add(d3);

		a3.add(a4, 0.6);
		a3.add(b4, 0.4);
		
		b3.add(a4, 0.9);
		b3.add(b4, 0.1);

		c3.add(c4, 0.3);
		c3.add(d4, 0.7);
		
		d3.add(c4, 0.2);
		d3.add(d4, 0.8);

		if (!a1.isComplete() || !a1.isConsistent()) {
			throw new RuntimeException();
		}

		return a1;
		
	}

	public static SPNElement<Double> createExample2() {

		SemiRing<Double> prob = new ProbSemiRing();
		
		SPNSumNode<Double> a1 = new SPNSumNode<Double>(prob);
		
		SPNProductNode<Double> a2 = new SPNProductNode<Double>(prob);
		SPNProductNode<Double> b2 = new SPNProductNode<Double>(prob);

		SPNIndicator<Double> a3 = new SPNIndicator<Double>(prob, "x1", true);

		SPNSumNode<Double> b3 = new SPNSumNode<Double>(prob);
		SPNSumNode<Double> c3 = new SPNSumNode<Double>(prob);
		SPNSumNode<Double> d3 = new SPNSumNode<Double>(prob);
		SPNSumNode<Double> e3 = new SPNSumNode<Double>(prob);
		
		SPNIndicator<Double> f3 = new SPNIndicator<Double>(prob, "x1", false);
		SPNIndicator<Double> a4 = new SPNIndicator<Double>(prob, "x2", true);
		SPNIndicator<Double> b4 = new SPNIndicator<Double>(prob, "x2", false);
		SPNIndicator<Double> c4 = new SPNIndicator<Double>(prob, "x3", true);
		SPNIndicator<Double> d4 = new SPNIndicator<Double>(prob, "x3", false);
		
		a1.add(a2, 0.8);
		a1.add(b2, 0.2);

		a2.add(a3);
		a2.add(b3);
		a2.add(d3);
		
		b2.add(c3);
		b2.add(e3);
		b2.add(f3);

		b3.add(a4, 0.3);
		b3.add(b4, 0.7);

		c3.add(a4, 0.5);
		c3.add(b4, 0.5);

		d3.add(c4, 0.6);
		d3.add(d4, 0.4);

		e3.add(c4, 0.9);
		e3.add(d4, 0.1);

		if (!a1.isComplete() || !a1.isConsistent()) {
			throw new RuntimeException();
		}
		
		return a1;
		
	}

	public static void main(String[] args) {
		
		SPNElement<Double> x = createExample1();
		
		System.out.println("Example: " + x);

		List<String> variables = new ArrayList<String>();
		variables.add("x1");
		variables.add("x2");
		
		List<Configuration> allConfigs = BaseTools.generateAllTotalConfigs(variables);
		
		System.out.println("Complete   : " + x.isComplete());
		System.out.println("Consistent : " + x.isConsistent());
		
		double sum = 0.0;
		for (Configuration cfg: allConfigs) {
			double p = x.getWeight(cfg);
			sum += p;
			System.out.println("Config: " + cfg + " P: " + p);
		}
		System.out.println("Sum: " + sum);

	}
	

}
