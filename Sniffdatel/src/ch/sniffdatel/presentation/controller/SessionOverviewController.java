package ch.sniffdatel.presentation.controller;

import ch.sniffdatel.application.AudioPlayer;
import ch.sniffdatel.application.PacketScanner;
import ch.sniffdatel.presentation.MainApp;
import ch.sniffdatel.presentation.model.SessionModel;
import ch.sniffdatel.service.AudioConverter;
import ch.sniffdatel.service.SessionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class SessionOverviewController {

	@FXML
	private TableView<SessionModel> sessionTable;
	@FXML
	private TableColumn<SessionModel, String> sessionNameColumn;
	@FXML
	private TableColumn<SessionModel, String> callerLeftColumn;
	@FXML
	private TableColumn<SessionModel, String> callerRightColumn;
	@FXML
	private TableColumn<SessionModel, String> stateColumn;
	@FXML
	private TableColumn<SessionModel, String> codecColumn;

	@FXML
	private Button stopCaptureButton;

	@FXML
	private ToggleButton playCaptureButton;

	public ToggleButton getPlayCapture() {
		return playCaptureButton;
	}

	@FXML
	private Button resetCaptureButton;

	@FXML
	private Button selectInterfaceButton;

	@FXML
	private ToggleButton playAudioButton;

	@FXML
	private Button stopAudioButton;

	@FXML
	private ComboBox<String> audioDirectionComboBox;

	private MainApp mainApp;
	private SessionHandler sessionHandler;
	private Thread scannerThread;
	private AudioPlayer leftAudioPlayer = new AudioPlayer("left");
	private AudioPlayer rightAudioPlayer = new AudioPlayer("right");
	private AudioConverter leftAudioConverter = new AudioConverter("left");
	private AudioConverter rightAudioConverter = new AudioConverter("right");
	private Thread leftAudioConverterThread;
	private Thread rightAudioConverterThread;
	private Thread leftAudioPlayerThread;
	private Thread rightAudioPlayerThread;
	private PacketScanner packetScanner;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public SessionOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		audioDirectionComboBox.getItems().addAll("Both directions", "Left --> Right", "Right --> Left");
		audioDirectionComboBox.getSelectionModel().selectFirst();
		setInitionalButtonActivity();
		setToolTips();
		
		// Initialize the session table.
		sessionTable.setPlaceholder(new Label("Select interface to start capturing."));
		sessionNameColumn.setCellValueFactory(cellData -> cellData.getValue().sessionNameProperty());
		callerRightColumn.setCellValueFactory(cellData -> cellData.getValue().rightIpAdressProperty());
		callerLeftColumn.setCellValueFactory(cellData -> cellData.getValue().leftIpAdressProperty());
		stateColumn.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
		codecColumn.setCellValueFactory(cellData -> cellData.getValue().codecProperty());

		// Clear person details.
		showSessionDetails(null);

		// Listen for selection changes and show the session details when
		// changed.
		sessionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				showSessionDetails(newValue);


			}
		});
		
		sessionTable.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2 ){
	        	handleAudioPlay();
	        }
	    });
	}

	@FXML
	private TreeView<String> tree;

	private void setInitionalButtonActivity() {
		selectInterfaceButton.setDisable(false);
		playCaptureButton.setDisable(true);
		stopCaptureButton.setDisable(true);
		resetCaptureButton.setDisable(true);
		playAudioButton.setDisable(true);
		stopAudioButton.setDisable(true);
	}

	private void setToolTips() {
		playCaptureButton.setTooltip(new Tooltip("Start capturing SIP-Sessions"));
		stopCaptureButton.setTooltip(new Tooltip("Stop capturing sessions"));
		resetCaptureButton.setTooltip(new Tooltip("Restart current capture"));
		selectInterfaceButton.setTooltip(new Tooltip("Select network interface"));
		playAudioButton.setTooltip(new Tooltip("Play audio from selected session"));
		stopAudioButton.setTooltip(new Tooltip("Stop current audio playback"));
		audioDirectionComboBox.setTooltip(new Tooltip("Select audio directions"));
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Fills all text fields to show details about the session. If the specified
	 * session is null, all text fields are cleared.
	 * 
	 * @param session
	 *            the session or null
	 */
	private void showSessionDetails(SessionModel session) {

		if (session != null) {
			playAudioButton.setDisable(false);
//			playAudioButton.setDisable(!session.getIsReadyforAudioPlay());
//			setObserverbleOnState();
			TreeItem<String> root = new TreeItem<>("Session details");
			TreeItem<String> callerLeftTreeItem = new TreeItem<>("Caller Left");
			TreeItem<String> callerRightTreeItem = new TreeItem<>("Caller Right");
			tree.setRoot(root);

			root.getChildren().add(callerLeftTreeItem);
			root.getChildren().add(callerRightTreeItem);

			TreeItem<String> leftUriAddressTreeItem = new TreeItem<>("URI:			" + session.getLeftUri());
			callerLeftTreeItem.getChildren().add(leftUriAddressTreeItem);
			TreeItem<String> leftIpAddressTreeItem = new TreeItem<>("IP address:	" + session.getLeftIpAdress());
			callerLeftTreeItem.getChildren().add(leftIpAddressTreeItem);
			TreeItem<String> leftMacAddressTreeItem = new TreeItem<>("MAC address:	" + session.getLeftMacAdress());
			callerLeftTreeItem.getChildren().add(leftMacAddressTreeItem);
			TreeItem<String> rightUriAddressTreeItem = new TreeItem<>("URI:			" + session.getRightUri());
			callerRightTreeItem.getChildren().add(rightUriAddressTreeItem);
			TreeItem<String> rightIpAddressTreeItem = new TreeItem<>("IP address:	" + session.getRightIpAdress());
			callerRightTreeItem.getChildren().add(rightIpAddressTreeItem);
			TreeItem<String> rightMacAddressTreeItem = new TreeItem<>("MAC address:	" + session.getRightMacAdress());
			callerRightTreeItem.getChildren().add(rightMacAddressTreeItem);

			TreeItem<String> sessionNameTreeItem = new TreeItem<>("Session Name:	" + session.getSessionName());
			root.getChildren().add(sessionNameTreeItem);
			String codecSupported = session.getCodecIsSupported() ? "" : "	!!!Codec is not supported!!!";
			TreeItem<String> sessionCodecTreeItem = new TreeItem<>("Codec:		" + session.getCodec() + codecSupported);
			root.getChildren().add(sessionCodecTreeItem);
			TreeItem<String> startTimeTreeItem = new TreeItem<>("Starting Time:	" + session.getStartTime());
			root.getChildren().add(startTimeTreeItem);

			root.setExpanded(true);

		} else {
			TreeItem<String> root = new TreeItem<>("");
			tree.setRoot(root);

		}
	}

	/**
	 * Called when the user clicks Interface Selection.
	 */
	@FXML
	private void handleInterfaceSelection() {
		mainApp.showInterfaceSelection(this);

	}

	/**
	 * Called when the user clicks Start Capture.
	 */
	@FXML
	protected void handleStartCapture() {
		mainApp.getPrimaryStage().setTitle("Sniffdatel: searching VoIP-Sessions");
		
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setMaxSize(40.0, 40.0);
		sessionTable.setPlaceholder(progressIndicator);
		showSessionDetails(null);
		selectInterfaceButton.setDisable(true);
		playCaptureButton.setDisable(true);
		stopCaptureButton.setDisable(false);
		resetCaptureButton.setDisable(false);

		this.packetScanner = new PacketScanner();
		this.sessionHandler = new SessionHandler();

		sessionHandler.clearCurrentSessions();
		sessionHandler.clearSipSdpQueue();

		scannerThread = new Thread(packetScanner);

		// Add observable list data to the table

		sessionTable.setItems(sessionHandler.getCurrentSessions().getSessionData());
		scannerThread.start();

	}

	/**
	 * Called when the user clicks Stop Capture.
	 */
	@FXML
	private void handleStopCapture() {
		mainApp.getPrimaryStage().setTitle("Sniffdatel");
		stopCaptureButton.setDisable(true);
		resetCaptureButton.setDisable(true);
		playCaptureButton.setDisable(false);
		playAudioButton.setDisable(true);
		stopAudioButton.setDisable(true);
		selectInterfaceButton.setDisable(false);
		sessionTable.setPlaceholder(new Label("Select interface to start capturing."));
		packetScanner.stopScanner();

	}

	/**
	 * Called when the user clicks Restart Capture.
	 */
	@FXML
	private void handleRestartCapture() {
		handleAudioStop();
		handleStopCapture();
		handleStartCapture();
		showSessionDetails(null);

	}

	/**
	 * Called when the user clicks Audio Play.
	 */
	@FXML
	private void handleAudioPlay() {
		if (!sessionTable.getSelectionModel().selectedItemProperty().getValue().getIsReadyforAudioPlay()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Call not initialzed");
			alert.setHeaderText("Call not initialze");
			alert.setContentText("Please wait for \"Active Call\" state");

			alert.showAndWait();
		} else if (sessionTable.getSelectionModel().selectedItemProperty().getValue().getIsReadyforAudioPlay()
				&& !sessionTable.getSelectionModel().selectedItemProperty().getValue().getCodecIsPlayable()) {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Wrong codec");
			alert.setHeaderText("Not supportet codec");
			alert.setContentText("Please select a session with PCMU or PCMA");

			alert.showAndWait();
		} else {
			mainApp.getPrimaryStage().setTitle("Sniffdatel: Playing VoIP-Session");
			playAudioButton.setDisable(true);
			stopAudioButton.setDisable(false);


			// Start audioplayer with selected direction
			int audioDirectionSelection = audioDirectionComboBox.getSelectionModel().getSelectedIndex();
			if (audioDirectionSelection == 0) {

				packetScanner.setActiveSession(sessionTable.getSelectionModel().selectedItemProperty().getValue().getLeftPort(), sessionTable.getSelectionModel().selectedItemProperty().getValue()
						.getRightPort());
				startLeftAudio(leftAudioPlayer, leftAudioConverter);
				startRightAudio(rightAudioPlayer, rightAudioConverter);

			} else if (audioDirectionSelection == 1) {
				packetScanner.setActiveSession(sessionTable.getSelectionModel().selectedItemProperty().getValue().getLeftPort(), "0");
				startLeftAudio(leftAudioPlayer, leftAudioConverter);

			} else {
				packetScanner.setActiveSession("0", sessionTable.getSelectionModel().selectedItemProperty().getValue().getRightPort());
				startRightAudio(rightAudioPlayer, rightAudioConverter);

			}

		}

	}

	private void startLeftAudio(AudioPlayer audioPlayer, AudioConverter audioConverter) {
		leftAudioConverterThread = new Thread(audioConverter);
		audioConverter.setIsRunning(true);
		leftAudioConverterThread.start();
		leftAudioPlayerThread = new Thread(audioPlayer);
		audioPlayer.setIsRunning(true);
		leftAudioPlayerThread.start();
	}

	private void startRightAudio(AudioPlayer audioPlayer, AudioConverter audioConverter) {
		rightAudioConverterThread = new Thread(audioConverter);
		audioConverter.setIsRunning(true);
		rightAudioConverterThread.start();
		rightAudioPlayerThread = new Thread(audioPlayer);
		audioPlayer.setIsRunning(true);
		rightAudioPlayerThread.start();
	}

	/**
	 * Called when the user clicks Audio Stop.
	 */
	@FXML
	private void handleAudioStop() {
		mainApp.getPrimaryStage().setTitle("Sniffdatel");
		stopAudioButton.setDisable(true);
		playAudioButton.setDisable(false);
		leftAudioConverter.setIsRunning(false);
		leftAudioPlayer.setIsRunning(false);
		rightAudioConverter.setIsRunning(false);
		rightAudioPlayer.setIsRunning(false);
		packetScanner.setActiveSession("-1", "-1");

		// Singletonobjects
		leftAudioConverter.clearJitterBuffer();
		leftAudioConverter.clearRtpQueue();

	}

}