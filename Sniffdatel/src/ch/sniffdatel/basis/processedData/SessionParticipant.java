package ch.sniffdatel.basis.processedData;

public class SessionParticipant {
	private final byte[] ip;
	private final byte[] mac;
	private final String sipURI;
	private int port;

	public SessionParticipant(byte[] ip, byte[] mac, String sipUri) {
		this.ip = ip;
		this.mac = mac;
		this.sipURI = sipUri;
	}

	public byte[] getMac() {
		return mac;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public byte[] getIp() {
		return ip;
	}

	public String getSipURI() {
		return sipURI;
	}

	public int getPort() {
		return port;
	}
}
