/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*       GuiDemo.java        *
*         1068753           *
*         26/11/19          *
\***************************/

package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.util.ArrayList;
import java.io.File;

/**
* This class is the GUI of this program.
*/
public class GuiDemo extends Application {
    /* Even if it is a GUI it is useful to have instance variables
    so that you can break the processing up into smaller methods that have
    one responsibility.
    */

    /** The controller. */
    private Controller theController;
    /** the root element of this GUI. */
    private BorderPane root;
    /** The list of all the space son the current level. */
    private ListView cpList;
    /** The center text area. */
    private TextArea text;
    /** A popup for doors. */
    private Popup descriptionPane;
    /** The stage that is passed in on initialization. */
    private Stage primaryStage;

    /**
    * A call to start replaces a call to the constructor for a JavaFX GUI.
    * @param assignedStage the Stage.
    */
    @Override
    public void start(Stage assignedStage) {
        primaryStage = assignedStage;
        setController();

        setUpRoot();

        Scene scene = new Scene(root, 750, 500);
        scene.getStylesheets().add("res/style.css");

        primaryStage.setTitle("Dungeons and Dragons");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
    * This method resets the content on the gui.
    */
    public void reset() {
        int currSpace = this.cpList.getSelectionModel().getSelectedIndex();
        setUpRoot();
        Scene scene = new Scene(root, 750, 500);
        scene.getStylesheets().add("res/style.css");

        primaryStage.setTitle("Dungeons and Dragons");
        primaryStage.setScene(scene);
        primaryStage.show();
        selectPrevList(currSpace);

    }

    /**
    * This method selects the index passed to it, on the list of spaces.
    * @param index - an integer index of the item to select.
    */
    private void selectPrevList(int index) {
        if (index < 0) {
            index = 0;
        }
        this.cpList.getSelectionModel().select(index);
        this.cpList.getFocusModel().focus(index);
        updateText(index);
    }

    /**
    * This method createsa new controller and sets it as an attribute of the class.
    */
    private void setController() {
        this.theController = new Controller(this);
    }

    /**
    * This method sets up all the child nodes of the root node.
    */
    private void setUpRoot() {
        root = new BorderPane();

        root.setLeft(setLeftNode());
        root.setCenter(setCenterNode());
        root.setRight(setRightNode());
        root.setTop(setTopNode());
    }

    /**
    * This method creates a menu bar with the option to save and load a file.
    * @return - A MenuBar element.
    */
    private MenuBar setTopNode() {

        MenuBar menu = new MenuBar();
        Menu file = new Menu("File");
        MenuItem save = saveItem();
        MenuItem load = loadItem();

        file.getItems().addAll(save, load);
        menu.getMenus().add(file);

        return menu;

    }

    /**
    * This method creates a MenuItem for loading a file.
    * @return - The MenuItem for loading a file.
    */
    private MenuItem loadItem() {
        FileChooser chooser = new FileChooser();
        MenuItem item = new MenuItem("Load File");
        item.setOnAction((ActionEvent event) -> {
            File file = chooser.showOpenDialog(primaryStage);
            theController.loadFile(file.toString());
        });
        return item;
    }

    /**
    * This method creates a MenuItem for saving a file.
    * @return - The MenuItem for saving a file.
    */
    private MenuItem saveItem() {
        FileChooser chooser = new FileChooser();
        MenuItem item = new MenuItem("Save File");
        item.setOnAction((ActionEvent event) -> {
            File file = chooser.showSaveDialog(primaryStage);
            theController.saveFile(file.toString());
        });
        return item;
    }

    /**
    * This method creates a VBox to set it as the right node of the rood.
    * @return - The VBox created to be the right node of the root.
    */
    private VBox setRightNode() {

        VBox box = new VBox();
        box.getStyleClass().add("box");
        ComboBox<String> exitList = updateExitList();

        Label label = new Label("Doors: ");
        box.getChildren().add(label);

        box.getChildren().addAll(exitList);

        return box;

    }

    /**
    * This method creates a VBox to set it as the left node of the rood.
    * @return - The VBox created to be the left node of the root.
    */
    private VBox setLeftNode() {

        VBox box = new VBox();
        box.getStyleClass().add("box");
        Region fill = new Region();
        fill.setPrefHeight(200);
        cpList = new ListView();
        cpList.getStyleClass().add("space-list");

        ObservableList listItems = getListItems();

        cpList.setItems(listItems);
        initListAction();

        box.getChildren().addAll(cpList, fill, createEditButton());

        return box;
    }

    /**
    * This method creates a button for editting the current space.
    * @return - The Button for editting.
    */
    private Button createEditButton() {
        Button btn = new Button();
        btn.setText("Edit");
        btn.setOnAction((ActionEvent event) -> {
            Edit edit = new Edit(theController);
            Popup editPopup = edit.getPopup();
            editPopup.show(primaryStage);
        });

        return btn;
    }

    /**
    * This method sets the ActionEvent for the list items in the space list.
    */
    private void initListAction() {

        cpList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = cpList.getSelectionModel().getSelectedIndex();
                if (index < 0) {
                    return;
                }
                text.setText(theController.getItemDescription(index));
                root.setRight(setRightNode());
            }
        });

    }

    /**
    * This method updates the text in the center text box.
    * @param index - The integer index of the item by which the text is to b e updates.
    */
    private void updateText(int index) {
        text.setText(theController.getItemDescription(index));
        root.setRight(setRightNode());
    }

    /**
    * This method gets the list of doors in a chamber or a passage.
    * @return - The ObservableList of all the doors.
    */
    private ObservableList getExitList() {
        ObservableList list = FXCollections.observableArrayList();
        ArrayList<String> exits = theController.getCurrExits();

        for (int i = 0; i < exits.size(); i++) {
            list.add("Door " + (i + 1));

        }

        return list;
    }

    /**
    * This method gets the list of Spaces in the current levl.
    * @return - The ObservableList of all the spaces.
    */
    private ObservableList getListItems() {
        ObservableList list = FXCollections.observableArrayList();

        theController.getAllList().forEach((space) -> {
            list.add(space);
        });

        return list;
    }

    /**
    * This method creates a VBox to be set as the center node of the root.
    * @return - The VBox to be set as the center node of the root.
    */
    private VBox setCenterNode() {
        VBox box = new VBox();
        box.getStyleClass().add("box");

        text = new TextArea();
        text.setEditable(false);
        text.setText(theController.getItemDescription(0));
        text.setPrefWidth(400);
        text.setPrefHeight(400);
        text.setWrapText(true);
        box.getChildren().addAll(text);

        return box;
    }

    /**
    * This method updates the list of doors in the right node of the root.
    * @return - An updated ComboBox to be set as the list of doors.
    */
    private ComboBox<String> updateExitList() {
        ComboBox<String> exitList = new ComboBox<String>();
        ObservableList list = FXCollections.observableArrayList();
        ArrayList<String> exits = theController.getCurrExits();

        for (int i = 0; i < exits.size(); i++) {
            list.add("Door " + (i + 1));

        }

        exitList.setItems(list);
        rightListAction(exitList);

        return exitList;
    }

    /**
    * This method sets the ActionEvent of the items in the ComboBox holding the list of doors.
    * @param list - The ComboBox whos item's ActionEvent needs to be set.
    */
    private void rightListAction(ComboBox<String> list) {

        list.setOnAction((event) -> {
            String value = (String) list.getValue();
            char num = value.charAt(value.length() - 1);
            int index = Character.getNumericValue(num);
            descriptionPane = createPopUp(theController.getCurrExits().get(index - 1));
            descriptionPane.show(primaryStage);
        });

    }

    /**
    * This method creates a popup showing the description of the selected door.
    * @param theText - The text to be in the popup.
    * @return - The created popup.
    */
    private Popup createPopUp(String theText) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.setAutoFix(true);
        TextArea textA = new TextArea(theText);
        textA.setPrefWidth(200);
        textA.setPrefHeight(200);
        textA.setEditable(false);
        popup.getContent().addAll(textA);
        return popup;
    }

    /**
    * This is the main method for this program.
    * @param args - Main args
    */
    public static void main(String[] args) {
        launch(args);
    }

}
