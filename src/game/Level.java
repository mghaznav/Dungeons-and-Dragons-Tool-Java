/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*        Level.java         *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import dnd.models.Treasure;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import monsters.Monster;
import monsters.DBConnection;
import java.io.Serializable;

/**
* This class is responsible for generating a whole level.
*/
public class Level implements Serializable {

    /** An ArrayList to hold all the chambers. */
    private ArrayList<Chamber> allChambers;
    /** An ArrayList to hold all the Passages. */
    private ArrayList<Passage> allPassages;
    /** A map of all the doors and their target chambers. */
    private HashMap<Door, ArrayList<Chamber>> doorMap;
    /** An Arraylist of current targets. */
    private ArrayList<Chamber> currTargets;
    /** The current DataFactory. */
    private DataFactory print;
    /** The list of all the monsters. */
    private ArrayList<Monster> allMonsters;

    /**
    * Constructor for Level.
    */
    public Level() {
        this.allChambers = new ArrayList<Chamber>();
        this.allPassages = new ArrayList<Passage>();
        this.allMonsters = new ArrayList<Monster>();
        this.doorMap = new HashMap<Door, ArrayList<Chamber>>();
        getMonsters();
    }

    /**
    * This method gets all teh monsters from the database and stores them in allMonsters.
    */
    private void getMonsters() {
        DBConnection c = new DBConnection();
        this.allMonsters = c.getAllMonsters();
    }

    /**
    * This method gets a random omnster from all the monsters.
    * @return - A random Monster.
    */
    public Monster getRandMonster() {

        Random randNum = new Random();
        int index = randNum.nextInt(this.allMonsters.size());
        return this.allMonsters.get(index);

    }

    /**
    * This method gets the list of all the monsters.
    * @return - The List of all the Monsters.
    */
    public ArrayList<Monster> getAllMonsters() {
        return this.allMonsters;
    }

    /**
    * This method calls all the different methods to generate a
    * complete level and then print it out.
    */
    public void generateLevel() {
        generateChambers();
        joinChambers();
        this.print = new DataFactory(this);
        this.print.generateLevel();
    }

    /**
    * This method adds treasure to a chamber.
    * @param theTreasure - The treasure to add.
    * @param index - The index of the chamber to add the treasure to.
    */
    public void addTreasureInChamber(Treasure theTreasure, int index) {
        this.allChambers.get(index).addTreasure(theTreasure);
    }

    /**
    * This method adds treasure to a passage.
    * @param theTreasure - The treasure to add.
    * @param index - The index of the passage to add the treasure to.
    */
    public void addTreasureInPassage(Treasure theTreasure, int index) {
        this.allPassages.get(index).addTreasure(theTreasure);
    }

    /**
    * This method adds monster to a chamber.
    * @param theMonster - The monster to add.
    * @param index - The index of the chamber to add the monster to.
    */
    public void addMonsterInChamber(Monster theMonster, int index) {
        this.allChambers.get(index).addMonster(theMonster);
    }

    /**
    * This method deletes a monster from a chamber.
    * @param theMonster - The monster to delete.
    * @param index - The index of the chamber to remove the monster from.
    */
    public void deleteMonsterFromChamber(Monster theMonster, int index) {
        this.allChambers.get(index).removeMonster(theMonster);
    }

    /**
    * This method deletes a treasure from a chamber.
    * @param theTreasure - The treasure to delete.
    * @param index - The index of the chamber to remove the treasure from.
    */
    public void deleteTreasureFromChamber(Treasure theTreasure, int index) {
        this.allChambers.get(index).removeTreasure(theTreasure);
    }

    /**
    * This method deletes a treasure from a passage.
    * @param theTreasure - The treasure to delete.
    * @param index - The index of the passage to remove the treasure from.
    */
    public void deleteTreasureFromPassage(Treasure theTreasure, int index) {
        this.allPassages.get(index).removeTreasure(theTreasure);
    }

    /**
    * This method adds a monster to a passage.
    * @param theMonster - The monster to add.
    * @param index - The index of the passage to add the monster to.
    */
    public void addMonsterInPassage(Monster theMonster, int index) {
        Passage currPassage = this.allPassages.get(index);
        Random randNum = new Random();
        int sectionNum = randNum.nextInt(currPassage.getSections().size());
        currPassage.addMonsterToSection(sectionNum, theMonster);

    }

    /**
    * This method deletes a monster from a passage.
    * @param theMonster - The monster to delete.
    * @param index - The index of the passage to remove the monster from.
    */
    public void deleteMonsterFromPassage(Monster theMonster, int index) {
        this.allPassages.get(index).deleteMonster(theMonster);
    }

    /**
    * This method gets the current data factory.
    * @return - The current DataFactory.
    */
    public DataFactory getDataFactory() {
        return this.print;
    }

    /**
    * This method gets the hashmap that maps doors to their targer chambers.
    * @return A HashMap of doors and arraylists of chambers.
    */
    public HashMap<Door, ArrayList<Chamber>> getDoorMap() {
        return this.doorMap;
    }

    /**
    * This method gets a list of all the chambers on the floor.
    * @return and ArrayList of chambers.
    */
    public ArrayList<Chamber> getChambers() {
        return this.allChambers;
    }

    /**
    * This method gets a list of all the passages on the floor.
    * @return an ArrayList of passages.
    */
    public ArrayList<Passage> getPassages() {
        return this.allPassages;
    }

    /**
    * This method generates five chambers and adds them to list of chambers.
    */
    private void generateChambers() {
        for (int i = 0; i < 5; i++) {
            Chamber theChamber = new Chamber(this);
            addChambers(theChamber);
        }
        setChambers();
    }

    /**
    * This method sets the attributes of the chambers.
    */
    private void setChambers() {
        this.allChambers.forEach((chamber) -> setDoors(chamber));
    }

    /**
    * This method sets the attributes of each door in the chamber passed to it.
    * @param theChamber - The chamber whos doors need to be set.
    */
    private void setDoors(Chamber theChamber) {
        this.currTargets = new ArrayList<Chamber>();

        for (int i = 0; i < theChamber.getDoors().size(); i++) {
            Door currDoor = theChamber.getDoors().get(i);
            setTargets(currDoor, (i + 1));
        }
    }

    /**
    * This method sets the target chambers on random for each door passed to it.
    * @param theDoor - The door to set the targets of.
    * @param doorNum - The door number in the chamber.
    */
    private void setTargets(Door theDoor, int doorNum) {

        Chamber theChamber = (Chamber) theDoor.getSpace(0);
        Chamber targetChamber = randChamber(theChamber, doorNum);
        this.currTargets.add(targetChamber);

        theDoor.setTargets(targetChamber);
        doorMap.put(theDoor, theDoor.getTargets());
    }

    /**
    * This method returns a random chamber which is different from
    * the chambers already selected of current chamber.
    * @param theChamber - The chamber which shouldnt be returned.
    * @param doorNum - The door number in the chamber.
    * @return a random Chamber
    */
    private Chamber randChamber(Chamber theChamber, int doorNum) {
        Chamber currChamber = null;
        boolean found = false;

        while (!found) {
            currChamber = this.allChambers.get(die5());
            if ((theChamber != currChamber && !this.currTargets.contains(currChamber)) || (doorNum > 4 && theChamber != currChamber)) {
                found = true;
            }
        }
        return currChamber;
    }

    /**
    * This method joins all the chambers using passages.
    */
    private void joinChambers() {

        this.doorMap.forEach((door, chambers) -> generatePassages(door, chambers));

    }

    /**
    * This methdo generates a passage for a Door -> target relationship.
    * @param theDoor - The door the passage needs to start from.
    * @param targets - The List of chambers the passages need to lead to.
    */
    private void generatePassages(Door theDoor, ArrayList<Chamber> targets) {

        targets.forEach((chamber) -> {
            Passage thePassage = new Passage();
            addPassage(thePassage);
            theDoor.addSpace(thePassage);
            theDoor.setTargetMap(chamber, thePassage);
            Door targetDoor = randDoor(chamber);
            targetDoor.addSpace(thePassage);
        });

    }

    private Door randDoor(Chamber theChamber) {
        Random rand = new Random();

        int numDoor = theChamber.getDoors().size();
        int doorChosen = rand.nextInt(numDoor);

        return theChamber.getDoors().get(doorChosen);
    }

    /**
    * This method adds a passage to the list of all the passages.
    * @param thePassage - The Passage to add.
    */
    private void addPassage(Passage thePassage) {
        this.allPassages.add(thePassage);
    }

    /**
    * This method gets an integer between 0 - 4 on random.
    * @return An int between 0 - 4.
    */
    private int die5() {

        Random die5 = new Random();

        int num = die5.nextInt(5);

        return num;
    }

    /**
    * This method adds the chamber passed to it, to the list of all the chambers.
    * @param theChamber - The Chamber to be added to all Chambers.
    */
    private void addChambers(Chamber theChamber) {
        this.allChambers.add(theChamber);
    }


    /**
    * This method gets all the monsters in a chamber.
    * @param index - The index of the chamber to get the monsters from.
    * @return - The list of all the Monsters.
    */
    public ArrayList<Monster> getChamberMonsters(int index) {
        return this.allChambers.get(index).getMonsters();
    }

    /**
    * This method gets all the treasures in a chamber.
    * @param index - The index of the chamber to get the treasures from.
    * @return - The list of all the treasures.
    */
    public ArrayList<Treasure> getChamberTreasures(int index) {
        return this.allChambers.get(index).getTreasure();
    }

    /**
    * This method gets all the monsters in a passage.
    * @param index - The index of the passage to get the monsters from.
    * @return - The list of all the Monsters.
    */
    public ArrayList<Monster> getPassageMonsters(int index) {
        return this.allPassages.get(index).getMonsters();
    }

    /**
    * This method gets all the treasures in a passage.
    * @param index - The index of the passage to get the treasures from.
    * @return - The list of all the treasures.
    */
    public ArrayList<Treasure> getPassageTreasures(int index) {
        return this.allPassages.get(index).getTreasures();
    }

}
