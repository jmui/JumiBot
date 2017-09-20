
package net.jmui;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import net.jmui.builds.BuildSelector;
//import java.util.*;


public class JumiBot extends DefaultBWListener {

    private Mirror mirror = new Mirror();
    private Game game;
    private Player self;
    //private Player opponent;
    private static JumiBot bot; //singleton bot object
    //private boolean foundEnemyBase;
    private BuildSelector build;
    //private ArrayList<BaseLocation> baseList;
 
    
    
    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }
    
    
    //probably won't need later
    @Override
    public void onUnitCreate(Unit unit) {
        //System.out.println("New unit discovered " + unit.getType());
        printMessage("New unit discovered " + unit.getType());
    }

    
    
    
    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();
        build = new BuildSelector(self, this);
        //foundEnemyBase = false;
        build.pickBuild();
        //currentStrat = new Test4Pool();
        //this.baseList = new ArrayList<BaseLocation>();
        
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }

    }

    
    
    
    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");
 
        //iterate through my units
        for (Unit myUnit : self.getUnits()) {
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");

            build.doBuild(myUnit);

            
            
            //if it's a worker and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                Unit closestMineral = null;

                //find closest mineral patch
                for (Unit neutralUnit : game.neutral().getUnits()) {
                    if (neutralUnit.getType().isMineralField()) {
                        if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                            closestMineral = neutralUnit;
                        }
                    }
                }

                //if a mineral patch was found, send the worker to gather it
                if (closestMineral != null) {
                    myUnit.gather(closestMineral, false);
                }
                
                
            } 
            
            
        } //outer for loop for iterating through units. self.getUnits

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
        
    }

    
    //won't need later
    public void printMessage(String msg) {
    	System.out.println(msg);
    }
    
    

    
    
    public static void main(String[] args) {
    	bot = new JumiBot();
    	bot.run();
        //new JumiBot().run();
    }
    
    
    
    //get amount of free supply
    public int getFreeSupply() {
    	int supply;
    	supply = self.supplyTotal() - self.supplyUsed();
    	return supply;
    }
    
    
    
	    
	 // Returns a suitable TilePosition to build a given building type near
	 // specified TilePosition aroundTile, or null if not found. (builder parameter is our worker)
	 public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
	 	TilePosition ret = null;
	 	int maxDist = 3;
	 	int stopDist = 60;
	
	 	// Refinery, Assimilator, Extractor
	 	if (buildingType.isRefinery()) {
	 		for (Unit n : game.neutral().getUnits()) {
	 			if ((n.getType() == UnitType.Resource_Vespene_Geyser) &&
	 					( Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist ) &&
	 					( Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist )
	 					) return n.getTilePosition();
	 		}
	 	}
	
	 	while ((maxDist < stopDist) && (ret == null)) {
	 		for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
	 			for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
	 				if (game.canBuildHere(new TilePosition(i,j), buildingType, builder, false)) {
	 					// units that are blocking the tile
	 					boolean unitsInWay = false;
	 					for (Unit u : game.getAllUnits()) {
	 						if (u.getID() == builder.getID()) continue;
	 						if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
	 					}
	 					if (!unitsInWay && !buildingType.requiresPsi()) {
	 						return new TilePosition(i, j);
	 					}
	 					
						// check if area is powered by a pylon for non pylons and non assimilators
						if (buildingType.requiresPsi()) {
							boolean psiMissing = false;
							for (int k=i; k<=i+buildingType.tileWidth(); k++) {
								for (int l=j; l<=j+buildingType.tileHeight(); l++) {
									if (!game.hasPower(k, l)) psiMissing = true;
									break;
								}
							}
							if (psiMissing) continue;
							
							if(!psiMissing) {
								return new TilePosition(i, j);
							}
						}
	 				}
	 			}
	 		}
	 		maxDist += 2;
	 	}
	
	 	if (ret == null) game.printf("Unable to find suitable build position for "+buildingType.toString());

	 	return ret;
	 }
    
    
    
}

