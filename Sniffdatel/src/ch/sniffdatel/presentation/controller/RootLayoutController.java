package ch.sniffdatel.presentation.controller;

import ch.sniffdatel.presentation.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 * 
 */

public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("About Sniffdatel");
        alert.setHeaderText("Sniffdatel");
     
        ImageView logo = new ImageView("file:resources/images/logo.png");
        logo.setFitHeight(100);
        logo.setFitWidth(100);
        alert.setGraphic(logo);

        alert.setContentText("Version: 1.0\n\n"
        		+ "Sniffdatel was developed in the cours \"Engineering Project 2016\" at the HSR.\n\n"
        		+ "GitHub: https://github.com/SeppDeDepp/HSR_EngineeringProjekt_FS2016\n\n"
        		+ "Authors: A.Stalder, D.Meister, G.Vincenti, S.Krieg");
        alert.getDialogPane().setMinSize(600, Region.USE_PREF_SIZE);

        
        alert.showAndWait();
    }
    
    /**
     * Opens an help view.
     */
    @FXML
    private void handleHelp() {
		mainApp.showHelp();
    }
}