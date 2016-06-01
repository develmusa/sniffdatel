package ch.sniffdatel.service;

import ch.sniffdatel.basis.processedData.CurrentSessions;
import ch.sniffdatel.basis.processedData.Session;
import ch.sniffdatel.basis.processedData.SessionParticipant;
import ch.sniffdatel.basis.processedData.SipSdpObserver;
import ch.sniffdatel.basis.processedData.SipSdpQueue;
import ch.sniffdatel.basis.rawDataType.SipPacket;

public class SessionHandler extends Thread {

	private SipSdpQueue sipSdpQueue;
	private CurrentSessions currentSessions;

	private SipSdpObserver sipSdpObserver = new SipSdpObserver(this);

	public SessionHandler() {
		this.currentSessions = new CurrentSessions();
		this.sipSdpQueue = SipSdpQueue.getInstance();
		sipSdpQueue.addObserver(sipSdpObserver);
	}

	@Override
	public void run() {
		parseSessions();
	}

	private void parseSessions() {
		SipPacket packet = sipSdpQueue.getPacket();
		if (packet != null) {

			invitePacketCheck(packet);
			ackPacketCheck(packet);
			byePacketCheck(packet);

			getCurrentSessions().addSessionData();
		}
	}

	private void invitePacketCheck(SipPacket packet) {
		if (packet.getMethod().compareTo("INVITE") == 0) {
			if (packet.getStatuscode().compareTo("") == 0)
				createSession(packet);
			else if (packet.getStatuscode().compareTo("200") == 0 && packet.getSdpPacket() != null)
				updateSession(packet.getCallId(), packet.getStatusname(), packet.getStatuscode(), packet.getSdpPacket().getPort());
			else
				updateSession(packet.getCallId(), packet.getStatusname(), packet.getStatuscode());
		}
	}

	private void ackPacketCheck(SipPacket packet) {
		if (packet.getMethod().compareTo("ACK") == 0) {
			Session session = currentSessions.findSessionByCallId(packet.getCallId());
			if (session != null) {
				session.setStatusname("Active Call...");
				session.setMethod("ACK");
			}
		}
	}

	private void byePacketCheck(SipPacket packet) {
		if (packet.getMethod().compareTo("BYE") == 0) {
			Session session = currentSessions.findSessionByCallId(packet.getCallId());
			if (session != null) {
				session.setMethod("BYE");
				session.setStatusname("BYE");
			}
		}
	}

	public void createSession(SipPacket packet) {
		if (currentSessions.findSessionByCallId(packet.getCallId()) != null)
			return;

		SessionParticipant left = new SessionParticipant(packet.getSrcIp(), packet.getSrcMac(), packet.getSrcUri());
		SessionParticipant right = new SessionParticipant(packet.getDestIp(), packet.getDestMac(), packet.getDestUri());

		left.setPort(packet.getSdpPacket().getPort());

		Session session = new Session(packet.getCallId(), packet.getSdpPacket().getCodec(), right, left);
		currentSessions.addSession(session);
	}

	public void updateSession(String callId, String statusname, String statuscode) {
		Session session = currentSessions.findSessionByCallId(callId);
		if (session != null) {
			session.setStatusname(statusname);
			session.setStatuscode(statuscode);
		}
	}

	public void updateSession(String callId, String statusname, String statuscode, int port) {
		Session session = currentSessions.findSessionByCallId(callId);
		if (session != null) {
			session.setStatusname(statusname);
			session.setStatuscode(statuscode);
			session.getSessionParticipantRight().setPort(port);
		}
	}

	public void clearSipSdpQueue() {
		sipSdpQueue.clear();
	}

	public void clearCurrentSessions() {
		currentSessions.clear();
	}

	public CurrentSessions getCurrentSessions() {
		return currentSessions;
	}

}
