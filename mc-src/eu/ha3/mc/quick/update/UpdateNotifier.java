package eu.ha3.mc.quick.update;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.ha3.mc.quick.chat.Chatter;
import eu.ha3.util.property.simple.ConfigProperty;

/* x-placeholder-wtfplv2 */

/**
 * The Update Notifier.
 * 
 * @author Hurry
 * 
 */
public class UpdateNotifier extends Thread {
	private final boolean USE_JSON = true;
	
	private final NotifiableHaddon haddon;
	private final String queryLocation;
	
	private int lastFound;
	
	private int displayCount = 3;
	private int displayRemaining = 0;
	private boolean enabled = true;
	
	public UpdateNotifier(NotifiableHaddon mod, String query) {
		haddon = mod;
		queryLocation = query;
		lastFound = mod.getIdentity().getHaddonVersionNumber();
	}
	
	public void attempt() {
		if (enabled) start();
	}
	
	@Override
	public void run() {
		try {
			checkUpdates();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkUpdates() throws Exception {
		final int currentVersionNumber = haddon.getIdentity().getHaddonVersionNumber();
		final String currentVersionType = haddon.getIdentity().getHaddonVersionPrefix();
		
		URL url = new URL(String.format(queryLocation, currentVersionNumber));
		
		InputStream contents = url.openStream();
		
		int solvedVersion = 0;
		String solvedVersionType = "r";
		String solvedMinecraftVersion = "";
		if (USE_JSON) {
			String jasonString = IOUtils.toString(contents, "UTF-8");
			
			JsonObject jason = new JsonParser().parse(jasonString).getAsJsonObject();
			JsonArray versions = jason.get("versions").getAsJsonArray();
			for (JsonElement element : versions.getAsJsonArray()) {
				JsonObject o = element.getAsJsonObject();
				int vn = o.get("number").getAsInt();
				if (vn > solvedVersion) {
					solvedVersion = vn;
					if (o.has("for")) {
						solvedMinecraftVersion = o.get("for").getAsString();
					}
				}
				if (o.has("type")) {
					solvedVersionType = o.get("type").getAsString();
				}
			}
		} else {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(contents);
			
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			NodeList nl = doc.getElementsByTagName("release");
			
			for (int i = 0; i < nl.getLength(); i++) {
				Node release = nl.item(i);
				String versionnumber = xp.evaluate("./version", release);
				if (versionnumber != null) {
					int vn = Integer.parseInt(versionnumber);
					if (vn > solvedVersion) solvedVersion = vn;
				}
				String versiontype = xp.evaluate("./type", release);
				if (versiontype != null) {
					solvedVersionType = versiontype;
				}
			}
		}
		
		System.out.println("(UN: " + haddon.getIdentity().getHaddonName() + ") Update version found: " + solvedVersion + " (running " + currentVersionNumber + ")");
		
		Thread.sleep(10000);
		
		if (solvedVersion > currentVersionNumber || (solvedVersion >= currentVersionNumber && !solvedVersionType.contentEquals(currentVersionType))) {
			ConfigProperty config = haddon.getConfig();
			Chatter chatter = haddon.getChatter();
			
			boolean needsSave = false;
			if (solvedVersion != lastFound) {
				lastFound = solvedVersion;
				displayRemaining = displayCount;
				
				needsSave = true;
				config.setProperty("update_found.version", lastFound);
				config.setProperty("update_found.display.remaining.value", displayRemaining);
			}
			
			if (displayRemaining > 0) {
				config.setProperty("update_found.display.remaining.value", displayRemaining--);
				
				int vc = solvedVersion - currentVersionNumber;
				if (vc == 0) vc++;
				
				if (solvedMinecraftVersion.equals("")) {
					chatter.printChat(EnumChatFormatting.GOLD, "An update is available: ", solvedVersionType + solvedVersion);
				} else if (solvedMinecraftVersion.equals(haddon.getIdentity().getHaddonMinecraftVersion())) {
					chatter.printChat(EnumChatFormatting.GOLD, "An update is available for your version of Minecraft: ", solvedVersionType + solvedVersion);
				} else {
					chatter.printChat(
							EnumChatFormatting.GOLD, "An update is available for ",
							EnumChatFormatting.GOLD, EnumChatFormatting.ITALIC, "another",
							EnumChatFormatting.GOLD, " version of Minecraft: ",
							solvedVersionType + solvedVersion + " for " + solvedMinecraftVersion);
				}
				chatter.printChatShort(EnumChatFormatting.GOLD, "You're ", EnumChatFormatting.WHITE, vc, EnumChatFormatting.GOLD, " version" + (vc > 1 ? "s" : "") + " late.");
				chatter.printChatShort(EnumChatFormatting.UNDERLINE, new ClickEvent(ClickEvent.Action.OPEN_URL, haddon.getIdentity().getHaddonAddress()), haddon.getIdentity().getHaddonAddress());
				
				if (displayRemaining > 0) {
					chatter.printChatShort(
							EnumChatFormatting.GRAY, "This message will display ",
							EnumChatFormatting.WHITE, displayRemaining,
							EnumChatFormatting.GRAY, " more time" + (displayRemaining > 1 ? "s" : "") + ".");
				} else {
					chatter.printChatShort(EnumChatFormatting.GRAY, "You won't be notified anymore unless a newer version comes out.");
				}
				needsSave = true;
			}
			
			if (needsSave) haddon.saveConfig();
		}
	}
	
	public void fillDefaults(ConfigProperty configuration) {
		configuration.setProperty("update_found.enabled", true);
		configuration.setProperty("update_found.version", haddon.getIdentity().getHaddonVersionNumber());
		configuration.setProperty("update_found.display.remaining.value", 0);
		configuration.setProperty("update_found.display.count.value", 3);
	}
	
	public void loadConfig(ConfigProperty configuration) {
		enabled = configuration.getBoolean("update_found.enabled");
		lastFound = configuration.getInteger("update_found.version");
		displayRemaining = configuration.getInteger("update_found.display.remaining.value");
		displayCount = configuration.getInteger("update_found.display.count.value");
	}
	
}
