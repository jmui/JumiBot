package net.jmui.builds;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import net.jmui.JumiBot;

/**************
8 - Pylon
9 - Gateway
9 - Gateway  -from scouting probe
9 - Probe    -can be used to scout on 4 base maps
10 - Probe
11 - Zealot
13 - Pylon
13 - Zealot
15 - Zealot
**************/

public class NineNineGate extends DefaultBuild{
	private Unit myUnit;
	private Player self;
	private JumiBot bot;
	private TilePosition tilePos;
	private TilePosition myStartLoc;
	//private TilePosition myNexusPos;
	private TilePosition firstPylonPos;
	private int probeCount;
	private int gateCount;
	private int zealotCount;
	private boolean firstPylon;
	private boolean nextGate;
	private boolean buildingPylon;
	private boolean buildingGate;
	private boolean finishedGates;
	private boolean pylonStarted;
	
	public NineNineGate(Player self, JumiBot bot) {
		this.self = self;
		this.bot = bot;
		probeCount = 4;
		gateCount = 0;
		zealotCount = 0;
		firstPylon = false;
		nextGate = false;
		buildingPylon = false;
		buildingGate = false;
		firstPylonPos = null;
		finishedGates = false;
		pylonStarted = false;
		myStartLoc = self.getStartLocation();
	}
	
	
	
	@Override
	public void setUnit(Unit current) {
		myUnit = current;
	}
	
	
	
	@Override
	public void continueBuild() {
		
		
		if((myUnit.getType() == UnitType.Protoss_Nexus) && (self.minerals() >= 50) && (probeCount < 8) && (!myUnit.isTraining())) {
			//myNexusPos = myUnit.getInitialTilePosition();
			myUnit.train(UnitType.Protoss_Probe);
			probeCount++;
			
			
			//first pylon at 8
		} else if ((probeCount == 8) && (myUnit.getType() == UnitType.Protoss_Probe) &&  (!pylonStarted)) {
			
			tilePos =  bot.getBuildTile(myUnit, UnitType.Protoss_Pylon, myStartLoc);
			
			if(tilePos != null && !buildingPylon && self.minerals() >= 100) {
				System.out.println("Building first pylon");
				myUnit.build(UnitType.Protoss_Pylon, tilePos);
				firstPylonPos = tilePos;
				buildingPylon = true;
				//firstPylon = true;
			} else if(tilePos == null){
				System.out.println("tilePos is null for pylon");
			}
			
			if(buildingPylon) {
				for(Unit currUnit : self.getUnits()) {
					if(currUnit.getType() == UnitType.Protoss_Pylon) {
						pylonStarted = true;
					}
				}
			} 

			
		} else if (/*(!firstPylon) && (buildingPylon) && (firstPylonPos != null)*/(pylonStarted) && (myUnit.getType() == UnitType.Protoss_Nexus) && (self.minerals() >= 50) && (probeCount <= 9) && (!myUnit.isTraining())) {		
			
			if(probeCount < 9) {
				myUnit.train(UnitType.Protoss_Probe);
				probeCount++;
			}
			
			if(buildingPylon) {
				for(Unit currUnit : self.getUnits()) {
					if(currUnit.getType() == UnitType.Protoss_Pylon) {
						if(currUnit.isCompleted()) {
							firstPylon = true;
							buildingPylon = false;
							break;
						}
					}
				}
			}
			
		} else if ((probeCount == 9) && (myUnit.getType() == UnitType.Protoss_Probe) && (gateCount < 1) &&  (firstPylon)){
			
			tilePos = bot.getBuildTile(myUnit,  UnitType.Protoss_Gateway, firstPylonPos);
			if((tilePos != null) && (!buildingGate) && self.minerals() >= 150) {
				System.out.println("***************** " + tilePos.toString());
				myUnit.build(UnitType.Protoss_Gateway, tilePos);
				buildingGate = true;
				//nextGate = true;
				//gateCount++;
			} else {
				//System.out.println("tilePos is null");
			}
			
			if(buildingGate) {
				for(Unit currUnit : self.getUnits()) {
					if(currUnit.getType() == UnitType.Protoss_Gateway) {
						nextGate = true;
						gateCount++;
						break;
					}
				}
			}
			
			
		} else if ((gateCount <= 2) && (myUnit.getType() == UnitType.Protoss_Probe) && (nextGate) && (probeCount == 9) && (!finishedGates)) {
			int tempGateCount = 0;
			
			if((self.minerals() >= 150) && (gateCount == 1)) {
				tilePos = bot.getBuildTile(myUnit,  UnitType.Protoss_Gateway, firstPylonPos);
				if(tilePos != null) {
					System.out.println("***************** " + tilePos.toString());
					myUnit.build(UnitType.Protoss_Gateway, tilePos);
					//nextGate = false;
					//finishedGates = true;
					gateCount++;
				} else {
					//System.out.println("tilePos is null");
				}
			}
			
			if(gateCount == 2) {
				for(Unit currUnit : self.getUnits()) {
					if(currUnit.getType() == UnitType.Protoss_Gateway) {
						tempGateCount++;
						if(tempGateCount == 2) {
							break;
						}
					}
				}
				
				if(tempGateCount == 2) {
					finishedGates = true;
					nextGate = false;
					//System.out.println("temp gate count 2");
				}
			}
			System.out.println(finishedGates + " gate count " + gateCount + " probe count " + probeCount);
		} else if((finishedGates) && (probeCount <= 11) && (myUnit.getType() == UnitType.Protoss_Nexus)){
			
			System.out.println(finishedGates + " ^^^^^^^^^^^^^^ probe count " + probeCount + " unit type " + myUnit.getType().toString() + " minerals " + self.minerals() );
			if(self.minerals() >= 50) {
				//System.out.println("PROBE");
				myUnit.train(UnitType.Protoss_Probe);

				probeCount++;
			}
			
			 
		} /*else if((probeCount == 11) && (finishedGates) && (gateCount == 2)) {
			System.out.println("&&&&&&&&&&&");
			buildingPylon = false;
			
			for(Unit currUnit : self.getUnits()) {
				if(currUnit.getType() == UnitType.Protoss_Pylon) {
					if(!currUnit.isCompleted()) {
						buildingPylon = true;
						break;
					}
				}
			}
			
			
			if(myUnit.getType() == UnitType.Protoss_Gateway) {
				if((bot.getFreeSupply() > 2) && (self.minerals() > 100) && (!myUnit.isTraining())) {
					myUnit.train(UnitType.Protoss_Zealot);
				}
			}
			
			if(myUnit.getType() == UnitType.Protoss_Probe) {
				if((!buildingPylon) && (bot.getFreeSupply() <= 2)) {
					tilePos =  bot.getBuildTile(myUnit, UnitType.Protoss_Pylon, myStartLoc);
					if(tilePos != null && !buildingPylon) {
						myUnit.build(UnitType.Protoss_Pylon, tilePos);
						//firstPylonPos = tilePos;
						//buildingPylon = true;
						//firstPylon = true;
					} else if(tilePos == null){
						System.out.println("tilePos is null for pylon");
					}
				}
			}
			
			
			//iterate through all units.  if enough zealots then attack
		} */
		
		
		/*else if((gateCount == 2) && (probeCount >= 11) && (firstPylon)) {
			if(myUnit.getType() == UnitType.Protoss_Gateway) {
				if((myUnit.isCompleted()) && (self.minerals() >= 100) && (!myUnit.isTraining()) && (bot.getFreeSupply() > 2)) {
					myUnit.train(UnitType.Protoss_Zealot);
					zealotCount++;
				}
				
			} else if(myUnit.getType() == UnitType.Protoss_Probe) {
				if((bot.getFreeSupply() < 4) && self.minerals() > 100) {
					
				}
			}
		}*/
		
		/*else if ((twoGates) && (firstPylon) && (probeCount < 11) && (myUnit.getType() == UnitType.Protoss_Nexus) && (self.minerals() >= 50) && (!myUnit.isTraining()) ) {
			myUnit.train(UnitType.Protoss_Probe);
			probeCount++;
		}*/
		

		
	}

}
