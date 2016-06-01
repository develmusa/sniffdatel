package ch.sniffdatel.basis.processedData;

import java.util.concurrent.ConcurrentLinkedQueue;

import ch.sniffdatel.basis.rawDataType.RtpPacket;

public class RtpPacketQueue {
	private static RtpPacketQueue rtpPacketQueueInstance = new RtpPacketQueue();
	private ConcurrentLinkedQueue<RtpPacket> leftRtpQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<RtpPacket> rightRtpQueue = new ConcurrentLinkedQueue<>();

	private RtpPacketQueue() {
	}

	public static RtpPacketQueue getInstance() {
		return rtpPacketQueueInstance;
	}

	public void addPacket(RtpPacket rtpPacket, String direction) throws Exception {

		if (direction.compareTo("left") == 0)
			leftRtpQueue.add(rtpPacket);
		else if (direction.compareTo("right") == 0)
			rightRtpQueue.add(rtpPacket);
		else
			throw new Exception();
	}

	public synchronized RtpPacket getPacket(String direction) throws Exception {
		if (direction.compareTo("left") == 0)
			return leftRtpQueue.poll();
		else if (direction.compareTo("right") == 0)
			return rightRtpQueue.poll();
		else
			throw new Exception();
	}

	public void clear() {
		leftRtpQueue.clear();
		rightRtpQueue.clear();

	}
}
