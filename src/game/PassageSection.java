/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*    PassageSection.java    *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import java.util.ArrayList;
import java.io.Serializable;
import monsters.Monster;
/**
* This class represents a 10 ft section of passageway.
*/
public class PassageSection implements Serializable {

    /** Door in the section. */
    private Door door;
    /** The passage holding the section. */
    private Passage thePassage;
    /** Boolean to represent if the section has a door. */
    private boolean hasDoor;
    /** String to hold the description of the section. */
    private String desc;
    /** A list of all the monsters in the PassageSection. */
    private ArrayList<Monster> monsters;

    /**
    * This is the constructor for PassageSection.
    * @param passage - The Passage the section belongs to.
    */
    public PassageSection(Passage passage) {
        setPassage(passage);
        this.hasDoor = false;
        monsters = new ArrayList<Monster>();
    }

    /**
    * This methods sets thePassage attribut of the class.
    * @param passage - The Passage the section belongs to.
    */
    private void setPassage(Passage passage) {
        this.thePassage = passage;
    }

    /**
    * This method returns the door that is in the passage section, if there is one.
    * @return The Door in the passage section.
    */
    public Door getDoor() {

        if (hasDoor) {
            return this.door;
        } else {
            return null;
        }
    }

    /**
    * This method gets the String description of the PassageSection.
    * @return A String with the description of the passage section.
    * @param index - The index to set the description.
    */
    public String getDescription(int index) {
        setDescription(index + 1);
        return this.desc;
    }

    /**
    * This method adds a Door to the passage section.
    * @param theDoor - The Door to add.
    */
    protected void addDoor(Door theDoor) {
        this.door = theDoor;
        this.hasDoor = true;
    }

    /**
    * This method generates a new door and adds it to the passage section.
    */
    private void addDoor() {
        Door theDoor = new Door();
        this.door = theDoor;
        this.hasDoor = true;
    }

    /**
    * This method adds a monster to the section.
    * @param theMonster - The monster to add.
    */
    public void addMonster(Monster theMonster) {
        this.thePassage.addMonsters(theMonster);
        this.monsters.add(theMonster);
    }

    /**
    * This method tells if the section has a door or not.
    * @return A boolean value.
    */
    public boolean hasDoor() {
        return this.hasDoor;
    }

    /**
    * This method removes a monster from tge section.
    * @param theMonster - The Monster to remove.
    * @return - A boolean value representing if the delete was successful.
    */
    public boolean removeMonster(Monster theMonster) {
        Monster currMonster;

        for (int i = 0; i < monsters.size(); i++) {
            currMonster = this.monsters.get(i);
            if (theMonster.getName().equals(currMonster.getName())) {
                this.monsters.remove(currMonster);
                return true;
            }
        }

        return false;
    }

    /**
    * This method sets the description of the passage section based on the section number.
    * @param sectionNumber - An integer representing the section number in the current passage.
    */
    public void setDescription(int sectionNumber) {

        this.desc = "";

        switch (sectionNumber) {
            case 1:
                this.desc = "Passage goes straight for 10 ft";
                break;

            case 2:
                this.desc = "Passage ends in Door to a Chamber";
                break;

            default:
                break;
        }

        getMonsterDescription();

    }

    /**
    * This method gets the description of the monsters in the passage section.
    */
    private void getMonsterDescription() {

        for (int i = 0; i < this.monsters.size(); i++) {
            this.desc = this.desc.concat("\r\nMonster Name: " + this.monsters.get(i).getName() + "\r\nMonster Description: " + this.monsters.get(i).getDescription());
        }
    }

}
