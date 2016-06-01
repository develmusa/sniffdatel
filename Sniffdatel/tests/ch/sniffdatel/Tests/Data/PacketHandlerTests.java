package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import org.jnetpcap.packet.PcapPacket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.sniffdatel.service.packetParser.PacketHandler;

public class PacketHandlerTests {
	private PacketHandler packetHandler;

	@Mock
	PcapPacket pcapPacket;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		packetHandler = new PacketHandler();
	}

	@Test(expected = NumberFormatException.class)
	public void testSetActiveSession_NoValidPortNumberLeft() {
		packetHandler.setActiveSession("asdf5544", "3355");
	}

	@Test(expected = NumberFormatException.class)
	public void testSetActiveSession_NoValidPortNumberRight() {
		packetHandler.setActiveSession("7754", "asdf3355");
	}

	@Test(expected = NumberFormatException.class)
	public void testSetActiveSession_NoValidPortNumberBoth() {
		packetHandler.setActiveSession("asdf754", "asdf3355");
	}

	@Test(expected = NumberFormatException.class)
	public void testSetActiveSession_EmptyPortNumberLeft() {
		packetHandler.setActiveSession("", "3355");
	}

	@Test(expected = NumberFormatException.class)
	public void testSetActiveSession_EmptyPortNumberRight() {
		packetHandler.setActiveSession("5566", "");
	}

	@Test(expected = NumberFormatException.class)
	public void testSetActiveSession_EmptyPortNumberBoth() {
		packetHandler.setActiveSession("", "");
	}

	@Test
	public void testPatternMatcher_Match() {
		assertEquals("123", packetHandler.patternMatcher("sample text ([0-9]*)", "sample text 123", 1));
	}

	@Test
	public void testPatternMatcher_NoMatch() {
		assertEquals("", packetHandler.patternMatcher("sample text ([0-9]*)", "sampletext 123", 1));

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testPatternMatcher_NoSuchGroup() {
		packetHandler.patternMatcher("sample text ([0-9]*)", "sample text 123", -11);
		packetHandler.patternMatcher("sample text ([0-9]*)", "sample text 123", 55);
	}
}
