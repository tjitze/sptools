package com.tr.sptools.spn;

import java.util.Collections;
import java.util.List;

import com.tr.sptools.base.Configuration;
import com.tr.sptools.semiring.SemiRing;

/**
 * An indicator is a variable plus a value that the variable can take.
 * 
 * @param <V>
 */
public class SPNIndicator<V> extends SPNElement<V> {

	private final String var;	
	private boolean value;
	
	public SPNIndicator(SemiRing<V> semiRing, String var, boolean value) {
		super(semiRing);
		this.var = var;
		this.value = value;
	}
	
	/**
	 * @return The variable of this indicator
	 */
	public String getVariable() {
		return var;
	}
	
	/**
	 * @return The value of this indicator
	 */
	public boolean getValue() {
		return value;
	}

	public V getWeight(Configuration config) {
		if (!config.getVariables().contains(var)) {
			throw new IllegalArgumentException("Illegal variable, config " + config + " should contain variable " + var);
		}
		return config.getValue(var) == value? getSemiRing().one(): getSemiRing().zero();
	}
	
	public String toString() {
		return "I["+var+"="+(value?"T":"F")+"]";
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public boolean isConsistent() {
		return true;
	}
	
	@Override
	public List<String> getVariablesCovered() {
		return Collections.singletonList(var);
	}

	@Override
	public List<SPNIndicator<V>> getIndicatorsCovered() {
		return Collections.singletonList(this);
	}

	@Override
	public <V2> SPNElement<V2> convert(SPNTransformer<V, V2> trans) {
		return trans.transformIndicator(this);
	}

}
