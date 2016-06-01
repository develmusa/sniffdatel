package ch.sniffdatel.basis.rawDataType;

public class RtpPacket {
	private final String ssrc;
	private final String seqNr;
	private final String timeStamp;
	private final byte[] payload;
	private final int payloadType;
	private final int port;

	public RtpPacket(String ssrc, String seqNr, String timeStamp, byte[] payload, int payloadType, int port) {
		this.ssrc = ssrc;
		this.seqNr = seqNr;
		this.timeStamp = timeStamp;
		this.payload = payload;
		this.payloadType = payloadType;
		this.port = port;
	}

	public String getSsrc() {
		return ssrc;
	}

	public String getSeqNr() {
		return seqNr;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public byte[] getPayload() {
		return payload;
	}

	public int getPayloadType() {
		return payloadType;
	}

	public int getPort() {
		return port;
	}

}
