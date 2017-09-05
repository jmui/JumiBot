package net.jmui.builds;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class BuildSelector {
	private DefaultBuild currentBuild;
	
	//constructor
	public BuildSelector() {

	}
	
	
	
	public void pickBuild() {
		
		//leave this as double nine gate for now
		currentBuild = new NineNineGate();
		assert(currentBuild instanceof NineNineGate);
	}
	
	
	public void doBuild(Unit myUnit) {
		currentBuild.getUnit(myUnit);
	}

	
	
}

