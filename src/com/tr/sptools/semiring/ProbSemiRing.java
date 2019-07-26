package com.tr.sptools.semiring;

/**
 * Semiring for probabilities: (0, 1, +, *)
 */
public class ProbSemiRing extends SemiRing<Double> {

	private static final ProbSemiRing instance = new ProbSemiRing();
	
	public static ProbSemiRing getInstance() {
		return instance;
	}

	@Override
	public Double sum(Double v1, Double v2) {
		return v1 + v2;
	}

	@Override
	public Double product(Double v1, Double v2) {
		return v1 * v2;
	}

	@Override
	public Double zero() {
		return 0.0;
	}

	@Override
	public Double one() {
		return 1.0;
	}
	
}
