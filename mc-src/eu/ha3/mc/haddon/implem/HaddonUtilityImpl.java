package eu.ha3.mc.haddon.implem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Keyboard;

import eu.ha3.mc.haddon.PrivateAccessException;
import eu.ha3.mc.haddon.Utility;

public abstract class HaddonUtilityImpl implements Utility {
	private static final int WORLD_HEIGHT = 256;
	
	private Map<String, PrivateEntry> getters = new HashMap<String, PrivateEntry>();
	private Map<String, PrivateEntry> setters = new HashMap<String, PrivateEntry>();
	
	protected long ticksRan;
	protected File mcFolder;
	protected File modsFolder;
	
	/**
	 * Initialise reflection (Call the static constructor)
	 */
	public HaddonUtilityImpl() {
		HaddonUtilitySingleton.getInstance();
	}
	
	@Override
	public void registerPrivateGetter(String name, Class classToPerformOn, int zeroOffsets, String... lessToMoreImportantFieldName) {
		getters.put(name, new HaddonPrivateEntry(name, classToPerformOn, zeroOffsets, lessToMoreImportantFieldName));
	}
	
	@Override
	public void registerPrivateSetter(String name, Class classToPerformOn, int zeroOffsets, String... lessToMoreImportantFieldName) {
		setters.put(name, new HaddonPrivateEntry(name, classToPerformOn, zeroOffsets, lessToMoreImportantFieldName));
	}
	
	@Override
	public Object getPrivate(Object instance, String name) throws PrivateAccessException {
		return getters.get(name).get(instance);
	}
	
	@Override
	public void setPrivate(Object instance, String name, Object value) throws PrivateAccessException {
		setters.get(name).set(instance, value);
	}
	
	/**
	 * Warning: This may be removed in a later update.
	 */
	@Deprecated
	@Override
	public Object getPrivateValue(Class classToPerformOn, Object instanceToPerformOn, int zeroOffsets) throws PrivateAccessException {
		return HaddonUtilitySingleton.getInstance().getPrivateValue(classToPerformOn, instanceToPerformOn, zeroOffsets);
	}
	
	/**
	 * Warning: This may be removed in a later update.
	 */
	@Deprecated
	@Override
	public void setPrivateValue(Class classToPerformOn, Object instanceToPerformOn, int zeroOffsets, Object newValue) throws PrivateAccessException {
		HaddonUtilitySingleton.getInstance().setPrivateValue(
			classToPerformOn, instanceToPerformOn, zeroOffsets, newValue);
	}
	
	/**
	 * Warning: This may be removed in a later update.
	 */
	@Deprecated
	@Override
	public Object getPrivateValueLiteral(Class classToPerformOn, Object instanceToPerformOn, String obfPriority, int zeroOffsetsDebug) throws PrivateAccessException {
		Object ret;
		try {
			ret = HaddonUtilitySingleton.getInstance().getPrivateValueViaName(classToPerformOn, instanceToPerformOn, obfPriority);
		} catch (Exception e) {
			ret = HaddonUtilitySingleton.getInstance().getPrivateValue(classToPerformOn, instanceToPerformOn, zeroOffsetsDebug); // This throws a PrivateAccessException
		}
		return ret;
	}
	
	/**
	 * Warning: This may be removed in a later update.
	 */
	@Deprecated
	@Override
	public void setPrivateValueLiteral(Class classToPerformOn, Object instanceToPerformOn, String obfPriority, int zeroOffsetsDebug, Object newValue) throws PrivateAccessException {
		try {
			HaddonUtilitySingleton.getInstance().setPrivateValueViaName(classToPerformOn, instanceToPerformOn, obfPriority, newValue);
		} catch (PrivateAccessException e) {
			HaddonUtilitySingleton.getInstance().setPrivateValue(classToPerformOn, instanceToPerformOn, zeroOffsetsDebug, newValue); // This throws a PrivateAccessException
		}
	}
	
	@Override
	public int getWorldHeight() {
		return WORLD_HEIGHT;
	}
	
	@Override
	public Object getCurrentScreen() {
		return Minecraft.getMinecraft().currentScreen;
	}
	
	@Override
	public boolean isCurrentScreen(final Class classtype) {
		Object current = getCurrentScreen();
		if (classtype == null) return current == null;
		if (current == null) return false;
		return classtype.isInstance(current);
	}
		
	@Override
	public void closeCurrentScreen() {
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
	
	@Override
	public void printChat(Object... args) {
		if (Minecraft.getMinecraft().thePlayer == null) return;
		
		ChatComponentText message = new ChatComponentText("");
		ChatStyle style = null;
		for (Object o : args) {
			if (o instanceof EnumChatFormatting) {
				EnumChatFormatting code = (EnumChatFormatting)o;
				if (style == null) {
					style = new ChatStyle();
				}
				
				switch (code) {
					case OBFUSCATED:
						style.setObfuscated(true);
						break;
					case BOLD:
						style.setBold(true);
						break;
					case STRIKETHROUGH:
						style.setStrikethrough(true);
						break;
					case UNDERLINE:
						style.setUnderlined(true);
						break;
					case ITALIC:
						style.setItalic(true);
						break;
					case RESET:
						style = null;
						break;
					default:
						style.setColor(code);
				}
			} else if (o instanceof ClickEvent) {
				if (style == null) {
					style = new ChatStyle();
				}
				style.setChatClickEvent((ClickEvent)o);
			} else if (o instanceof HoverEvent) {
				if (style == null) {
					style = new ChatStyle();
				}
				style.setChatHoverEvent((HoverEvent)o);
			} else if (o instanceof IChatComponent) {
				if (o instanceof ChatComponentStyle) {
					if (style != null) {
						((ChatComponentStyle)o).setChatStyle(style);
						style = null;
					}
				}
				message.appendSibling((IChatComponent)o);
			} else if (o instanceof ChatStyle) {
				if (!((ChatStyle)o).isEmpty()) {
					if (style != null) {
						inheritFlat((ChatStyle)o, style);
					}
					style = ((ChatStyle)o);
				}
			} else {
				ChatComponentText line = new ChatComponentText(o.toString());
				if (style != null) {
					line.setChatStyle(style);
					style = null;
				}
				message.appendSibling(line);
			}
		}
		
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(message);
	}
	
    /**
     * Merges the given child ChatStyle into the given parent preserving hierarchical inheritance.
     * 
     * @param parent	The parent to inherit style information
     * @param child		The child style who's properties will override those in the parent
     */
    private void inheritFlat(ChatStyle parent, ChatStyle child) {
		if ((parent.getBold() != child.getBold()) && child.getBold()) {
			parent.setBold(true);
		}
		if ((parent.getItalic() != child.getItalic()) && child.getItalic()) {
			parent.setItalic(true);
		}
		if ((parent.getStrikethrough() != child.getStrikethrough()) && child.getStrikethrough()) {
			parent.setStrikethrough(true);
		}
		if ((parent.getUnderlined() != child.getUnderlined()) && child.getUnderlined()) {
			parent.setUnderlined(true);
		}
		if ((parent.getObfuscated() != child.getObfuscated()) && child.getObfuscated()) {
			parent.setObfuscated(true);
		}
        
        Object temp;
        if ((temp = child.getColor()) != null) {
        	parent.setColor((EnumChatFormatting)temp);
        }
        if ((temp = child.getChatClickEvent()) != null) {
        	parent.setChatClickEvent((ClickEvent)temp);
        }
        if ((temp = child.getChatHoverEvent()) != null) {
        	parent.setChatHoverEvent((HoverEvent)temp);
        }
        if ((temp = child.getInsertion()) != null) {
        	parent.setInsertion((String)temp);
        }
    }
	
	@Override
	public boolean areKeysDown(int... args) {
		for (int arg : args) {
			if (!Keyboard.isKeyDown(arg)) return false;
		}
		return true;
	}
	
	private ScaledResolution drawString_scaledRes = null;
	private int drawString_screenWidth;
	private int drawString_screenHeight;
	private int drawString_textHeight;
	
	@Override
	public void prepareDrawString() {
		Minecraft mc = Minecraft.getMinecraft();
		drawString_scaledRes = new ScaledResolution(mc);
		drawString_screenWidth = drawString_scaledRes.getScaledWidth();
		drawString_screenHeight = drawString_scaledRes.getScaledHeight();
		drawString_textHeight = mc.fontRendererObj.FONT_HEIGHT;
	}
	
	@Override
	public void drawString(String text, float px, float py, int offx, int offy, char alignment, int cr, int cg, int cb, int ca, boolean hasShadow) {
		if (drawString_scaledRes == null) prepareDrawString();
		
		Minecraft mc = Minecraft.getMinecraft();
		
		int xPos = (int) Math.floor(px * this.drawString_screenWidth) + offx;
		int yPos = (int) Math.floor(py * this.drawString_screenHeight) + offy;
		
		if (alignment == '2' || alignment == '5' || alignment == '8') {
			xPos = xPos - mc.fontRendererObj.getStringWidth(text) / 2;
		} else if (alignment == '3' || alignment == '6' || alignment == '9') {
			xPos = xPos - mc.fontRendererObj.getStringWidth(text);
		}
		
		if (alignment == '4' || alignment == '5' || alignment == '6') {
			yPos = yPos - this.drawString_textHeight / 2;
		} else if (alignment == '1' || alignment == '2' || alignment == '3') {
			yPos = yPos - this.drawString_textHeight;
		}
		
		int color = ca << 24 | cr << 16 | cg << 8 | cb;
		
		if (hasShadow) {
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, xPos, yPos, color);
		} else {
			Minecraft.getMinecraft().fontRendererObj.drawString(text, xPos, yPos, color);
		}
	}
	
	@Override
	public File getModsFolder() {
		if (modsFolder == null) {
			modsFolder = new File(Minecraft.getMinecraft().mcDataDir, "mods");
		}
		return modsFolder;
	}
	
	@Override
	public File getMcFolder() {
		if (mcFolder == null) {
			mcFolder = Minecraft.getMinecraft().mcDataDir;
		}
		return mcFolder;
	}
}
