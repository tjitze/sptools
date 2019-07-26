package com.tr.sptools.semiring;

/**
 * Semiring for rational probabilities expressed by rationals
 */
public class RationalProbSemiRing extends SemiRing<RationalNumber> {

	private static final RationalProbSemiRing instance = new RationalProbSemiRing();
	
	public static RationalProbSemiRing getInstance() {
		return instance;
	}

	@Override
	public RationalNumber sum(RationalNumber v1, RationalNumber v2) {
		return v1.plus(v2);
	}

	@Override
	public RationalNumber product(RationalNumber v1, RationalNumber v2) {
		return v1.times(v2);
	}

	@Override
	public RationalNumber zero() {
		return new RationalNumber(0, 1);
	}

	@Override
	public RationalNumber one() {
		return new RationalNumber(1, 1);
	}
	
}
