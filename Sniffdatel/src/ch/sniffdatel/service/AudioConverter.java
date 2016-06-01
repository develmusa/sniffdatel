package ch.sniffdatel.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import ch.sniffdatel.basis.processedData.JitterBuffer;
import ch.sniffdatel.basis.processedData.RtpPacketQueue;
import ch.sniffdatel.basis.rawDataType.RtpPacket;

public class AudioConverter extends Thread {
	RtpPacketQueue rtpPacketQueue;
	JitterBuffer jitterBuffer;
	private String direction;
	private volatile Boolean isRunning = true;
	private final static Logger LOGGER = Logger.getGlobal();

	// nur g�ltige directions zulassen
	public AudioConverter(String direction) {
		this.direction = direction;
		this.rtpPacketQueue = RtpPacketQueue.getInstance();
		this.jitterBuffer = JitterBuffer.getInstance();
	}

	@Override
	public void run() {
		convertPacketToByte();
	}

	public void convertPacketToByte() {
		while (isRunning) {
			RtpPacket packet;
			try {
				packet = rtpPacketQueue.getPacket(direction);

				if (packet != null) {
					byte[] convertedPayload = convertByteData(packet);
					jitterBuffer.write(convertedPayload, direction);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}

		}
	}

	public byte[] convertByteData(RtpPacket packet) {
		int i = 0;
		int payloadType = packet.getPayloadType();
		byte[] convertedaudio = new byte[packet.getPayload().length * 2];
		for (byte item : packet.getPayload()) {
			int convertint;
			switch (payloadType) {
			case 0:
				convertint = ulaw2linear(item);
				break;
			case 8:
				convertint = alaw2linear(item);
				break;
			default:
				convertint = 0;
				break;
			}
			byte b = (byte) (convertint % 100);
			byte a = (byte) (convertint / 100);
			convertedaudio[i++] = a;
			convertedaudio[i++] = b;
		}
		return convertedaudio;
	}

	public static int ulaw2linear(byte ulaw) {
		int sign, exponent, mantissa, sample;
		final int[] expLut = { 0, 132, 396, 924, 1_980, 4_092, 8_316, 16_764 };

		ulaw = (byte) ~ulaw;
		sign = Byte.toUnsignedInt(ulaw) & 0x80;
		exponent = Byte.toUnsignedInt(ulaw) >> 4 & 0x07;
		mantissa = Byte.toUnsignedInt(ulaw) & 0x0F;
		sample = expLut[exponent] + (mantissa << exponent + 3);
		if (sign != 0)
			sample = -sample;

		return sample;
	}

	public static int alaw2linear(byte alaw) {
		int sample, exponent, sign;

		alaw = (byte) (Byte.toUnsignedInt(alaw) ^ 0x55);
		sign = Byte.toUnsignedInt(alaw) & 0x80;
		sample = (Byte.toUnsignedInt(alaw) & 0x0F) << 4;
		exponent = (Byte.toUnsignedInt(alaw) & 0x70) >> 4;

		if (exponent != 0)
			sample = sample + 0x100 << exponent - 1;
		if (sign != 0)
			sample = -sample;

		return sample;
	}

	public void clearRtpQueue() {
		rtpPacketQueue.clear();
	}

	public void clearJitterBuffer() {
		jitterBuffer.clear();
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

}