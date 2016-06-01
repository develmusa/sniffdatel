package ch.sniffdatel.basis.processedData;

import java.time.LocalTime;

public class Session {
	private final String callId;
	private String statusname = "";
	private String statuscode = "";
	private String method;
	private final String codec;
	private final String startTime;


	private SessionParticipant sessionParticipantRight;
	private SessionParticipant sessionParticipantLeft;

	public Session(String callId, String codec, SessionParticipant sessionParticipantRight, SessionParticipant sessionParticipantLeft) {
		this.callId = callId;
		this.codec = codec;
		this.sessionParticipantLeft = sessionParticipantLeft;
		this.sessionParticipantRight = sessionParticipantRight;
		startTime = LocalTime.now().toString().split("\\.")[0];
	}

	protected Boolean codecIsSupported() {
		return (codec.compareTo("PCMU") == 0 || codec.compareTo("PCMA") == 0 ||
				codec.compareTo("pcmu") == 0 || codec.compareTo("pcma") == 0 ) ? true : false;
	}

	protected Boolean readyToPlay() {
		return (statusname.compareTo("Active Call...") == 0 || statusname.compareTo("OK") == 0) ? true : false;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public String getCallId() {
		return callId;
	}

	public String getCodec() {
		return codec;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStatusname() {
		return statusname;
	}

	public SessionParticipant getSessionParticipantRight() {
		return sessionParticipantRight;
	}

	public SessionParticipant getSessionParticipantLeft() {
		return sessionParticipantLeft;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
}
