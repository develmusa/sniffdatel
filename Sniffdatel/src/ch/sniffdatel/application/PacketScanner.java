package ch.sniffdatel.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnetpcap.Pcap;
import ch.sniffdatel.service.packetParser.Interfaces;
import ch.sniffdatel.service.packetParser.PacketFilter;
import ch.sniffdatel.service.packetParser.PacketHandler;

public class PacketScanner extends Thread {

	private final PacketHandler packetHandler;
	private final Interfaces intface;
	private final PacketFilter packetFilter;
	private Pcap pcap;

	private final int snaplen = 64 * 1024;
	private final int flags = Pcap.MODE_PROMISCUOUS;
	private final int timeout = 1 * 1000;
	private final StringBuilder errbuf = new StringBuilder();
	private Logger LOGGER = Logger.getGlobal();

	public PacketScanner() {
		this.intface = Interfaces.getInstance();
		this.packetHandler = new PacketHandler();
		this.packetFilter = new PacketFilter();
	}

	@Override
	public void run() {
		startScanner();
	}

	private void startScanner() {
		try {
			packetHandler.setIsRunning(true);
			pcap = Pcap.openLive(intface.getInterface(), snaplen, flags, timeout, errbuf);
			if (pcap == null) {
				LOGGER.log(Level.SEVERE, "Fehler beim oeffnen des Device: " + errbuf.toString());
				return;
			}

			pcap = packetFilter.setFilter(pcap);
			pcap.loop(Pcap.LOOP_INFINITE, packetHandler.getPacketHandler(), pcap);
		} finally {
			pcap.close();
		}
	}

	public void stopScanner() {
		pcap.breakloop();
	}

	public void setActiveSession(String portLeft, String portRight) {
		packetHandler.setActiveSession(portLeft, portRight);

	}

}
