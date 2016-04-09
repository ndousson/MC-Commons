package eu.ha3.mc.haddon;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;

public interface Client extends Keyer {
	public EntityPlayer getPlayer();
	
	public List<EntityPlayer> getAllPlayers();
	
	public FontRenderer getFontRenderer();
	
	public boolean registerReloadListener(IResourceManagerReloadListener reloadListener);
	
}
