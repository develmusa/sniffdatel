package ch.sniffdatel.basis.rawDataType;

public class SipPacket {

	private final String callId;
	private final byte[] destIp;
	private final byte[] srcIp;
	private final byte[] destMac;
	private final byte[] srcMac;
	private final String destUri;
	private final String srcUri;
	private final String statusname;
	private final String statuscode;
	private final String method;
	private final SdpPacket sdpPacket;

	public SipPacket(String callId, byte[] destIp, byte[] srcIp, byte[] destMac, byte[] srcMac, String destUri, String srcUri, String statusname, String statuscode, String method, SdpPacket sdpPacket) {
		this.callId = callId;
		this.destIp = destIp;
		this.srcIp = srcIp;
		this.destMac = destMac;
		this.srcMac = srcMac;
		this.destUri = destUri;
		this.srcUri = srcUri;
		this.statusname = statusname;
		this.statuscode = statuscode;
		this.method = method;
		this.sdpPacket = sdpPacket;
	}

	public String getStatuscode() {
		return statuscode;
	}

	public SdpPacket getSdpPacket() {
		return sdpPacket;
	}

	public String getStatusname() {
		return statusname;
	}

	public String getMethod() {
		return method;
	}

	public String getCallId() {
		return callId;
	}

	public byte[] getDestIp() {
		return destIp;
	}

	public byte[] getSrcIp() {
		return srcIp;
	}

	public byte[] getDestMac() {
		return destMac;
	}

	public byte[] getSrcMac() {
		return srcMac;
	}

	public String getSrcUri() {
		return srcUri;
	}

	public String getDestUri() {
		return destUri;
	}

}
