package ch.sniffdatel.basis.rawDataType;

public class SdpPacket {

	private final int port;
	private final String codec;
	private final String attributes;

	public SdpPacket(int port, String codec, String attributes) {
		this.port = port;
		this.codec = codec;
		this.attributes = attributes;
	}

	public int getPort() {
		return port;
	}

	public String getCodec() {
		return codec;
	}

	public String getAttributes() {
		return attributes;
	}

}
