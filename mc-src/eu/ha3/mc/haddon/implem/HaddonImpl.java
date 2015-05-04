package eu.ha3.mc.haddon.implem;

import eu.ha3.mc.haddon.Haddon;
import eu.ha3.mc.haddon.Operator;
import eu.ha3.mc.haddon.Utility;

public abstract class HaddonImpl implements Haddon {
	//protected String NAME = "_MOD_NAME_NOT_DEFINED";
	//protected int VERSION = -1;
	//protected String FOR = "0.0.0";
	//protected String ADDRESS = "http://example.org";
	
	private Utility utility;
	private Operator operator;
	
	@Override
	public Utility getUtility() {
		return utility;
	}
	
	@Override
	public void setUtility(Utility util) {
		utility = util;
	}
	
	@Override
	public Operator getOperator() {
		return operator;
	}
	
	@Override
	public void setOperator(Operator op) {
		operator = op;
	}
	
	/**
	 * Convenience shortener for getUtility()
	 */
	public Utility util() {
		return getUtility();
	}
	
	/**
	 * Convenience shortener for getCaster()
	 */
	public Operator op() {
		return getOperator();
	}
}
