package eu.ha3.mc.haddon.implem;

import eu.ha3.mc.haddon.Identity;

/*
--filenotes-placeholder
*/

public class HaddonIdentity implements Identity
{
	protected final String NAME;
	protected final int VERSION;
	protected final String FOR;
	protected final String ADDRESS;
	
	protected String PREFIX;
	
	public HaddonIdentity(String NAME, int VERSION, String FOR, String ADDRESS)
	{
		this.NAME = NAME;
		this.VERSION = VERSION;
		this.FOR = FOR;
		this.ADDRESS = ADDRESS;
	}
	
	public HaddonIdentity setPrefix(String prefix) {
		PREFIX = prefix;
		return this;
	}
	
	@Override
	public String getHaddonName()
	{
		return this.NAME;
	}
	
	@Override
	public int getHaddonVersionNumber()
	{
		return this.VERSION;
	}
	
	@Override
	public String getHaddonMinecraftVersion()
	{
		return this.FOR;
	}
	
	@Override
	public String getHaddonAddress()
	{
		return this.ADDRESS;
	}
	
	@Override
	public String getHaddonHumanVersion()
	{
		return getHaddonVersionPrefix() + getHaddonVersionNumber() + " for " + getHaddonMinecraftVersion();
	}
	
	public String getHaddonVersionPrefix() {
		return PREFIX;
	}
}
