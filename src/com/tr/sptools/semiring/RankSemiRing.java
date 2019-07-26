package com.tr.sptools.semiring;

/**
 * Semiring for ranks: (infinity, 0, min, +)
 */
public class RankSemiRing extends SemiRing<Integer> {
	
	private static final RankSemiRing instance = new RankSemiRing();
	
	public static RankSemiRing getInstance() {
		return instance;
	}
	
	@Override
	public Integer sum(Integer v1, Integer v2) {
		return Math.min(v1, v2);
	}

	@Override
	public Integer product(Integer v1, Integer v2) {
		try {
			return Math.addExact(v1, v2);
		} catch (ArithmeticException ex) {
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public Integer zero() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Integer one() {
		return 0;
	}
	
}
