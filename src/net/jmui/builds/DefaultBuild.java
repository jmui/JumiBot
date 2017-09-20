package net.jmui.builds;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public abstract class DefaultBuild {
	
	
	//set the current unit or building being selected 
	public abstract void setUnit(Unit current);
	
	//continue with the build order
	public abstract void continueBuild();

}
