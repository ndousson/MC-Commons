package eu.ha3.mc.quick.chat;

import net.minecraft.util.EnumChatFormatting;
import eu.ha3.mc.haddon.Haddon;

public class Chatter {
	private final Haddon mod;
	private final String prefix;
	
	public Chatter(Haddon mod, String prefix) {
		this.mod = mod;
		this.prefix = prefix;
	}
	
	public void printChat(Object... args) {
		printChat(new Object[] { EnumChatFormatting.WHITE, prefix }, args);
	}
	
	public void printChatShort(Object... args) {
		mod.getUtility().printChat(args);
	}
	
	protected void printChat(final Object[] in, Object... args) {
		Object[] dest = new Object[in.length + args.length];
		System.arraycopy(in, 0, dest, 0, in.length);
		System.arraycopy(args, 0, dest, in.length, args.length);
		mod.getUtility().printChat(dest);
	}
}
