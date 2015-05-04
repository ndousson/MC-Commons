package eu.ha3.mc.haddon;

public interface Haddon {
	/**
	 * Triggered during the addon loading process.
	 */
	public void onLoad();
	
	/**
	 * Returns the utility object dedicated to this haddon.
	 */
	public Utility getUtility();
	
	/**
	 * Sets the utility object dedicated to this haddon.
	 */
	public void setUtility(Utility utility);
	
	/**
	 * Returns the caster object dedicated to this haddon.
	 */
	public Operator getOperator();
	
	/**
	 * Sets the caster object dedicated to this haddon.
	 */
	public void setOperator(Operator operator);
	
	/**
	 * Returns the identity of this Haddon.
	 */
	public Identity getIdentity();
}
