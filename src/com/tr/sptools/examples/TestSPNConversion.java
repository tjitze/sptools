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
import com.tr.sptools.tools.PR_SPNTransformer;
import com.tr.sptools.tools.ProbToRank;

public class TestSPNConversion {

	public static void main(String[] args) {
	
		SPNElement<Double> testSPN = SPNExamples.createExample2();
				
		SPNElement<Integer> rankingSPN = testSPN.convert(new PR_SPNTransformer());
		
		System.out.println(ProbToRank.isCongruent(testSPN, rankingSPN));
	}
	
	
}
