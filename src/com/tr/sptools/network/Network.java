package com.tr.sptools.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tr.sptools.base.AbstractDistribution;
import com.tr.sptools.base.BaseTools;
import com.tr.sptools.base.Configuration;
import com.tr.sptools.base.Distribution;
import com.tr.sptools.semiring.SemiRing;

/**
 * A Bayesian Network.
 * 
 * It can utilize an arbitrary semiring for its values.  This includes probabilities but also ranks.
 * 
 * The structure of the BN is determined by the set of CPNs, which specify a variable and its parents.
 *
 * @param <V> Value type for weights (e.g. double for probabilities)
 */
public class Network<V> implements AbstractDistribution<V> {

	/** Semiring in use */
	private final SemiRing<V> semiRing;
	
	/** The conditional probability tables (CPTs) of this BN */
	public List<CWT<V>> cpts;
	
	/**
	 * Construct BN with given semiring and CPTs.
	 */
	public Network(SemiRing<V> semiRing, List<CWT<V>> cpts) {
		this.semiRing = semiRing;
		this.cpts = cpts;
		check();
	}
	
	/**
	 * Check if this BN is OK:
	 * - All 
	 */
	public void check() {
		if (!areAllVariablesUnique()) {
			throw new IllegalStateException("Not all variables are unique");
		}
		if (!doAllParentsExist()) {
			throw new IllegalStateException("Not all parents exist");
		}
		if (!isGraphLoopFree()) {
			throw new IllegalStateException("Cycle detected");
		}
	}
	
	/**
	 * @return True iff every variable in this BN is unique
	 */
	private boolean areAllVariablesUnique() {
		Set<String> vars = new HashSet<String>();
		for (CWT<V> cpt: cpts) {
			if (!vars.add(cpt.var)) return false;
		}
		return true;
	}

	/**
	 * @return True if for each parent var in each CPT, the var exists in this BN.
	 */
	private boolean doAllParentsExist() {
		Set<String> vars = new HashSet<String>();
		vars.addAll(getVariables());
		for (CWT<V> cpt: cpts) {
			for (String parent: cpt.parents) {
				if (!vars.contains(parent)) return false;
			}
		}
		return true;
	}

	/**
	 * @return True iff the BN graph is acyclic
	 */
	private boolean isGraphLoopFree() {
		for (String var: getVariables()) {
			if (isDescendant(var, var)) return false;
		}
		return true;
	}

	public Set<String> getVariables() {
		return cpts.stream()
				.map(cpt -> cpt.var)
				.collect(Collectors.toSet());
	}

	/**
	 * @return The conditional probability tables of this BN
	 */
	public List<CWT<V>> getCPTs() {
		return cpts;
	}

	/**
	 * @return Conditional probability table for given variable
	 */
	public CWT<V> getCPT(String var) {
		for (CWT<V> cpt: cpts) {
			if (cpt.var.equals(var)) return cpt;
		}
		throw new IllegalArgumentException("Illegal variable");
	}

	/**
	 * @return Conditional probabiltiy tables for given variables.
	 */
	public List<CWT<V>> getCPTs(Collection<String> vars) {
		return vars.stream()
				.map(var -> getCPT(var))
				.collect(Collectors.toList());
	}

	/**
	 * Returns children of given variable.
	 * 
	 * @param var A variable that is part of this BN
	 * @return Children of variable
	 */
	public List<String> getChildren(String var) {
		return cpts.stream()
				.filter(cpt -> cpt.parents.contains(var))
				.map(cpt -> cpt.var)
				.collect(Collectors.toList());
	}
	
	/**
	 * Return descendants of variable (children, children of children, etc.). 
	 * 
	 * @param var A variable that is part of this BN
	 * @return Descendants variables of variable 
	 */
	public List<String> getDescendants(String var) {
		List<String> children = getChildren(var);
		List<String> descendants = new ArrayList<String>();
		for (String child: children) {
			descendants.add(child);
		}
		for (String child: children) {
			descendants.addAll(getDescendants(child));
		}
		return descendants;
	}
	
	/**
	 * Returns true if d is a descendant of n.
	 * 
	 * @param d A variable that is part of this BN
	 * @param n A variable that is part of this BN
	 * @return true iff d is descendant of n.
	 */
	private boolean isDescendant(String d, String n) {
		for (String child: getChildren(n)) {
			if (d.equals(child) || isDescendant(d, child)) return true;
		}
		return false;
	}

	/**
	 * Return ancestors of variable (parents, parents of parents, etc.). 
	 * 
	 * @param var A variable that is part of this BN
	 * @return Ancestors variables of variable 
	 */
	public Set<String> getAncestors(String var) {
		Set<String> ancestors = new LinkedHashSet<String>();
		for (String parent: getParents(var)) {
			ancestors.addAll(getAncestors(parent));
		}
		for (String parent: getParents(var)) {
			ancestors.add(parent);
		}
		return ancestors;
	}

	/**
	 * Return ancestors of given set of variables (parents, parents of parents, etc.). 
	 * 
	 * @param var A set of variables that are part of this BN
	 * @return Ancestors of variables 
	 */
	public Set<String> getAncestors(Collection<String> vars) {
		Set<String> ancestors = new LinkedHashSet<String>();
		for (String var: vars) {
			ancestors.addAll(getAncestors(var));
		}
		return ancestors;
	}

	/** @return The nondescendants of given variable */
	public List<String> getNondescendants(String var) {
		List<String> variables = new ArrayList<String>();
		variables.addAll(getVariables());
		variables.removeAll(getDescendants(var));
		return variables;
	}

	/** @return The parents of given variable */
	public List<String> getParents(String var) {
		CWT<V> cpt = getCPT(var);
		if (cpt == null) throw new IllegalArgumentException();
		return cpt.getParents();
	}
	
	/** @return The family of given variable (parents + variable itself) */
	public List<String> getFamily(String var) {
		List<String> variables = new ArrayList<String>();
		CWT<V> cpt = getCPT(var);
		if (cpt == null) throw new IllegalArgumentException();
		variables.add(var);
		variables.addAll(cpt.getParents());
		return variables;
	}
	
	/** @return List of variables with no parents */
	public List<String> getRootNodes() {
		return getVariables().stream()
				.filter(var -> getParents(var).isEmpty())
				.collect(Collectors.toList());
	}

	/** @return List of variables that are not a parent of another variable */
	public List<String> getLeafNodes() {
		return getVariables().stream()
				.filter(var -> getChildren(var).isEmpty())
				.collect(Collectors.toList());
	}

	/**
	 * Return a new BN containing only variables relevant to 
	 * the given set of variables. This BN consists of the
	 * given variables as well as their ancestors.
	 * 
	 * @param vars A set of variables, all of which must be part of this BN.
	 * @return A restricted BN.
	 */
	public Network<V> restrict(Collection<String> vars) {
		Set<String> relevantVars = getAncestors(vars);
		relevantVars.addAll(vars);
		return new Network<V>(semiRing, getCPTs(relevantVars));
	}
	
	/**
	 * Return probability of given configuration. The configuration
	 * must contain all variables that are part of this BN, and no more.
	 *  
	 * @param config A configuration for this BN.
	 * @return Probability of configuration.
	 */
	private V getProbabilityOfFullConfig(Configuration config) {
		if (!getVariables().equals(config.getVariables())) {
			throw new IllegalArgumentException("Illegal variable set");
		}
		V p = semiRing.one();
		for (CWT<V> cpt: cpts) {
			p = semiRing.product(p, cpt.getWeight(config.getValue(cpt.getVariable()), config.restrict(cpt.getParents())));
		}
		return p;
	}

	public V getWeight(Configuration config) {
		return getWeight(Collections.singleton(config));
	}
	
	public Distribution<V> getCompleteDistribution() {
		Distribution<V> dist = new Distribution<V>(semiRing, cpts.stream().map(cpt -> cpt.var).collect(Collectors.toSet()));
		for (Configuration c: BaseTools.generateAllTotalConfigs(dist.getVariables())) {
			dist.set(c, getProbabilityOfFullConfig(c));
		}
		return dist;
	}
	
	public V getWeight(Collection<Configuration> configs) {
		// Extract relevant variables
		Set<String> variables = new LinkedHashSet<String>();
		for (Configuration c: configs) {
			variables.addAll(c.getVariables());
		}
		
		// Create relevant BN
		Network<V> relevantBN = restrict(variables);
		
		// Sum over all configurations of relevant BN
		V p = semiRing.zero();
		for (Configuration c: BaseTools.generateAllTotalConfigs(relevantBN.getVariables())) {

			// Check if some configuration of interest is consistent with it
			boolean consistent = false;
			for (Configuration d: configs) {
				if (c.isConsistent(d)) {
					consistent = true;
					break;
				}
			}
			
			// If so, get its probability and add to sum
			if (consistent) {
				p = semiRing.sum(p, relevantBN.getProbabilityOfFullConfig(c));
			}
		}

		
		return p;
	}

	@Override
	public SemiRing<V> getSemiRing() {
		return semiRing;
	}

}
