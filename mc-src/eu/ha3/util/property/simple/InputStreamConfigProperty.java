package eu.ha3.util.property.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import eu.ha3.util.property.contract.ConfigInputStream;
import eu.ha3.util.property.contract.PropertyHolder;
import eu.ha3.util.property.contract.Versionnable;

public class InputStreamConfigProperty implements PropertyHolder, Versionnable, ConfigInputStream {
	private VersionnableProperty mixed;
	
	public InputStreamConfigProperty() {
		mixed = new VersionnableProperty();
	}
	
	@Override
	public boolean loadStream(InputStream stream) {
		try {
			Reader reader = new InputStreamReader(stream);
			
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
