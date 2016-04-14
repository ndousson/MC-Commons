package eu.ha3.mc.haddon.implem;

public interface Instantiator<E> {
	/**
	 * Creates a new instance of an underlying class. Returns null in all cases if it fails.
	 */
	public E instantiate(Object... pars);
}
