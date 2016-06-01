package ch.sniffdatel.basis.processedData;

import java.util.Observable;
import java.util.Observer;

import ch.sniffdatel.service.SessionHandler;

public class SipSdpObserver implements Observer {

	private SipSdpQueue ssq = null;
	private SessionHandler sessionHandler;

	public SipSdpObserver(SessionHandler sessionHandler) {
		this.ssq = SipSdpQueue.getInstance();
		this.sessionHandler = sessionHandler;

	}

	public void update(Observable obs, Object obj) {
		if (obs == ssq)
			new Thread(sessionHandler).start();
	}
}
