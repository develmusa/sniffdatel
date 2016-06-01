package ch.sniffdatel.service.packetParser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;

public class PacketFilter {

	private String name;
	private String description;
	private String filterSettings;
	private int optimize = 0;
	private int netmask;
	private PcapBpfProgram filter = new PcapBpfProgram();
	private final static Logger LOGGER = Logger.getGlobal();

	public PacketFilter() {
		this.name = "UDPFilter";
		this.description = "Filtert UDP";
		this.filterSettings = "udp";
		this.netmask = 0xFFF0000; // 255.255.0.0
		this.optimize = 0;
	}

	public Pcap setFilter(Pcap pcap) {
		if (pcap.compile(filter, filterSettings, optimize, netmask) != Pcap.OK) {
			LOGGER.log(Level.SEVERE, pcap.getErr());
		}

		if (pcap.setFilter(filter) != Pcap.OK) {
			LOGGER.log(Level.SEVERE, pcap.getErr());
		}

		return pcap;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getFilterSettings() {
		return filterSettings;
	}

	public int getNetmask() {
		return netmask;
	}

	public int getOptimize() {
		return optimize;
	}
}
