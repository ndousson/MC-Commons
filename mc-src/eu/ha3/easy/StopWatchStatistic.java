package eu.ha3.easy;

public class StopWatchStatistic extends TimeStatistic {
	private long stopTime;
	
	@Override
	public long getMilliseconds() {
		return stopTime - startTime;
	}
	
	public void reset() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		stopTime = System.currentTimeMillis();
	}
}
