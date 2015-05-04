package eu.ha3.easy;

import java.util.Locale;

public class TimeStatistic {
	private final Locale locale;
	
	protected long startTime;
	
	public TimeStatistic(Locale locale) {
		this.locale = locale;
		startTime = System.currentTimeMillis();
	}
	
	public TimeStatistic() {
		this(null);
	}
	
	public long getMilliseconds() {
		return System.currentTimeMillis() - startTime;
	}
	
	public String getSecondsAsString(int precision) {
		if (locale == null) {
			return String.format("%." + precision + "f", getMilliseconds() / 1000f);
		}
		return String.format(locale, "%." + precision + "f", getMilliseconds() / 1000f);
	}
	
}
