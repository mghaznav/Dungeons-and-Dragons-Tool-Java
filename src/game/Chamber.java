/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*       Chamber.java        *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import dnd.die.Die;
import dnd.exceptions.UnusualShapeException;
import dnd.models.ChamberContents;
import dnd.models.ChamberShape;
import dnd.models.Exit;
import dnd.models.Treasure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import monsters.Monster;

/**
* This class is an abstract of space and represents a chamber.
*/
public class Chamber extends Space {

    /** Holds the description of the contents of the Chamber. */
    private ChamberContents myContents;
    /** Holds the description of the shape of the Chamber. */
    private ChamberShape mySize;
    /** A list of monsters in the chamber. */
    private ArrayList<Monster> monsters;
    /** A list of treasures in the chamber. */
    private ArrayList<Treasure> treasures;
    /** A list of doors in the chamber. */
    private ArrayList<Door> doors;
    /** A list of exits in the chamber. */
    private ArrayList<Exit> exits;
    /** HashMap that holds the Exits associated with the doors. */
    private HashMap<Exit, Door> doorMap;
    /** Holds an id number for chamber on a level. */
    private int idNumber;
    /** The Current Level. */
    private Level level;

    /**
    * This is the default constructor for chamber.
    * @param theLevel - The Current level.
    */
    public Chamber(Level theLevel) {

        this.doorMap = new HashMap<Exit, Door>();
        setContents();
        setShape();
        setLevel(theLevel);
        initArrays();
        generateContents();
        setIdNumber(0);

    }

    /**
    * This method sets the level attribute of the chamber.
    * @param currLevel - The level to set.
    */
    private void setLevel(Level currLevel) {

        this.level = currLevel;

    }

    /**
    * This method sets the shape of the chamber.
    */
    private void setShape() {

        this.mySize = ChamberShape.selectChamberShape(Die.d20());

    }

    /**
    * This method sets the contents of the chamber.
    */
    private void setContents() {

        this.myContents = new ChamberContents();
        this.myContents.chooseContents(Die.d20());

    }

    /**
    * This method sets the Id number of the chamber so that
    * different chamber on a floor can be distinguished.
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
    * This method gets the list of all the doors connected to the chamber.
    * @return an ArrayList of all the doors connected to the chamber.
    */
    public ArrayList<Door> getDoors() {

        return this.doors;
    }

    /**
    * This method creates a new Monster and adds it to the chamber.
    */
    private void addMonster() {

        Monster theMonster = this.level.getRandMonster();
        this.monsters.add(theMonster);

    }

    /**
    * This method adds a monster passed to it, to the chamber.
    * @param monster - The Monster to add.
    */
    public void addMonster(Monster monster) {
        this.monsters.add(monster);
    }

    /**
    * This method removes the monster passed to it, from the chamber.
    * @param theMonster - The Monster to remove.
    */
    public void removeMonster(Monster theMonster) {
        Monster currMonster;

        for (int i = 0; i < monsters.size(); i++) {
            currMonster = this.monsters.get(i);
            if (theMonster.getName().equals(currMonster.getName())) {
                this.monsters.remove(currMonster);
                break;
            }
        }
    }

    /**
    * This method removes the treasure passed to it, from the chamber.
    * @param theTreasure - The Treasure to remove.
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
    * This method gets the list of all the monsters in the chamber.
    * @return An ArrayList of monsters in the chamber.
    */
    public ArrayList<Monster> getMonsters() {

        return this.monsters;

    }

    /**
    * This method creates a new instance of a treasure, chooses
    * its type on random and adds it to the Chamber.
    */
    private void addTreasure() {

        Treasure theTreasure = new Treasure();
        theTreasure.chooseTreasure(die100());
        this.treasures.add(theTreasure);

    }

    /**
    * This method ass a treasure to the chamber.
    * @param theTreasure - The Treasure to be added.
    */
    public void addTreasure(Treasure theTreasure) {

        this.treasures.add(theTreasure);

    }

    /**
    * This method gets the list of all the treasures in the chamber.
    * @return An ArrayList of treasures in the chamber.
    */
    public ArrayList<Treasure> getTreasure() {

        return this.treasures;
    }

    /**
    * This method is inherited from Space and is overriden
    * to get the description of the chamber.
    * @return The String description of the chammber.
    */
    @Override
    public String getDescription() {

        String desc;

        desc = "===================================\r\n" + "============ Chamber " + getIdNumber() + " ============\r\n" + "===================================\r\n";
        desc = getShapeDescription(desc);
        desc = desc.concat(getContentDescription());
        desc = getExitDescription(desc);

        return desc;

    }

    /**
    * This method is inherited from space and is overriden to make sure
    * the space on the door is set and the door is then added to the chamber.
    * @param newDoor - The Door to be added to the chamber.
    */
    @Override
    public void setDoor(Door newDoor) {

        if (newDoor.getSpaces().size() == 0) {
            newDoor.addSpace(this);
        } else {
            doors.add(newDoor);
        }

    }

    /**
    * This method generates the contents of the chamber. Including
    * Monsters, Treasure, Exits and Doors.
    */
    private void generateContents() {

        String desc = this.myContents.getDescription();

        if (desc.compareTo("monster only") == 0) {
            addMonster();
        } else if (desc.compareTo("monster and treasure") == 0) {
            addMonster();
            addTreasure();
        } else if (desc.compareTo("treasure") == 0) {
            addTreasure();
        }

        createExits();

    }

    /**
    * This method gets the description of the shape of the chamber.
    * @param description - A string to concat the description to.
    * @return A String description of the shape
    */
    private String getShapeDescription(String description) {

        try {
            description = getUsualShapeDescription(description);
        } catch (UnusualShapeException e) {
            description = getUnusualShapeDescription(description);
        }

        return description;

    }

    /**
    * This method gets the description of the shape of the chamber,
    * if it is a usual shape and has a length and a width.
    * @param description - A string to concat the description to.
    * @return A String description of the shape
    */
    private String getUsualShapeDescription(String description) {

        String shape = this.mySize.getShape();
        int length = this.mySize.getLength();
        int width = this.mySize.getWidth();

        description = description.concat("Description: " + shape + ", " + length + "' x " + width + "'" + "\r\n");

        return description;

    }

    /**
    * This method gets the description of the shape of the chamber,
    * if it is an unusual shape and has an area.
    * @param description - A string to concat the description to.
    * @return A String description of the shape
    */
    private String getUnusualShapeDescription(String description) {
        String shape = this.mySize.getShape();
        int area = this.mySize.getArea();

        description = description.concat("Description: " + shape + ", about " + area + " sq. ft" + "\r\n");

        return description;
    }

    /**
    * This method gets the description of the contents in the chamber.
    * @return A String description of the contents.
    */
    private String getContentDescription() {

        String desc = "";

        desc = getMonsterDescription(desc);
        desc = getTreasureDescription(desc);

        return desc;
    }

    /**
    * This method gets the description of the monster in the chamber.
    * @param description - A string to concat the description to.
    * @return A String description of the monster
    */
    private String getMonsterDescription(String description) {

        for (int i = 0; i < this.monsters.size(); i++) {
             description = description.concat("Monster Name: " + this.monsters.get(i).getName() + "\r\nMonster Description: " + this.monsters.get(i).getDescription() + "\r\n");
        }

        return description;
    }

    /**
    * This method gets the description of the treasure in the chamber.
    * @param description - A string to concat the description to.
    * @return A String description of the treasure
    */
    private String getTreasureDescription(String description) {

        for (int i = 0; i < this.treasures.size(); i++) {
            Treasure currTreasure = this.treasures.get(i);
            description = description.concat("Treasure: " + currTreasure.getDescription() + "\r\n");
        }

        return description;
    }

    /**
    * This method gets the description of the exits from the chamber.
    * @param description - A string to concat the description to.
    * @return A String description of the exits.
    */
    private String getExitDescription(String description) {

        int numExits = getNumExits();

        for (int i = 0; i < numExits; i++) {
            description = description.concat("Exit " + (i + 1) + ": " + this.exits.get(i).getDirection() + ", " + this.exits.get(i).getLocation() + "\r\n");
        }

        return description;

    }

    /**
    * This method creats the exits from the chamber.
    */
    private void createExits() {

        for (int i = 0; i < getNumExits(); i++) {
            Exit exit = new Exit();
            this.exits.add(exit);
        }

        setExits();

    }

    /**
    * This method creates a door (an exit from the chamber)
    * and attaches the door to this chamber.
    */
    private void setExits() {

        for (int i = 0; i < this.exits.size(); i++) {
            Exit theExit = this.exits.get(i);
            Door door = new Door(theExit);
            door.setIdNumber(i + 1);
            door.addSpace(this);
            this.doorMap.put(theExit, door);
        }

    }

    /**
    * This function gets the number of exits from the Chamber.
    * @return An int representing the number of exits.
    */
    public int getNumExits() {

        int numExits;

        numExits = this.mySize.getNumExits();

        return numExits;
    }

    /**
    * This method provides a die used in generating random content in other methods.
    * @return A random integer between 1-100.
    */
    private int die100() {

        Random die100 = new Random();

        int roll = die100.nextInt(101 - 1) + 1;

        return roll;
    }

    /**
    * This method initialises all the ArrayLists used in the Chamber class.
    */
    private void initArrays() {

        this.doors = new ArrayList<Door>();
        this.exits = new ArrayList<Exit>();
        this.monsters = new ArrayList<Monster>();
        this.treasures = new ArrayList<Treasure>();

    }

}
