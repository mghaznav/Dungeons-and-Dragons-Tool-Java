/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*     DataFactory.java      *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

/**
* This class is responsible for printing the whole level.
*/
public class DataFactory implements Serializable {

    /** A map of all the doors and their target chambers. */
    private HashMap<Door, ArrayList<Chamber>> doorMap;
    /** A map of all the chambers and all their exits. */
    private HashMap<Integer, ArrayList<String>> chamberExits;
    /** A map of all the passages and all their exits. */
    private HashMap<Integer, ArrayList<String>> passageExits;
    /** An ArrayList to hold all the chambers. */
    private ArrayList<Chamber> allChambers;
    /** An ArrayList to hold all the Passages. */
    private ArrayList<Passage> allPassages;
    /** The level to be printed. */
    private Level theLevel;
    /** List of all the Chamber descriptions. */
    private ArrayList<String> chambersDesc;
    /** List of all the passage descriptions. */
    private ArrayList<String> passagesDesc;

    /**
    * This is the constructor for this class, it requires a level to print.
    * @param currLevel - The level to print.
    */
    public DataFactory(Level currLevel) {
        setLevel(currLevel);
        setDoorMap();
        setPassages();
        setChambers();
    }

    /**
    * This method calls all the different methods to get the description of
    * the whole floor and then prints it all out.
    */
    public void generateLevel() {
        setChamberIds();
        setPassageIds();
        setChamberDescriptions();
        setPassageDescriptions();
    }

    /**
    * This method resets all teh chamber and passage descriptions.
    */
    public void resetDescription() {
        setChamberDescriptions();
        setPassageDescriptions();
    }

    /**
    * This method sets the doorMap attribute, by getting it from level.
    */
    private void setDoorMap() {
        this.doorMap = this.theLevel.getDoorMap();
    }

    /**
    * This method sets the level passed to it as the current level to print.
    * @param currLevel - The current level to print.
    */
    private void setLevel(Level currLevel) {
        this.theLevel = currLevel;
    }

    /**
    * This method sets the list of passages attribute, by getting it from level.
    */
    private void setPassages() {
        this.allPassages = this.theLevel.getPassages();
    }

    /**
    * This method sets the list of chambers attribute, by getting it from level.
    */
    private void setChambers() {
        this.allChambers = this.theLevel.getChambers();
    }

    /**
    * This method assigns id's to all the chambers on the current level.
    */
    private void setChamberIds() {
        for (int i = 1; i <= this.allChambers.size(); i++) {
            Chamber currChamber = this.allChambers.get(i - 1);
            currChamber.setIdNumber(i);
        }
    }

    /**
    * This method assigns id's to all the passages on the current level.
    */
    private void setPassageIds() {
        for (int i = 1; i <= this.allPassages.size(); i++) {
            Passage currPassage = this.allPassages.get(i - 1);
            currPassage.setIdNumber(i);
        }
    }

    /**
    * This method gets the description of all the chambers on the floor.
    */
    private void setChamberDescriptions() {
        chambersDesc = new ArrayList<String>();
        chamberExits = new HashMap<Integer, ArrayList<String>>();
        this.allChambers.forEach((chamber) -> {
            String currChamber = chamber.getDescription();
            chamberExits.put(chamber.getIdNumber(), getExitsDescription(chamber));
            this.chambersDesc.add(currChamber);
        });
    }

    /**
    * This method gets the description of all the exits from a chamber.
    * @param theChamber - The chamber whos exits descriptions need to be set.
    * @return - The ArrayList of all the Exits from the Chamber.
    */
    private ArrayList<String> getExitsDescription(Chamber theChamber) {
        ArrayList<Door> doors = theChamber.getDoors();
        ArrayList<String> allExits = new ArrayList<String>();
        String chamberExit = "";
        for (int i = 0; i < doors.size(); i++) {
            Door currDoor = doors.get(i);
            Chamber targetChamber = currDoor.getTargets().get(0);
            Passage targetPassage = currDoor.getTargetPassage(targetChamber);
            chamberExit = "********* Exit " + (i + 1) + " *********\r\n" + currDoor.getDescription() + "\r\n" + "*** Leads to Chamber " + targetChamber.getIdNumber() + " ***";
            chamberExit = chamberExit.concat("\r\n" + "***** Via Passage " + targetPassage.getIdNumber() + (targetPassage.getIdNumber() < 10 ? " ******" : " *****") + "\r\n" + "***** Enters Door " + targetPassage.getDoors().get(1).getIdNumber() + " ******" + "\r\n\r\n");
            allExits.add(chamberExit);
        }

        return allExits;
    }

    /**
    * This method gets the description of all the exits from a Passage.
    * @param thePassage - The passage whos exits descriptions need to be set.
    * @return - The ArrayList of all the Exits from the passage.
    */
    private ArrayList<String> getPassageExitDesc(Passage thePassage) {
        ArrayList<Door> doors = thePassage.getDoors();
        ArrayList<String> allExits = new ArrayList<String>();
        String passageExit = "";
        for (int i = 0; i < doors.size(); i++) {
            Door currDoor = doors.get(i);
            Chamber targetChamber = currDoor.getTargets().get(0);
            Chamber currChamber = (Chamber) currDoor.getSpace(0);
            passageExit = "***** Starting Chamber " + currChamber.getIdNumber() + " *****\r\n" + "********* Exit " + (i + 1) + " *********\r\n" + currDoor.getDescription() + "\r\n" + "*** Leads to Chamber " + targetChamber.getIdNumber() + " ***";
            passageExit = passageExit.concat("\r\n" + "***** Via Passage " + thePassage.getIdNumber() + (thePassage.getIdNumber() < 10 ? " ******" : " *****") + "\r\n" + "***** Enters Door " + thePassage.getDoors().get(1).getIdNumber() + " ******" + "\r\n\r\n");
            allExits.add(passageExit);
        }

        return allExits;
    }

    /**
    * This method gets the description of all the passages on the level.
    */
    private void setPassageDescriptions() {
        passagesDesc = new ArrayList<String>();
        passageExits = new HashMap<Integer, ArrayList<String>>();
        this.allPassages.forEach((passage) -> {
            passageExits.put(passage.getIdNumber(), getPassageExitDesc(passage));
            Chamber chamberOne = (Chamber) passage.getDoor(0).getSpaces().get(0);
            Chamber chamberTwo = (Chamber) passage.getDoor(1).getSpaces().get(0);
            String currPassage = "********** Passage " + passage.getIdNumber() + " ";
            currPassage = (passage.getIdNumber() < 10) ? currPassage.concat("*") : currPassage;
            currPassage = currPassage.concat("***********\r\n" + "***** Starts from Chamber " + chamberOne.getIdNumber() + " *****\r\n" + passage.getDescription());
            currPassage = currPassage.concat("******* Ends at Chamber " + chamberTwo.getIdNumber() + " *******\r\n");
            passagesDesc.add(currPassage);
        });
    }

    /**
    * This method gets the list of all the chambers in the level.
    * @return - The ArrayList of all the chambers.
    */
    public ArrayList<String> getChambers() {

        ArrayList<String> chamberDesc = new ArrayList<String>();

        this.allChambers.forEach((chamber) -> {
            String currDesc = "Chamber ";
            currDesc = currDesc.concat(Integer.toString(chamber.getIdNumber()));
            chamberDesc.add(currDesc);
        });

        return chamberDesc;
    }

    /**
    * This method gets the list of all the passages in the level.
    * @return - The ArrayList of all the passages.
    */
    public ArrayList<String> getPassages() {

        ArrayList<String> passageDesc = new ArrayList<String>();

        this.allPassages.forEach((passage) -> {
            String currDesc = "Passage ";
            currDesc = currDesc.concat(Integer.toString(passage.getIdNumber()));
            passageDesc.add(currDesc);
        });

        return passageDesc;
    }

    /**
    * This method gets the description of a specific chamber.
    * @param index - The Index of the chamber to get.
    * @return - The String decription of the chamber.
    */
    public String getChamberDesc(int index) {
        return this.chambersDesc.get(index);
    }

    /**
    * This method gets the description of a specific passage.
    * @param index - The Index of the passage to get.
    * @return - The String decription of the chpassageamber.
    */
    public String getPassageDesc(int index) {
        return this.passagesDesc.get(index);
    }

    /**
    * This method gets all the exits of a specific chamber.
    * @param index - The index of the chamber to get.
    * @return - An ArrayList of all the chamber exits description.
    */
    public ArrayList<String> getChamberExits(int index) {
        return this.chamberExits.get(index);
    }

    /**
    * This method gets all the exits of a specific passage.
    * @param index - The index of the passage to get.
    * @return - An ArrayList of all the passage exits description.
    */
    public ArrayList<String> getPassageExits(int index) {
        return this.passageExits.get(index);
    }

}











