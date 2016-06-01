package ch.sniffdatel.basis.processedData;

import java.util.ArrayList;
import org.jnetpcap.packet.format.FormatUtils;

import ch.sniffdatel.presentation.model.SessionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentSessions {

	private ArrayList<Session> currentSessions = new ArrayList<Session>();
	private volatile ObservableList<SessionModel> sessionData = FXCollections.observableArrayList();

	public void addSession(Session session) {
		currentSessions.add(session);
	}

	public synchronized void addSessionData() {

		for (Session session : currentSessions) {
			String sessionName = session.getCallId();
			String leftIpAdress = FormatUtils.ip(session.getSessionParticipantLeft().getIp());
			String rightIpAdress = FormatUtils.ip(session.getSessionParticipantRight().getIp());
			String leftMacAdress = FormatUtils.mac(session.getSessionParticipantLeft().getMac());
			String rightMacAdress = FormatUtils.mac(session.getSessionParticipantRight().getMac());
			String leftUri = session.getSessionParticipantLeft().getSipURI();
			String rightUri = session.getSessionParticipantRight().getSipURI();
			String leftPort = String.valueOf(session.getSessionParticipantLeft().getPort());
			String rightPort = String.valueOf(session.getSessionParticipantRight().getPort());
			String startTime = session.getStartTime();
			Boolean isReady = session.readyToPlay();
			Boolean isSupported = session.codecIsSupported();

			String status = session.getStatusname();
			String codec = session.getCodec();
			int index = isInList(sessionName);
			if (index >= 0) {
				sessionData.get(index).setState(status);
				sessionData.get(index).setReadyforAudioPlay(session.readyToPlay());
				sessionData.get(index).setRightPort(rightPort);
			} else {
				sessionData.add(new SessionModel(sessionName, leftIpAdress, rightIpAdress, status, codec, leftMacAdress, rightMacAdress, leftUri, rightUri, leftPort, rightPort, startTime, isReady, isSupported));
			}
		}

	}

	private int isInList(String sessionName) {
		for (SessionModel sessionModel : sessionData) {
			if (sessionModel.getSessionName().compareTo(sessionName) == 0)
				return sessionData.indexOf(sessionModel);
		}
		return -1;
	}

	public Session findSessionByCallId(String callId) {
		for (Session session : currentSessions) {
			if (session.getCallId().compareTo(callId) == 0)
				return session;
		}
		return null;
	}

	public void clear() {
		currentSessions.clear();
	}

	public ObservableList<SessionModel> getSessionData() {
		return sessionData;
	}

	public ArrayList<Session> getCurrentSessions() {
		return currentSessions;
	}
}
