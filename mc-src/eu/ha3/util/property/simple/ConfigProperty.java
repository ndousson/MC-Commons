package eu.ha3.util.property.simple;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;

import eu.ha3.util.property.contract.ConfigSource;
import eu.ha3.util.property.contract.PropertyHolder;
import eu.ha3.util.property.contract.Versionnable;

public class ConfigProperty implements PropertyHolder, Versionnable, ConfigSource {
	private VersionnableProperty mixed;
	
	private String path;
	
	public ConfigProperty() {
		mixed = new VersionnableProperty();
	}
	
	@Override
	public void setSource(String source) {
		path = source;
	}
	
	@Override
	public boolean load() {
		File file = new File(path);
		if (file.exists()) {
			try {
				Reader reader = new FileReader(file);
				
				Properties props = new Properties();
				props.load(reader);
				
				for (Entry<Object, Object> entry : props.entrySet()) {
					mixed.setProperty(entry.getKey().toString(), entry.getValue().toString());
				}
				mixed.commit();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				mixed.revert();
			}
		}
		return false;
	}
	
	@Override
	public boolean save() {
		try {
			File userFile = new File(this.path);
			Properties props = new Properties() {
				@Override
				public synchronized Enumeration<Object> keys() {
					return Collections.enumeration(new TreeSet<Object>(super.keySet()));
				}
			};
			for (Entry<String, String> property : mixed.getAllProperties().entrySet()) {
				props.setProperty(property.getKey(), property.getValue());
			}
			props.store(new FileWriter(userFile), "");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean commit() {
		return mixed.commit();
	}
	
	@Override
	public void revert() {
		mixed.revert();
	}
	
	@Override
	public String getString(String name) {
		return mixed.getString(name);
	}
	
	@Override
	public boolean getBoolean(String name) {
		return mixed.getBoolean(name);
	}
	
	@Override
	public int getInteger(String name) {
		return mixed.getInteger(name);
	}
	
	@Override
	public float getFloat(String name) {
		return mixed.getFloat(name);
	}
	
	@Override
	public long getLong(String name) {
		return mixed.getLong(name);
	}
	
	@Override
	public double getDouble(String name) {
		return mixed.getDouble(name);
	}
	
	@Override
	public void setProperty(String name, Object o) {
		mixed.setProperty(name, o);
	}
	
	@Override
	public Map<String, String> getAllProperties() {
		return mixed.getAllProperties();
	}
	
}
