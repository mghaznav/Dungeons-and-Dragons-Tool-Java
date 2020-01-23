/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*        Door.java          *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import dnd.die.Die;
import dnd.models.Exit;
import dnd.models.Trap;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

/**
* This class represents a door.
*/
public class Door implements Serializable {

    /** Holds the exit associated with the door. */
    private Exit exit;
    /** Holds a the trap in the door. */
    private Trap trap;
    /** Boolean representing if the door is trapped. */
    private boolean trapped;
    /** Boolean representing if the door is open. */
    private boolean open;
    /** Boolean representing if the door is an archway. */
    private boolean archway;
    /** Boolean representing if the door is locked. */
    private boolean locked;
    /** ArrayList of the spaces around the door. */
    private ArrayList<Space> spaces;
    /** The target chamber for the door. */
    private ArrayList<Chamber> targets;
    /** A map of targets and the passages to get to them. */
    private HashMap<Chamber, Passage> targetMap;
    /** Holds an id number for door relative to the chamber. */
    private int idNumber;

    /**
    * This is the defualt constructor for this class.
    */
    public Door() {
        this.exit = new Exit();
        this.spaces = new ArrayList<Space>();
        this.targets = new ArrayList<Chamber>();
        this.targetMap = new HashMap<Chamber, Passage>();
        setDoor();
    }

    /**
    * This constructor generates a door according to the Exit passed to it.
    * @param theExit - The Exit used to generate the door.
    */
    public Door(Exit theExit) {
        this.exit = theExit;
        this.spaces = new ArrayList<Space>();
        this.targets = new ArrayList<Chamber>();
        this.targetMap = new HashMap<Chamber, Passage>();
        setDoor();
    }

    /**
    * This method sets the Id number of the Door so that
    * different door in a Chamber can be distinguished.
    * @param id - An int to set as the idNumber.
    */
    public void setIdNumber(int id) {
        this.idNumber = id;
    }

    /**
    * This method gets the Id Number assigned to the Door.
    * @return the idNumber assigned to the chamber.
    */
    public int getIdNumber() {
        return this.idNumber;
    }

    /**
    * This method sets the trap attribute of the door.
    * @param flag - A boolean value telling if the trap is to be set or not.
    * @param roll - An optional int to set the type of trap.
    */
    private void setTrapped(boolean flag, int... roll) {

        this.trapped = flag;
        if (flag) {
            this.trap = new Trap();
            if (roll.length > 0) {
                this.trap.chooseTrap​(roll[0]);
            } else {
                this.trap.chooseTrap​(Die.d20());
            }
        }
    }

    /**
    * This method sets the open attribute of the door.
    * @param flag - A boolean value telling if the door is to be set open or not.
    */
    private void setOpen(boolean flag) {

        this.open = flag;

        if (isArchway() && !flag) {
            this.open = true;
        }
    }

    /**
    * This method sets the archway attribute of the door.
    * @param flag - A boolean value telling if the door is to be set an archway or not.
    */
    private void setArchway(boolean flag) {
        this.archway = flag;
        setLocked(false);
        setOpen(true);
    }

    /**
    * This method gets the list of all the target chambers of the door.
    * @return an ArrayList of chambers.
    */
    public ArrayList<Chamber> getTargets() {
        return this.targets;
    }

    /**
    * This method tells if the door is trapped or not.
    * @return A boolean value that tells if the door is trapped.
    */
    private boolean isTrapped() {
        return this.trapped;
    }

    /**
    * This method tells if the door is open or not.
    * @return A boolean value that tells if the door is open.
    */
    private boolean isOpen() {
        return this.open;
    }

    /**
    * This method tells if the door is an archway or not.
    * @return A boolean value that tells if the door is an archway.
    */
    private boolean isArchway() {
        return this.archway;
    }

    /**
    * This method adds a chamber to the list of targets of the door.
    * @param targetChamber - The chamber to add to the targets.
    */
    public void setTargets(Chamber targetChamber) {
        this.targets.add(targetChamber);
    }

    /**
    * This method adds a new key and value to the Target Map.
    * @param targetChamber - The target chamber.
    * @param thePassage - The passage to get to the target chamber.
    */
    public void setTargetMap(Chamber targetChamber, Passage thePassage) {
        this.targetMap.put(targetChamber, thePassage);
    }

    /**
    * This method gets the passage for a target chamber from the target map.
    * @param theChamber - The chamber whos passage is needed.
    * @return The Passage to get to that chamber.
    */
    public Passage getTargetPassage(Chamber theChamber) {
        return this.targetMap.get(theChamber);
    }

    /**
    * This method gets the description of the Trap set on the door.
    * @return A String with the description of the trap.
    */
    private String getTrapDescription() {

        String desc;

        desc = this.trap.getDescription();

        return desc;
    }

    /**
    * This method gets the spaces attached to the door.
    * @return An ArrayList of spaces.
    */
    public ArrayList<Space> getSpaces() {
        return this.spaces;
    }

    /**
    * This method gets a space at a particular index from the list of spaces.
    * @param i - The index of the space.
    * @return The Space at index i.
    */
    public Space getSpace(int i) {

        if (this.spaces.size() <= i) {
            return null;
        } else {
            return this.spaces.get(i);
        }

    }

    /**
    * This method adds a space to the list of spaces around the door.
    * @param theSpace - the space to add to the list.
    */
    public void addSpace(Space theSpace) {

        spaces.add(theSpace);
        theSpace.setDoor(this);

    }

    /**
    * This method sets the spaces around the door.
    * @param spaceOne - First space attached to the door.
    * @param spaceTwo - Second space attached to the door.
    */
    public void setSpaces(Space spaceOne, Space spaceTwo) {

        spaces.add(spaceOne);
        spaces.add(spaceTwo);

        spaceOne.setDoor(this);
        spaceTwo.setDoor(this);

    }

    /**
    * This method gets the decription of the door.
    * @return A String with description of the door.
    */
    public String getDescription() {

        String desc;

        desc = "********** Door **********\r\n";

        desc = desc.concat("Location: " + this.exit.getLocation() + "\r\n");
        desc = desc.concat("Direction: " + this.exit.getDirection());
        desc = getStateDescription(desc);

        return desc;
    }

    /**
    * This method gets the description of the state of the door,
    * and appends it to the string passed to it.
    * @param description - The string to append the description to.
    * @return The modified string.
    */
    private String getStateDescription(String description) {

        description = isArchway() ? description.concat("\r\nType: Archway") : description;
        description = isOpen() ? description.concat("\r\nState: Open") : description.concat("\r\nState: Locked");
        description = isTrapped() ? description.concat("\r\nTrapped: " + getTrapDescription()) : description;

        return description;
    }

    /**
    * This method randomly sets the attributes of the door.
    */
    private void setDoor() {

        boolean flag;

        flag = Die.d10() == 1 ? true : false;
        setArchway(flag);
        flag = Die.d20() == 1 ? true : false;
        setTrapped(flag);
        flag = Die.d6() == 1 ? true : false;
        setLocked(flag);
        flag = isLocked() ? false : true;
        setOpen(flag);

    }

    /**
    * This method sets the door to be locked or not.
    * @param flag - A boolean if the door is to be locked or not.
    */
    private void setLocked(boolean flag) {
        this.locked = flag;
    }

    /**
    * This method tells if the door is locked or not.
    * @return A boolean value representing if the door is locked or not.
    */
    private boolean isLocked() {
        return this.locked;
    }

    /**
    * This method gets the Exit associated with the door.
    * @return An Exit associated with the door.
    */
    public Exit getExit() {
        return this.exit;
    }


}
