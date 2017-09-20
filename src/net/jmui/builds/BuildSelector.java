package net.jmui.builds;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import net.jmui.JumiBot;

public class BuildSelector {
	private DefaultBuild currentBuild;
	private Player self;
	private JumiBot bot;
	
	//constructor
	public BuildSelector(Player self, JumiBot bot) {
		this.self = self;
		this.bot = bot;
	}
	
	
	
	public void pickBuild() {
		
		//leave this as double nine gate for now
		currentBuild = new NineNineGate(self, bot);
		assert(currentBuild instanceof NineNineGate);
	}
	
	
	public void doBuild(Unit myUnit) {
		currentBuild.setUnit(myUnit);
		currentBuild.continueBuild();
	}

	
	
}

