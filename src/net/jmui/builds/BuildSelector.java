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
		
		//leave this as four pool for now as a first build
		
		currentBuild = new FourPool();
		//currentBuild = (FourPool)currentBuild;
		assert(currentBuild instanceof FourPool);
		
	}
	
	
	public void doBuild(Unit myUnit) {
		
	}

	
	
}

