/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*     Controller.java       *
*         1068753           *
*         26/11/19          *
\***************************/

package gui;

import dnd.models.Treasure;
import game.Level;
import game.DataFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import monsters.DBConnection;
import monsters.Monster;

/**
* This class helps the view communicate with the model and helps passing data along.
*/
public class Controller {
    /** An Arraylist of all the doors. */
    private ArrayList<String> currExits;
    /** The data factory of the level generated. */
    private DataFactory myData;
    /** A connection to the DB of monsters. */
    private DBConnection dbc;
    /** The current gui. */
    private GuiDemo myGui;
    /** A map of all the treasures and their roll. */
    private HashMap<String, Integer> treasures;
    /** Index of current space. */
    private int currItem;
    /** The current level. */
    private Level level;

    /**
    * This is the constructor for this class.
    * @param theGui - The main Gui
    */
    public Controller(GuiDemo theGui) {
        myGui = theGui;
        level = new Level();
        level.generateLevel();
        this.dbc = new DBConnection();
        myData = level.getDataFactory();
        createTreasures();
    }

    /**
    * This method gets a list of all the spaces in the current level.
    * @return - An ArrayList of all the spaces in the level.
    */
    public ArrayList<String> getAllList() {
        ArrayList<String> allSpaces = new ArrayList<String>();

        allSpaces.addAll(this.myData.getChambers());
        allSpaces.addAll(this.myData.getPassages());

        return allSpaces;
    }

    /**
    * This method gets all the passages in the level.
    * @return - The ArrayList of passages in the level.
    */
    public ArrayList<String> getPassagesList() {
        return this.myData.getPassages();
    }

    /**
    * This method gets the description of the item pointed to by the index.
    * @param index - The integer index of the item to get description of.
    * @return - A String with the description of the current item.
    */
    public String getItemDescription(int index) {
        this.currItem = index;
        if (index <= 4) {
            currExits = this.myData.getChamberExits(index + 1);
            return this.myData.getChamberDesc(index);
        } else {
            currExits = this.myData.getPassageExits(index - 4);
            return this.myData.getPassageDesc(index - 5);
        }

    }

    /**
    * This method gets the list of all the doors.
    * @return - An ArrayList of all the doors.
    */
    public ArrayList<String> getCurrExits() {
        return this.currExits;
    }

    /**
    * This method saves the current file at the location pointed by path.
    * @param path - The location of where to save the file.
    */
    public void saveFile(String path) {

        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(level);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    /**
    * This method loads the file pointed by path.
    * @param path - The path to the file to load.
    */
    public void loadFile(String path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tryLoadFile(in);
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        }
    }

    /**
    * This method loads the file pointed by path.
    * @param in - The object to load.
    */
    private void tryLoadFile(ObjectInputStream in) {
        try {
            this.level = (Level) in.readObject();
            this.myData = this.level.getDataFactory();
            this.myGui.reset();
        } catch (ClassNotFoundException c) {
            System.out.println("Level class not found");
            c.printStackTrace();
            return;
        } catch (IOException i) {
            i.printStackTrace();
            return;
        }
    }

    /**
    * This method gets all the monsters from the database.
    * @return - The Arraylist of all the monsters.
    */
    public ArrayList<String> getDBMonsters() {
        ArrayList<Monster> allMonsters = level.getAllMonsters();
        ArrayList<String> monsters = new ArrayList<String>();

        allMonsters.forEach((m) -> {
            monsters.add(m.getName());
        });

        return monsters;
    }

    /**
    * This method gets all the treasures.
    * @return - The Arraylist of all the treasures.
    */
    public ArrayList<String> getAllTreasure() {
        ArrayList<String> allTreasures = new ArrayList<String>();
        allTreasures.addAll(this.treasures.keySet());
        return allTreasures;
    }

    /**
    * This method gets all the treasures in the current space.
    * @return - The Arraylist of the treasures.
    */
    public ArrayList<String> getCurrTreasures() {
        ArrayList<String> allTreasures = new ArrayList<String>();
        ArrayList<Treasure> treasure = new ArrayList<Treasure>();
        if (this.currItem <= 4) {
            treasure = this.level.getChamberTreasures(this.currItem);
        } else {
            treasure = this.level.getPassageTreasures((this.currItem - 5));
        }

        treasure.forEach((t) -> {
            allTreasures.add(t.getDescription());
        });

        return allTreasures;
    }

    /**
    * This method gets all the monsters in the current space.
    * @return - The Arraylist of the monsters.
    */
    public ArrayList<String> getCurrMonsters() {
        ArrayList<String> allMonsters = new ArrayList<String>();
        ArrayList<Monster> monsters = new ArrayList<Monster>();
        if (this.currItem <= 4) {
            monsters = this.level.getChamberMonsters(this.currItem);
        } else {
            monsters = this.level.getPassageMonsters((this.currItem - 5));
        }

        monsters.forEach((m) -> {
            allMonsters.add(m.getName());
        });

        return allMonsters;
    }

    /**
    * This method adds a Monster to a space at a specific index.
    * @param name - The name of the monster to add.
    */
    public void addMonsterToSpace(String name) {

        if (this.currItem <= 4) {
            addMonsterToChamber(name);
        } else {
            addMonsterToPassage(name);
        }

    }

    /**
    * This method adds a Treasure to a space at a specific index.
    * @param name - The name of the treasure to add.
    */
    public void addTreasureToSpace(String name) {

        if (this.currItem <= 4) {
            addTreasureToChamber(name);
        } else {
            addTreasureToPassage(name);
        }

    }

    /**
    * This method adds a Treasure to a passage.
    * @param name - The name of the Treasure to add.
    */
    private void addTreasureToPassage(String name) {
        Treasure treasure = new Treasure();
        treasure.chooseTreasure(this.treasures.get(name));
        level.addTreasureInPassage(treasure, (this.currItem - 5));
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method adds a Treasure to a Chamber.
    * @param name - The name of the Treasure to add.
    */
    private void addTreasureToChamber(String name) {
        Treasure treasure = new Treasure();
        treasure.chooseTreasure(this.treasures.get(name));
        level.addTreasureInChamber(treasure, this.currItem);
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method removes a Treasure from a Chamber.
    * @param name - The name of the Treasure to remove.
    */
    private void removeTreasureFromChamber(String name) {
        Treasure treasure = new Treasure();
        treasure.chooseTreasure(this.treasures.get(name));
        level.deleteTreasureFromChamber(treasure, this.currItem);
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method removes a Treasure from a passage.
    * @param name - The name of the Treasure to remove.
    */
    private void removeTreasureFromPassage(String name) {
        Treasure treasure = new Treasure();
        treasure.chooseTreasure(this.treasures.get(name));
        level.deleteTreasureFromPassage(treasure, (this.currItem - 5));
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method removes a monster from a space.
    * @param name - The name of the monster to remove.
    */
    public void removeMonsterFromSpace(String name) {
        if (this.currItem <= 4) {
            removeMonsterFromChamber(name);
        } else {
            removeMonsterFromPassage(name);
        }
    }

    /**
    * This method removes a treasure from a space.
    * @param name - The name of the treasure to remove.
    */
    public void removeTreasureFromSpace(String name) {

        if (this.currItem <= 4) {
            removeTreasureFromChamber(name);
        } else {
            removeTreasureFromPassage(name);
        }
    }

    /**
    * This method removes a monster from a chamber.
    * @param name - The name of the monster to remove.
    */
    private void removeMonsterFromChamber(String name) {
        Monster monster = this.dbc.findMonster(name);
        level.deleteMonsterFromChamber(monster, this.currItem);
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method removes a monster from a passage.
    * @param name - The name of the monster to remove.
    */
    private void removeMonsterFromPassage(String name) {
        Monster monster = this.dbc.findMonster(name);
        level.deleteMonsterFromPassage(monster, (this.currItem - 5));
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method adds a monster to a chamber.
    * @param name - The name of the monster to add.
    */
    private void addMonsterToChamber(String name) {
        Monster monster = this.dbc.findMonster(name);
        level.addMonsterInChamber(monster, this.currItem);
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method adds a monster to a passage.
    * @param name - The name of the monster to add.
    */
    private void addMonsterToPassage(String name) {
        Monster monster = this.dbc.findMonster(name);
        level.addMonsterInPassage(monster, (this.currItem - 5));
        myData.resetDescription();
        myGui.reset();
    }

    /**
    * This method gets the index of the current item.
    * @return - The Current index.
    */
    public int getCurrItem() {
        return this.currItem;
    }

    /**
    * This method creates a hashmap of all the treasures
    * and the in to roll to get thos tresures.
    */
    private void createTreasures() {
        this.treasures = new HashMap<String, Integer>();
        this.treasures.put("1000 copper pieces/level", 2);
        this.treasures.put("1000 silver pieces/level", 27);
        this.treasures.put("750 electrum pieces/level", 52);
        this.treasures.put("250 gold pieces/level", 67);
        this.treasures.put("100 platinum pieces/level", 82);
        this.treasures.put("1-4 gems/level", 92);
        this.treasures.put("1 piece jewellery/level", 96);
        this.treasures.put("1 magic item (roll on Magic item table", 99);
    }

}
