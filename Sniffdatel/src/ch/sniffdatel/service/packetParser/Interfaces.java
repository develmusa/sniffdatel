package ch.sniffdatel.service.packetParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.format.FormatUtils;

import ch.sniffdatel.presentation.model.Interface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Interfaces {

	private ObservableList<Interface> interfaceData = FXCollections.observableArrayList();
	private static Interfaces interfaceInstance = null;
	private boolean initIfacesExecuted = false;
	private List<PcapIf> allInterface = new ArrayList<>();
	private StringBuilder errbuf = new StringBuilder();
	private int selectetInterfaceNumber = 0;

	private final static Logger LOGGER = Logger.getGlobal();

	private Interfaces() {
	}

	public static Interfaces getInstance() {
		if (interfaceInstance == null) {
			interfaceInstance = new Interfaces();
		}
		return interfaceInstance;
	}

	public void initIfaces() {
		if (initIfacesExecuted == false) {

			try {
				Pcap.findAllDevs(allInterface, errbuf);
			} catch (UnsatisfiedLinkError e) {
				LOGGER.log(Level.SEVERE, e.getMessage() +"\n" + errbuf.toString() + "\n" + "FEHLERBEHEBUNG: Erstellen Sie einen symbolischen Link von Ihrer aktuellen libpcap.so.x.x Library auf libpcap.so.0.9");
			}

			int i = 0;
			for (PcapIf currentIntface : getAllInterface()) {
				String macString = "-";
				String name = "";

				name = System.getProperty("os.name").compareTo("Linux") == 0 ? currentIntface.getName() : currentIntface.getDescription();

				byte[] mac = new byte[] {};
				try {
					mac = currentIntface.getHardwareAddress();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.getMessage());
				}
				List<PcapAddr> ip = currentIntface.getAddresses();
				String ipString = ipCheck(ip);
				if (mac != null)
					macString = FormatUtils.mac(mac);

				interfaceData.add(new Interface(i++, name, ipString, macString));
			}
			initIfacesExecuted = true;

		}
	}

	private String ipCheck(List<PcapAddr> ip) {
		if (!ip.isEmpty()) {
			if (ip.size() > 1)
				return System.getProperty("os.name").compareTo("Linux") == 0 ? FormatUtils.ip(ip.get(1).getAddr().getData()): FormatUtils.ip(ip.get(0).getAddr().getData());
		}
		return "-";
	}

	public ObservableList<Interface> getInterfaceData() {
		return interfaceData;
	}

	public List<PcapIf> getAllInterface() {
		return allInterface;
	}

	public String getInterface() {
		return allInterface.get(selectetInterfaceNumber).getName();
	}

	public PcapIf getActiveInterface() {
		return allInterface.get(selectetInterfaceNumber);
	}

	public void setActiveInterface(int interfaceNumber) {
		this.selectetInterfaceNumber = interfaceNumber;
	}

	public int getSelectetInterfaceNumber() {
		return selectetInterfaceNumber;
	}

}
