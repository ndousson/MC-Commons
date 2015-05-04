package eu.ha3.mc.gui;

public interface HSliderListener {
	public void sliderValueChanged(HGuiSliderControl slider, float value);
	
	public void sliderPressed(HGuiSliderControl hGuiSliderControl);
	
	public void sliderReleased(HGuiSliderControl hGuiSliderControl);
}
