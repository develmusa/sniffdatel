package ch.sniffdatel.presentation.controller;

import java.io.IOException;

import ch.sniffdatel.presentation.MainApp;
import ch.sniffdatel.presentation.model.Interface;
import ch.sniffdatel.service.packetParser.Interfaces;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class InterfaceSelectionController {
	@FXML
	private TableView<Interface> interfaceTable;
	@FXML
	private TableColumn<Interface, Number> numberColumn;
	@FXML
	private TableColumn<Interface, String> nameColumn;
	@FXML
	private TableColumn<Interface, String> iPColumn;
	@FXML
	private TableColumn<Interface, String> mACColumn;

	@FXML
	private Button startButton;

	private Stage dialogStage;
	private boolean okClicked = false;
	private MainApp mainApp;
	private SessionOverviewController sessionOverviewController;
	private Interfaces interfaces = Interfaces.getInstance();

	@FXML
	private void initialize() throws IOException {
		startButton.setDisable(true);
		interfaces.initIfaces();
		interfaceTable.setPlaceholder(new Label("no interfaces recognized."));

		numberColumn.setCellValueFactory(cellData -> cellData.getValue().getNumber());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		iPColumn.setCellValueFactory(cellData -> cellData.getValue().getIpAdress());
		mACColumn.setCellValueFactory(cellData -> cellData.getValue().getMacAdress());

		interfaceTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				startButton.setDisable(false);
			}
		});
		
		
		interfaceTable.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2 ){
	            handleOk();
	        }
	    });
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		interfaceTable.setItems(interfaces.getInterfaceData());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks in list.
	 */
	@FXML
	private void handleListActive() {
		startButton.setDisable(false);
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		int selectedIndex = interfaceTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			interfaces.setActiveInterface(selectedIndex);
			sessionOverviewController.getPlayCapture().setDisable(false);
			dialogStage.close();
			sessionOverviewController.handleStartCapture();
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Interface Selected");
			alert.setContentText("Please select a interface in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public void setSessionOverviewController(SessionOverviewController sessionOverviewController) {
		this.sessionOverviewController = sessionOverviewController;

	}

}
