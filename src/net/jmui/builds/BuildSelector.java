package net.jmui.builds;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import net.jmui.JumiBot;

public class BuildSelector {
	private DefaultBuild currentBuild;
	private Player self;
	private JumiBot bot;
	private Game game;
	
	//constructor
	public BuildSelector(Player self, JumiBot bot, Game game) {
		this.self = self;
		this.bot = bot;
		this.game = game;
	}
	
	
	
	public void pickBuild() {
		
		//leave this as double nine gate for now
		currentBuild = new NineNineGate(self, bot, game);
		assert(currentBuild instanceof NineNineGate);
	}
	
	
	public void doBuild(Unit myUnit) {
		currentBuild.setUnit(myUnit);	//currently selected unit
		currentBuild.continueBuild();
	}

	
	
}

