package eu.ha3.util.property.simple;

import java.util.HashMap;
import java.util.Map;

import eu.ha3.util.property.contract.PropertyHolder;

public class PropertyCell implements PropertyHolder {
	private Map<String, String> properties;
	
	public PropertyCell() {
		properties = new HashMap<String, String>();
	}
	
	@Override
	public String getString(String name) {
		if (!properties.containsKey(name)) throw new PropertyMissingException();
		return properties.get(name);
	}
	
	@Override
	public boolean getBoolean(String name) {
		if (!properties.containsKey(name)) throw new PropertyMissingException();
		
		try {
			return Boolean.parseBoolean(this.properties.get(name));
		} catch (NumberFormatException e) {
			throw new PropertyTypeException();
		}
	}
	
	@Override
	public int getInteger(String name) {
		if (!properties.containsKey(name)) throw new PropertyMissingException();
		
		try {
			return Integer.parseInt(properties.get(name));
		} catch (NumberFormatException e) {
			throw new PropertyTypeException();
		}
	}
	
	@Override
	public float getFloat(String name) {
		if (!properties.containsKey(name)) throw new PropertyMissingException();
		
		try {
			return Float.parseFloat(properties.get(name));
		} catch (NumberFormatException e) {
			throw new PropertyTypeException();
		}
	}
	
	@Override
	public long getLong(String name) {
		if (!properties.containsKey(name)) throw new PropertyMissingException();
		
		try {
			return Long.parseLong(properties.get(name));
		} catch (NumberFormatException e) {
			throw new PropertyTypeException();
		}
	}
	
	@Override
	public double getDouble(String name) {
		if (!properties.containsKey(name)) throw new PropertyMissingException();
		
		try {
			return Double.parseDouble(properties.get(name));
		} catch (NumberFormatException e) {
			throw new PropertyTypeException();
		}
	}
	
	@Override
	public void setProperty(String name, Object o) {
		properties.put(name, o.toString());
	}
	
	@Override
	public Map<String, String> getAllProperties() {
		return properties;
	}
}
