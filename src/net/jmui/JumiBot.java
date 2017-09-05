
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
        build = new BuildSelector();
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

            //if there's enough minerals, train a probe
            if ((myUnit.getType() == UnitType.Protoss_Nexus) && (self.minerals() >= 50)) {
                myUnit.train(UnitType.Protoss_Probe);
            }
            
            
            //select a larva to morph a unit
            /*if((myUnit.getType() == UnitType.Zerg_Larva) ) {

            	if((self.supplyTotal() - self.supplyUsed() <= 3) && (self.minerals() >= 100)) {
            		
            		myUnit.morph(UnitType.Zerg_Overlord);    	
            		
            	}
            	
            	if(self.minerals() >= 50) {
            		myUnit.morph(UnitType.Zerg_Drone);
            	}
            	
            	
            }*/

            
            
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
    
    
}

