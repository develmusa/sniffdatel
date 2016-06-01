package ch.sniffdatel.basis.processedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class JitterBuffer {

	private static JitterBuffer jitterBufferInstance = new JitterBuffer();
	private ConcurrentLinkedQueue<byte[]> leftJitterBufferQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<byte[]> rightJitterBufferQueue = new ConcurrentLinkedQueue<>();

	private JitterBuffer() {
	}

	public static JitterBuffer getInstance() {
		return jitterBufferInstance;
	}

	public synchronized void write(byte[] convertedAudioPacket, String direction) throws Exception {
		if (direction.compareTo("left") == 0)
			leftJitterBufferQueue.add(convertedAudioPacket);
		else if (direction.compareTo("right") == 0)
			rightJitterBufferQueue.add(convertedAudioPacket);
		else
			throw new Exception();

	}

	public byte[] read(String direction) throws Exception {
		if (direction.compareTo("left") == 0)
			return leftJitterBufferQueue.poll();
		else if (direction.compareTo("right") == 0)
			return rightJitterBufferQueue.poll();
		else
			throw new Exception();
	}

	public void clear() {
		leftJitterBufferQueue.clear();
		rightJitterBufferQueue.clear();
	}
}
