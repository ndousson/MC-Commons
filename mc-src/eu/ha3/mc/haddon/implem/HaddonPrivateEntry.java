package eu.ha3.mc.haddon.implem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.ha3.mc.haddon.PrivateAccessException;

public class HaddonPrivateEntry implements PrivateEntry {
	private final String name;
	private final Class target;
	private final int zero;
	private final String[] fieldNames;
	private final List<String> fieldNamesMoreToLess_depleting;
	
	public HaddonPrivateEntry(String name, Class target, int zero, String... lessToMoreImportantFieldName) {
		this.name = name;
		this.target = target;
		this.zero = zero;
		fieldNames = lessToMoreImportantFieldName.clone();
		fieldNamesMoreToLess_depleting = Arrays.asList(fieldNames);
		Collections.reverse(fieldNamesMoreToLess_depleting);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Class getTarget() {
		return target;
	}
	
	@Override
	public int getZero() {
		return zero;
	}
	
	@Override
	public String[] getFieldNames() {
		return fieldNames;
	}
	
	@Override
	public Object get(Object instance) throws PrivateAccessException {
		while (!fieldNamesMoreToLess_depleting.isEmpty()) {
			try {
				return HaddonUtilitySingleton.getInstance().getPrivateValueViaName(target, instance, fieldNamesMoreToLess_depleting.get(0));
			} catch (PrivateAccessException e) {
				HaddonUtilitySingleton.LOGGER.info("(Haddon) PrivateEntry " + name + " cannot resolve " + fieldNamesMoreToLess_depleting.get(0));
				fieldNamesMoreToLess_depleting.remove(0);
			}
		}
		if (zero >= 0) {
			try {
				return HaddonUtilitySingleton.getInstance().getPrivateValue(target, instance, zero);
			} catch (PrivateAccessException e) {
				HaddonUtilitySingleton.LOGGER.info("(Haddon) PrivateEntry " + name + " cannot resolve zero-index " + zero);
			}
		}
		
		generateError(); // will throw an exception
		return null;
	}
	
	@Override
	public void set(Object instance, Object value) throws PrivateAccessException {
		int i = this.fieldNames.length - 1;
		while (i >= 0) {
			try {
				HaddonUtilitySingleton.getInstance().setPrivateValueViaName(target, instance, fieldNames[i], value);
				return;
			} catch (PrivateAccessException e) {
			}
			i--;
		}
		if (zero >= 0) {
			try {
				HaddonUtilitySingleton.getInstance().setPrivateValue(this.target, instance, this.zero, value);
				return;
			} catch (PrivateAccessException e) {
			}
		}
		
		generateError(); // will throw an exception
	}
	
	private void generateError() throws PrivateAccessException {
		StringBuilder sb = new StringBuilder();
		for (int j = fieldNames.length - 1; j >= 0; j--) {
			sb.append(fieldNames[j]);
			sb.append(",");
		}
		sb.append("[").append(zero).append("]");
		throw new PrivateAccessException(name + "(" + sb + ") could not be resolved");
	}
}
