package ch.sniffdatel.basis.processedData;

import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import ch.sniffdatel.basis.rawDataType.SipPacket;

public class SipSdpQueue extends Observable {

	private static SipSdpQueue sipSdpQueueInstance = new SipSdpQueue();
	private ConcurrentLinkedQueue<SipPacket> sipSdpQueue = new ConcurrentLinkedQueue<>();

	private SipSdpQueue() {
	}

	public static SipSdpQueue getInstance() {
		return sipSdpQueueInstance;
	}

	public synchronized void addPacket(SipPacket sipPacket) {
		sipSdpQueue.add(sipPacket);
		setChanged();
		notifyObservers();
	}

	public synchronized SipPacket getPacket() {
		return sipSdpQueue.poll();
	}

	public void clear() {
		sipSdpQueue.clear();
	}
}
