package eu.ha3.mc.haddon.implem;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import eu.ha3.mc.haddon.PrivateAccessException;

public class HaddonUtilitySingleton {
	final static public Logger LOGGER = Logger.getLogger("HaddonUtilitySingleton");
	private static final HaddonUtilitySingleton instance = new HaddonUtilitySingleton();
	
	private Field fieldMod;
	
	private HaddonUtilitySingleton() {
		try {
			fieldMod = java.lang.reflect.Field.class.getDeclaredField("modifiers");
			fieldMod.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("haddonUtility critical failure", e);
		}
	}
	
	public static HaddonUtilitySingleton getInstance() {
		return instance;
	}
	
	public Field getFieldModifiers() {
		return fieldMod;
	}
	
	public Object getPrivateValue(Class<?> classToPerformOn, Object instanceToPerformOn, int zeroOffsets) throws PrivateAccessException {
		try {
			Field field = classToPerformOn.getDeclaredFields()[zeroOffsets];
			field.setAccessible(true);
			return field.get(instanceToPerformOn);
		} catch (ReflectiveOperationException e) {
			throw new PrivateAccessException("getPrivateValue has failed.", e);
		}
	}
	
	public void setPrivateValue(Class<?> classToPerformOn, Object instanceToPerformOn, int zeroOffsets, Object newValue) throws PrivateAccessException {
		try {
			Field field = classToPerformOn.getDeclaredFields()[zeroOffsets];
			giveMeAccess(field);
			field.set(instanceToPerformOn, newValue);
			
		} catch (ReflectiveOperationException e) {
			throw new PrivateAccessException("setPrivateValue has failed.", e);
		}
	}
	
	public Object getPrivateValueViaName(Class<?> classToPerformOn, Object instanceToPerformOn, String obf) throws PrivateAccessException {
		try {
			Field field = classToPerformOn.getDeclaredField(obf);
			field.setAccessible(true);
			return field.get(instanceToPerformOn);
			
		} catch (ReflectiveOperationException e) {
			throw new PrivateAccessException("getPrivateValue has failed.", e);
		}
		
	}
	
	public void setPrivateValueViaName(Class<?> classToPerformOn, Object instanceToPerformOn, String obf, Object newValue) throws PrivateAccessException {
		try {
			Field field = classToPerformOn.getDeclaredField(obf);
			giveMeAccess(field);
			field.set(instanceToPerformOn, newValue);
		} catch (ReflectiveOperationException e) {
			throw new PrivateAccessException("setPrivateValue has failed.", e);
		}
	}
	
	private void giveMeAccess(Field field) throws ReflectiveOperationException {
		field.setAccessible(true);
		int j = fieldMod.getInt(field);
		
		if ((j & 0x10) != 0) {
			fieldMod.setInt(field, j & 0xffffffef);
		}
	}
}
