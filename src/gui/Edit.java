/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*        Edit.java          *
*         1068753           *
*         26/11/19          *
\***************************/

package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Popup;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import java.util.ArrayList;

/**
* This class represents the edit popup window.
*/
public class Edit {

    /** The current controller. */
    private Controller theController;
    /** The main popup. */
    private Popup thePopup;
    /** The main VBox in the popup. */
    private VBox theBox;
    /** The list of the current items. */
    private ComboBox<String> list;
    /** The int reprsenting current action. */
    private int action;
    /** The int reprsenting current type. */
    private int type;

    /**
    * This is the constructor for this class.
    * @param controller - The current controller.
    */
    public Edit(Controller controller) {
        thePopup = new Popup();
        this.action = -1;
        this.type = -1;
        setController(controller);
        setUpPopup();
    }

    /**
    * This method sets up the popup.
    */
    private void setUpPopup() {
        setVbox();
        this.thePopup.getContent().add(this.theBox);
    }

    /**
    * This method sets the main VBox.
    */
    private void setVbox() {
        theBox = new VBox();
        theBox.getStyleClass().add("edit-popup");
        addMenuBar();
        addContent();
    }

    /**
    * This method adds a vbow under the popup header.
    */
    private void addContent() {
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("content-box");
        contentBox.getChildren().addAll(getOptionGroup1(), getOptionGroup2(), getList(), getButtons());
        this.theBox.getChildren().add(contentBox);
    }

    /**
    * This method creates an group of radio buttons for selecting type of action.
    * @return - An HBox with the group of radio buttons.
    */
    private HBox getOptionGroup1() {
        HBox region = new HBox();
        region.getStyleClass().add("option-hbox");
        ToggleGroup group = setGroup1();
        RadioButton treasure = new RadioButton("Treasure");
        treasure.setUserData(0);
        treasure.setToggleGroup(group);
        RadioButton monster = new RadioButton("Monster");
        monster.setUserData(1);
        monster.setToggleGroup(group);
        region.getChildren().addAll(new Label("Select Type: "), treasure, monster);
        return region;
    }

    /**
    * This method creates an group of radio buttons for selecting action.
    * @return - An HBox with the group of radio buttons.
    */
    private HBox getOptionGroup2() {
        HBox region = new HBox();
        region.getStyleClass().add("option-hbox");
        ToggleGroup group = setGroup2();
        RadioButton add = new RadioButton("Add");
        add.setToggleGroup(group);
        add.setUserData(0);
        RadioButton remove = new RadioButton("Remove");
        remove.setToggleGroup(group);
        remove.setUserData(1);
        region.getChildren().addAll(new Label("Select Action: "), add, remove);
        return region;
    }

    /**
    * This method adds a listener to the first toggle group.
    * @return - A ToggleGroup with a new listner.
    */
    private ToggleGroup setGroup1() {
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if (group.getSelectedToggle() != null) {
                    setType((Integer) group.getSelectedToggle().getUserData());
                    loadList();
                }
            }
        });
        return group;
    }

    /**
    * This method adds a listener to the second toggle group.
    * @return - A ToggleGroup with a new listner.
    */
    private ToggleGroup setGroup2() {
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if (group.getSelectedToggle() != null) {
                    setAction((Integer) group.getSelectedToggle().getUserData());
                    loadList();
                }
            }
        });
        return group;
    }

    /**
    * This method loads the list for the combobox.
    */
    private void loadList() {
        enableList();
        if (this.type == 1 && this.action == 0) {
            loadAllMonsters();
        } else if (this.type == 1 && this.action == 1) {
            loadCurrMonsters();
        } else if (this.type == 0 && this.action == 0) {
            loadAllTreasures();
        } else if (this.type == 0 && this.action == 1) {
            loadCurrTreasures();
        }
    }

    /**
    * This method loads the current monsters in the combobox list.
    */
    private void loadCurrMonsters() {
        ObservableList monsterList = FXCollections.observableArrayList();
        ArrayList<String> monsters = theController.getCurrMonsters();

        monsters.forEach((m) -> {
            monsterList.add(m);
        });

        this.list.setItems(monsterList);
    }

    /**
    * This method loads the current treasures in the combobox list.
    */
    private void loadCurrTreasures() {
        ObservableList treasureList = FXCollections.observableArrayList();
        ArrayList<String> treasure = theController.getCurrTreasures();

        treasure.forEach((t) -> {
            treasureList.add(t);
        });

        this.list.setItems(treasureList);
    }

    /**
    * This method loads all the monsters in the combobox list.
    */
    private void loadAllMonsters() {
        ObservableList monsterList = FXCollections.observableArrayList();
        ArrayList<String> monsters = theController.getDBMonsters();

        monsters.forEach((m) -> {
            monsterList.add(m);
        });

        this.list.setItems(monsterList);
    }

    /**
    * This method loads all the treasures in the combobox list.
    */
    private void loadAllTreasures() {
        ObservableList treasureList = FXCollections.observableArrayList();
        ArrayList<String> treasure = theController.getAllTreasure();

        treasure.forEach((t) -> {
            treasureList.add(t);
        });

        this.list.setItems(treasureList);
    }

    /**
    * This method enables the list for the user to be able to use it.
    */
    private void enableList() {
        if (this.action != -1 && this.type != -1) {
            this.list.setDisable(false);
        }
    }

    /**
    * This method creates the ComboBox list of current items.
    * @return - An HBox with the comboBox.
    */
    private HBox getList() {
        HBox region = new HBox();
        region.getStyleClass().add("option-hbox");
        this.list = new ComboBox<String>();
        this.list.setDisable(true);
        region.getChildren().addAll(new Label("Select Item:   "), list);
        return region;
    }

    /**
    * This method creates the save button.
    * @return - An HBox with the button.
    */
    private HBox getButtons() {
        HBox region = new HBox();
        region.getStyleClass().add("button-hbox");
        Button save = new Button("Save");
        save.setOnAction((ActionEvent event) -> {
            saveChanges();
            this.thePopup.hide();
        });
        region.getChildren().add(save);
        return region;
    }

    /**
    * This method applies the changes made by the user.
    */
    private void saveChanges() {
        if (list.getValue() != null) {
            if (this.type == 1 && this.action == 0) {
                addMonster();
            } else if (this.type == 1 && this.action == 1) {
                removeMonster();
            } else if (this.type == 0 && this.action == 0) {
                addTreasure();
            } else if (this.type == 0 && this.action == 1) {
                removeTreasure();
            }
        }
    }

    /**
    * This method removes a treasure.
    */
    private void removeTreasure() {
        String value = (String) list.getValue();
        this.theController.removeTreasureFromSpace(value);
    }

    /**
    * This method adds a treasure.
    */
    private void addTreasure() {
        String value = (String) list.getValue();
        this.theController.addTreasureToSpace(value);
    }

    /**
    * This method removes a monster.
    */
    private void removeMonster() {
        String value = (String) list.getValue();
        this.theController.removeMonsterFromSpace(value);
    }

    /**
    * This method adds a monster.
    */
    private void addMonster() {
        String value = (String) list.getValue();
        this.theController.addMonsterToSpace(value);
    }

    /**
    * This method adds the top menu bar to the popup.
    */
    private void addMenuBar() {
        HBox menuBar = new HBox();
        menuBar.getStyleClass().add("popup-bar");

        Region leftRegion = new Region();
        Region rightRegion = new Region();
        menuBar.setHgrow(leftRegion, Priority.ALWAYS);
        menuBar.setHgrow(rightRegion, Priority.ALWAYS);

        menuBar.getChildren().addAll(addCloseButton(), leftRegion, new Label("Edit Window"), rightRegion);
        this.theBox.getChildren().add(menuBar);
    }

    /**
    * This method creates a close button for the popup.
    * @return - The Button to close the popup.
    */
    private Button addCloseButton() {
        Button closeButton = new Button();
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction((ActionEvent event) -> {
            this.thePopup.hide();
        });
        return closeButton;
    }

    /**
    * This method sets the action atribute.
    * @param i - The integer value to set.
    */
    private void setAction(int i) {
        this.action = i;
    }

    /**
    * This method sets the type atribute.
    * @param i - The integer value to set.
    */
    private void setType(int i) {
        this.type = i;
    }

    /**
    * This method sets the controller atribute.
    * @param controller - The Controller.
    */
    private void setController(Controller controller) {
        this.theController = controller;
    }

    /**
    * This method returns the popup.
    * @return - The popup.
    */
    public Popup getPopup() {
        return this.thePopup;
    }

}
