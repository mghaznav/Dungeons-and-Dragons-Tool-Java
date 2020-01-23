/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*      Passage.java         *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import dnd.models.Treasure;
import java.util.ArrayList;
import java.util.HashMap;
import monsters.Monster;

/**
* This class is an abstract of space and represents a Passage.
*/
public class Passage extends Space {

    /** ArrayList of the passage sections. */
    private ArrayList<PassageSection> thePassages;
    /** ArrayList of the doors. */
    private ArrayList<Door> doors;
    /** ArrayList of all the monsters. */
    private ArrayList<Monster> monsters;
    /** ArrayList of all the treasures. */
    private ArrayList<Treasure> treasures;
    /** Hasmap of the Doors and passage sections they belong to. */
    private HashMap<Door, PassageSection> doorMap;
    /** String to hold the description of the Passage. */
    private String description;
    /** Holds an id number for chamber on a level. */
    private int idNumber;

    /**
    * This is the Constructor for Passage.
    */
    public Passage() {

        this.thePassages = new ArrayList<PassageSection>();
        this.doors = new ArrayList<Door>();
        this.doorMap = new HashMap<Door, PassageSection>();
        this.monsters = new ArrayList<Monster>();
        this.treasures = new ArrayList<Treasure>();
        generatePassage();
        setIdNumber(0);

    }

    /**
    * This method adds a treasure to the passage.
    @param theTreasure - The treasure to be added.
    */
    public void addTreasure(Treasure theTreasure) {
        this.treasures.add(theTreasure);
    }

    /**
    * This method gets all the treasures in the passage.
    * @return - An ArrayList of all the treasures.
    */
    public ArrayList<Treasure> getTreasures() {
        return this.treasures;
    }

    /**
    * This method gets the list of doors in the passage.
    * @return An ArrayList of doors in the passage.
    */
    public ArrayList<Door> getDoors() {
        return this.doors;
    }

    /**
    * This method gets the door at a specific index.
    * @param i - The index fom where to get the door.
    * @return A Door.
    */
    public Door getDoor(int i) {

        if (this.doors.size() <= i) {
            return null;
        } else {
            return this.doors.get(i);
        }

    }

    /**
    * This method gets all the monsters in the passage.
    * @return - An ArrayList of all the monsters.
    */
    public ArrayList<Monster> getMonsters() {
        return this.monsters;
    }

    /**
    * This method sets the Id number of the passage so that
    * different passages on a floor can be distinguished.
    * @param id - An int to set as the idNumber.
    */
    public void setIdNumber(int id) {
        this.idNumber = id;
    }

    /**
    * This method gets the Id Number assigned to the chamber.
    * The id is set to 0 by default and is changed using setIdNumber().
    * @return the idNumber assigned to the chamber.
    */
    public int getIdNumber() {
        return this.idNumber;
    }

    /**
    * This method deletes a monster from the passage.
    * @param theMonster - The monster to delete.
    */
    public void deleteMonster(Monster theMonster) {
        Monster currMonster;

        for (int i = 0; i < monsters.size(); i++) {
            currMonster = this.monsters.get(i);
            if (theMonster.getName().equals(currMonster.getName())) {
                this.monsters.remove(currMonster);
                break;
            }
        }

        deleteMonsterFromSection(theMonster);
    }

    /**
    * This method deletes a treasure from the passage.
    * @param theTreasure - The monster to delete.
    */
    public void removeTreasure(Treasure theTreasure) {
        Treasure currTreasure;

        for (int i = 0; i < treasures.size(); i++) {
            currTreasure = this.treasures.get(i);
            if (theTreasure.getDescription().equals(currTreasure.getDescription())) {
                this.treasures.remove(currTreasure);
                break;
            }
        }
    }

    /**
    * This method deletes a monster from the PassageSections in the Passage.
    * @param theMonster - The monster to delete.
    */
    public void deleteMonsterFromSection(Monster theMonster) {
        for (int i = 0; i < thePassages.size(); i++) {
            PassageSection currSection = this.thePassages.get(i);
            if (currSection.removeMonster(theMonster)) {
                break;
            }
        }
    }

    /**
    * This method adds a monster to the passage.
    * @param theMonster - The monster to add.
    */
    public void addMonsters(Monster theMonster) {
        this.monsters.add(theMonster);
    }

    /**
    * This method adds a 10ft section to the passage.
    * @param toAdd - The PassageSection to be added.
    */
    private void addPassageSection(PassageSection toAdd) {

        this.thePassages.add(toAdd);
    }

    /**
    * This method adds a monster to a specific section of the passage.
    * @param index - The index of the PassageSection to add the Monster to.
    * @param theMonster - The monster to be added.
    */
    public void addMonsterToSection(int index, Monster theMonster) {
        this.thePassages.get(index).addMonster(theMonster);
    }

    /**
    * This method gets all the sections in the passage.
    * @return - An Arraylist of all the sections.
    */
    public ArrayList<PassageSection> getSections() {
        return this.thePassages;
    }

    /**
    * This method add a door to this Passage.
    * @param newDoor - The Door to be added to the Passage.
    */
    @Override
    public void setDoor(Door newDoor) {

        if (newDoor.getSpaces().size() == 0) {
            newDoor.addSpace(this);
        } else {
            this.doors.add(newDoor);
        }

    }

    /**
    * This method gets the description of the Door.
    * @return The String description of the Door.
    */
    @Override
    public String getDescription() {
        this.description = "************ Passage ************\r\n";

        for (int i = 0; i < this.thePassages.size(); i++) {
            PassageSection currSection = this.thePassages.get(i);
            getSectionDesc(currSection, i);
        }

        getTreasureDescription();

        return this.description;
    }

    /**
    * This method gets the description of the treasure in the chamber.
    */
    private void getTreasureDescription() {

        for (int i = 0; i < this.treasures.size(); i++) {
            Treasure currTreasure = this.treasures.get(i);
            this.description = this.description.concat("Treasure: " + currTreasure.getDescription() + "\r\n");
        }

    }

    /**
    * This method randomly generates all the sections in the passage.
    */
    private void generatePassage() {

        for (int i = 1; i <= 2; i++) {
            PassageSection section = new PassageSection(this);
            section.setDescription(i);
            addPassageSection(section);
        }

        setAllDoors();

    }

    /**
    * This method adds all the doors in passage sections to the passage.
    */
    private void setAllDoors() {

        for (int i = 0; i < thePassages.size(); i++) {
            PassageSection theSection = thePassages.get(i);
            if (theSection.hasDoor()) {
                Door theDoor = theSection.getDoor();
                setDoor(theDoor);
            }
        }

    }

    /**
    * This method sets the description of the passage.
    * @param theSection - The PassageSection whos description is to be used
    * @param index - The Index of the passageSection to get.
    */
    private void getSectionDesc(PassageSection theSection, int index) {

        this.description = this.description.concat(theSection.getDescription(index) + "\r\n");

    }

}
