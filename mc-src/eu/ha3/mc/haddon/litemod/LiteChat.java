package eu.ha3.mc.haddon.litemod;

import com.mumfrey.liteloader.ChatListener;

import eu.ha3.mc.haddon.Haddon;
import eu.ha3.mc.haddon.OperatorChatWatcher;
import eu.ha3.mc.haddon.supporting.SupportsChatEvents;
import net.minecraft.util.text.ITextComponent;

public class LiteChat extends LiteBase implements OperatorChatWatcher, ChatListener {
	protected final boolean suChat;
	
	protected boolean enableChat;
	
	public LiteChat(Haddon haddon) {
		super(haddon);
		suChat = haddon instanceof SupportsChatEvents;
	}
	
	@Override
	public void setChatEnabled(boolean enabled) {
		enableChat = enabled;
	}
	
	@Override
	public void onChat(ITextComponent chat, String message) {
		if (suChat && enableChat) {
			((SupportsChatEvents) haddon).onChat(chat, message);
		}
	}
}
