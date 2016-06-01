package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import org.jnetpcap.Pcap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.sniffdatel.service.packetParser.PacketFilter;

public class PacketFilterTests {

	private PacketFilter packetFilter;

	@Mock
	Pcap pcap;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		packetFilter = new PacketFilter();

	}

	@Test
	public void testSetFilterSuccessful() {
		assertEquals(pcap, packetFilter.setFilter(pcap));
	}
}
