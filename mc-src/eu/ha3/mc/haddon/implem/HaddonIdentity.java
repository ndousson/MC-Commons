package eu.ha3.mc.haddon.implem;

import eu.ha3.mc.haddon.Identity;

public class HaddonIdentity implements Identity {
	protected final String NAME;
	
	protected final int VERSION;
	protected final String MCVERSION;
	protected final String ADDRESS;
	
	protected String PREFIX;
	
	public HaddonIdentity(String name, int version, String mc, String address) {
		NAME = name;
		VERSION = version;
		MCVERSION = mc;
		ADDRESS = address;
		PREFIX = "r";
	}
	
	public HaddonIdentity setPrefix(String prefix) {
		PREFIX = prefix;
		return this;
	}
	
	@Override
	public String getHaddonName() {
		return NAME;
	}
	
	@Override
	public int getHaddonVersionNumber() {
		return VERSION;
	}
	
	@Override
	public String getHaddonMinecraftVersion() {
		return MCVERSION;
	}
	
	@Override
	public String getHaddonAddress() {
		return ADDRESS;
	}
	
	@Override
	public String getHaddonHumanVersion() {
		return getHaddonVersionPrefix() + getHaddonVersionNumber() + " for " + getHaddonMinecraftVersion();
	}
	
	public String getHaddonVersionPrefix() {
		return PREFIX;
	}
}
