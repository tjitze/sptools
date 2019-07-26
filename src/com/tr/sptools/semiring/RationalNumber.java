package com.tr.sptools.semiring;

public class RationalNumber implements Comparable<RationalNumber> {
    private static RationalNumber zero = new RationalNumber(0, 1);

    private int num;   // the numerator
    private int den;   // the denominator

    // create and initialize a new Rational object
    public RationalNumber(int numerator, int denominator) {

        if (denominator == 0) {
            throw new ArithmeticException("denominator is zero");
        }

        // reduce fraction
        int g = gcd(numerator, denominator);
        num = numerator   / g;
        den = denominator / g;

        // needed only for negative numbers
        if (den < 0) { den = -den; num = -num; }
    }

    // return the numerator and denominator of (this)
    public int numerator()   { return num; }
    public int denominator() { return den; }

    // return double precision representation of (this)
    public double toDouble() {
        return (double) num / den;
    }

    // return string representation of (this)
    public String toString() { 
        if (den == 1) return num + "";
        else          return num + "/" + den;
    }

    // return { -1, 0, +1 } if a < b, a = b, or a > b
    public int compareTo(RationalNumber b) {
        RationalNumber a = this;
        int lhs = a.num * b.den;
        int rhs = a.den * b.num;
        if (lhs < rhs) return -1;
        if (lhs > rhs) return +1;
        return 0;
    }

    // is this Rational object equal to y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        RationalNumber b = (RationalNumber) y;
        return compareTo(b) == 0;
    }

    // hashCode consistent with equals() and compareTo()
    // (better to hash the numerator and denominator and combine)
    public int hashCode() {
        return this.toString().hashCode();
    }


    // create and return a new rational (r.num + s.num) / (r.den + s.den)
    public static RationalNumber mediant(RationalNumber r, RationalNumber s) {
        return new RationalNumber(r.num + s.num, r.den + s.den);
    }

    // return gcd(|m|, |n|)
    private static int gcd(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        if (0 == n) return m;
        else return gcd(n, m % n);
    }

    // return lcm(|m|, |n|)
    private static int lcm(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        return m * (n / gcd(m, n));    // parentheses important to avoid overflow
    }

    // return a * b, staving off overflow as much as possible by cross-cancellation
    public RationalNumber times(RationalNumber b) {
        RationalNumber a = this;

        // reduce p1/q2 and p2/q1, then multiply, where a = p1/q1 and b = p2/q2
        RationalNumber c = new RationalNumber(a.num, b.den);
        RationalNumber d = new RationalNumber(b.num, a.den);
        return new RationalNumber(c.num * d.num, c.den * d.den);
    }


    // return a + b, staving off overflow
    public RationalNumber plus(RationalNumber b) {
        RationalNumber a = this;

        // special cases
        if (a.compareTo(zero) == 0) return b;
        if (b.compareTo(zero) == 0) return a;

        // Find gcd of numerators and denominators
        int f = gcd(a.num, b.num);
        int g = gcd(a.den, b.den);

        // add cross-product terms for numerator
        RationalNumber s = new RationalNumber((a.num / f) * (b.den / g) + (b.num / f) * (a.den / g),
                                  lcm(a.den, b.den));

        // multiply back in
        s.num *= f;
        return s;
    }

    // return -a
    public RationalNumber negate() {
        return new RationalNumber(-num, den);
    }

    // return |a|
    public RationalNumber abs() {
        if (num >= 0) return this;
        else return negate();
    }

    // return a - b
    public RationalNumber minus(RationalNumber b) {
        RationalNumber a = this;
        return a.plus(b.negate());
    }


    public RationalNumber reciprocal() { return new RationalNumber(den, num);  }

    // return a / b
    public RationalNumber divides(RationalNumber b) {
        RationalNumber a = this;
        return a.times(b.reciprocal());
    }

}