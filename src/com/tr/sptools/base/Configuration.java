package com.tr.sptools.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a valuation of a set of variables
 */
public class Configuration {
	
	private HashMap<String, Boolean> map = new HashMap<String, Boolean>();
	
	public Configuration() {
	}

	public Configuration(Map<String, Boolean> map) {
		this.map.putAll(map);
	}

	public void putValue(String var, boolean value) {
		map.put(var, value);
	}

	public boolean getValue(String var) {
		return map.get(var);
	}

	public Configuration append(String var, boolean varValue) {
		Configuration config = new Configuration(map);
		config.putValue(var, varValue);
		return config;
	}

	public Configuration append(Configuration config) {
		Configuration newConfig = new Configuration(map);
		newConfig.map.putAll(config.map);
		return newConfig;
	}

	public Set<String> getVariables() {
		return map.keySet();
	}
	
	public boolean hasVariable(String var) {
		return map.containsKey(var);
	}
	
	public int hashCode() {
		return map.hashCode();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Configuration) {
			Configuration c = (Configuration)o;
			return c.map.equals(map);
		} else {
			return false;
		}
	}

	public Configuration copy() {
		Configuration config = new Configuration();
		config.map.putAll(map);
		return config;
	}
	
	public Configuration restrict(Collection<String> vars) {
		if (!getVariables().containsAll(vars)) {
			throw new IllegalArgumentException("Illegal vars");
		}
		Configuration config = new Configuration();
		for (String var: vars) {
			config.map.put(var, getValue(var));
		}
		return config;
	}
	
	/**
	 * Returns true if given config is consistent with this config.
	 * 
	 * More precisely: for every variable to which the given config assigns a value,
	 * the current config must assign the same value.
	 * 
	 * The variables of the given config must be a subset of the variables of this
	 * config.
	 * 
	 * @param config Another configuration whose variables are contained in this configuration's variables.
	 * @return True if given config is consistent with this one.
	 */
	public boolean isConsistent(Configuration config) {
		for (String var: config.getVariables()) {
			if (!map.containsKey(var)) throw new IllegalArgumentException("Illegal variable");
			if (getValue(var) != config.getValue(var)) return false;
		}
		return true;
	}
	
	public String toString() {
		List<String> vars = new ArrayList<String>();
		vars.addAll(getVariables());
		Collections.sort(vars);
		String a = "{";
		for (String var: vars) {
			a += "("+var+":"+(getValue(var)?"T":"F")+")";
		}
		a += "}";
		return a;
	}
	
	public static Configuration singleton(String var, boolean value) {
		return new Configuration(Collections.singletonMap(var, value));
	}	

}
