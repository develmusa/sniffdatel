package ch.sniffdatel.presentation.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Session.
 */
public class SessionModel {

	private final StringProperty leftIpAdress;
	private final StringProperty rightIpAdress;
	private final StringProperty leftMacAdress;
	private final StringProperty rightMacAdress;
	private final StringProperty leftUri;
	private final StringProperty rightUri;
	private final StringProperty leftPort;
	private final StringProperty rightPort;
	private final StringProperty sessionName;
	private final StringProperty codec;
	private final StringProperty startTime;

	private StringProperty state;
	private Boolean readyforAudioPlay = false;
	private Boolean codecIsSupported = false;

	/**
	 * Constructor with some initial data.
	 * 
	 * @param sessionName
	 * @param leftIpAdress
	 * @param rightIpAdress
	 * @param state
	 * @param codec
	 */
	public SessionModel(String sessionName, String leftIpAdress, String rightIpAdress, String state, String codec, String leftMacAdress, String rightMacAdress, String leftUri, String rightUri,
			String leftPort, String rightPort, String startTime, Boolean isReady, Boolean isCodecSupported) {
		this.sessionName = new SimpleStringProperty(sessionName);
		this.leftIpAdress = new SimpleStringProperty(leftIpAdress);
		this.rightIpAdress = new SimpleStringProperty(rightIpAdress);
		this.state = new SimpleStringProperty(state);
		this.codec = new SimpleStringProperty(codec);
		this.leftMacAdress = new SimpleStringProperty(leftMacAdress);
		this.rightMacAdress = new SimpleStringProperty(rightMacAdress);
		this.leftUri = new SimpleStringProperty(leftUri);
		this.rightUri = new SimpleStringProperty(rightUri);
		this.leftPort = new SimpleStringProperty(leftPort);
		this.rightPort = new SimpleStringProperty(rightPort);
		this.startTime = new SimpleStringProperty(startTime);
		
		this.readyforAudioPlay = isReady;
		this.codecIsSupported = isCodecSupported;

	}

	public String getLeftIpAdress() {
		return leftIpAdress.get();
	}

	public void setLeftIpAdress(String leftIpAdress) {
		this.leftIpAdress.set(leftIpAdress);
	}

	public StringProperty leftIpAdressProperty() {
		return leftIpAdress;
	}

	public String getRightIpAdress() {
		return rightIpAdress.get();
	}

	public void setRightIpAdress(String rightIpAdress) {
		this.rightIpAdress.set(rightIpAdress);
	}

	public StringProperty rightIpAdressProperty() {
		return rightIpAdress;
	}

	public String getLeftMacAdress() {
		return leftMacAdress.get();
	}

	public void setLeftMacAdress(String leftMacAdress) {
		this.leftMacAdress.set(leftMacAdress);
	}

	public String getRightMacAdress() {
		return rightMacAdress.get();
	}

	public String getStartTime() {
		return startTime.get();
	}

	public void setRightMacAdress(String rightMacAdress) {
		this.rightMacAdress.set(rightMacAdress);
	}

	public String getSessionName() {
		return sessionName.get();
	}

	public void setSessionName(String sessionName) {
		this.sessionName.set(sessionName);
	}

	public StringProperty sessionNameProperty() {
		return sessionName;
	}

	public String getState() {
		return state.get();
	}

	public void setState(String state) {
		this.state.set(state);
	}

	public void setRightPort(String rightPort) {
		this.rightPort.set(rightPort);
		;
	}

	public StringProperty stateProperty() {
		return state;
	}

	public String getCodec() {
		return codec.get();
	}

	public void setCodec(String codec) {
		this.codec.set(codec);
	}

	public StringProperty codecProperty() {
		return codec;
	}

	public String getLeftPort() {
		return leftPort.get();
	}

	public String getRightPort() {
		return rightPort.get();
	}

	public String getLeftUri() {
		return leftUri.get();
	}

	public String getRightUri() {
		return rightUri.get();
	}

	public Boolean getIsReadyforAudioPlay() {
		return readyforAudioPlay;
	}

	public void setReadyforAudioPlay(Boolean readyforAudioPlay) {
		this.readyforAudioPlay = readyforAudioPlay;
	}

	public boolean getCodecIsPlayable() {
		return codecIsSupported;
	}

	public Boolean getCodecIsSupported() {
		return codecIsSupported;
	}

	public void setCodecIsSupported(Boolean codecIsSupported) {
		this.codecIsSupported = codecIsSupported;
	}

}
