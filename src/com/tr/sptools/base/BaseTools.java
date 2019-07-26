package com.tr.sptools.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BaseTools {

	public static <T> Set<Set<T>> powerSet(Collection<T> originalSet) {
	    Set<Set<T>> sets = new LinkedHashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	        sets.add(new LinkedHashSet<T>());
	        return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new LinkedHashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	        Set<T> newSet = new LinkedHashSet<T>();
	        newSet.add(head);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }       
	    return sets;
	} 
	
	public static List<Configuration> generateAllPartialConfigs(Collection<String> variables) {
		List<Configuration> configs = new ArrayList<Configuration>();
		Set<Set<String>> powerset = BaseTools.powerSet(variables);
		for (Set<String> subset: powerset) {
			configs.addAll(generateAllTotalConfigs(subset));
		}
		return configs;
	}
		
	public static List<Configuration> generateAllTotalConfigs(Collection<String> variables) {
		List<String> vars = new ArrayList<String>();
		vars.addAll(variables);
		
		List<Configuration> res = new ArrayList<Configuration>();
		Configuration config = new Configuration();
		for (String var: vars) {
			config.putValue(var, false);
		}
		
		res.add(config);
		boolean done = false;
		outer: while (!done) {
			config = config.copy();
			for (int i = 0; i < vars.size(); i++) {
				if (config.getValue(vars.get(i))) {
					config.putValue(vars.get(i), false);
				} else {
					config.putValue(vars.get(i), true);
					res.add(config);
					continue outer;
				}
			}
			done = true;
		}
		
		return res;
	}


}
