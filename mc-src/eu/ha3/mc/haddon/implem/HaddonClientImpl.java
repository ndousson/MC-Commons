package eu.ha3.mc.haddon.implem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import eu.ha3.mc.haddon.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class HaddonClientImpl implements Client {
	
	protected Minecraft unsafe() {
		return Minecraft.getMinecraft();
	}

	@Override
	public EntityPlayer getPlayer() {
		return unsafe().thePlayer;
	}
	
	@Override
	public List<EntityPlayer> getAllPlayers() {
		/*World w = unsafe().theWorld;
		if (w != null) {
			List<EntityPlayer> result = w.playerEntities;
			return result;
		}*/
		MinecraftServer server = MinecraftServer.getServer();
		if (server != null) {
			List<EntityPlayer> result = server.worldServers[getPlayer().dimension].playerEntities;
			return result;
			//return (List<EntityPlayer>)(List)server.getConfigurationManager().playerEntityList;
		}
		return new ArrayList<EntityPlayer>();
		
	}

	@Override
	public FontRenderer getFontRenderer() {
		return unsafe().fontRendererObj;
	}

	@Override
	public void addKeyBinding(KeyBinding binding) {
		Minecraft mc = unsafe();
		mc.gameSettings.keyBindings = ArrayUtils.addAll(mc.gameSettings.keyBindings, binding);
		KeyBinding.resetKeyBindingArrayAndHash();
	}
	
	@Override
	public void removeKeyBinding(KeyBinding binding) {
		Minecraft mc = unsafe();
		List<KeyBinding> kept = new ArrayList<KeyBinding>();
		for (KeyBinding i : mc.gameSettings.keyBindings) {
			if (!i.equals(binding)) kept.add(i);
		}
		if (kept.size() != mc.gameSettings.keyBindings.length) {
			mc.gameSettings.keyBindings = kept.toArray(new KeyBinding[kept.size()]);
			KeyBinding.resetKeyBindingArrayAndHash();
		}
	}
	
	@Override
	public boolean registerReloadListener(IResourceManagerReloadListener reloadListener) {
		IResourceManager manager = unsafe().getResourceManager();
		if (manager instanceof IReloadableResourceManager) {
			((IReloadableResourceManager)manager).registerReloadListener(reloadListener);
			return true;
		}
		return false;
	}
}
