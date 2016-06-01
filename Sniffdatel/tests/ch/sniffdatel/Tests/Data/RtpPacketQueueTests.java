package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.sniffdatel.basis.processedData.RtpPacketQueue;
import ch.sniffdatel.basis.rawDataType.RtpPacket;

public class RtpPacketQueueTests {
	RtpPacketQueue rtpPacketQueue = RtpPacketQueue.getInstance();

	@Mock
	RtpPacket rtpPacket1;
	@Mock
	RtpPacket rtpPacket2;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() {
		rtpPacketQueue.clear();
	}

	@Test
	public void testAddPacket_RightDirection() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket2, "right");
		assertEquals(rtpPacket2, rtpPacketQueue.getPacket("right"));
	}

	@Test
	public void testAddPacket_LeftDirection() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket1, "left");
		assertEquals(rtpPacket1, rtpPacketQueue.getPacket("left"));
	}

	@Test(expected = Exception.class)
	public void testAddPacket_EmptyDirection() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket1, "");
	}

	@Test(expected = Exception.class)
	public void testAddPacket_WrongDirection() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket1, "asdf");
	}

	@Test
	public void testGetPacket_Left() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket1, "left");
		rtpPacketQueue.addPacket(rtpPacket2, "right");
		assertEquals(rtpPacket1, rtpPacketQueue.getPacket("left"));
	}

	@Test
	public void testGetPacket_Right() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket2, "right");
		rtpPacketQueue.addPacket(rtpPacket1, "left");
		assertEquals(rtpPacket2, rtpPacketQueue.getPacket("right"));
	}

	@Test
	public void testGetPacket_EmptyQueueLeft() throws Exception {
		assertNull(rtpPacketQueue.getPacket("left"));
	}

	@Test
	public void testGetPacket_EmptyQueueRight() throws Exception {
		assertNull(rtpPacketQueue.getPacket("right"));
	}

	@Test(expected = Exception.class)
	public void testgetPacket_WrongDirection() throws Exception {
		rtpPacketQueue.getPacket("asdf");
	}

	@Test(expected = Exception.class)
	public void testgetPacket_EmptyDirection() throws Exception {
		rtpPacketQueue.getPacket("");
	}

	@Test
	public void testClear_LeftQueue() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket1, "left");
		rtpPacketQueue.clear();
		assertNull(rtpPacketQueue.getPacket("left"));
	}

	@Test
	public void testClear_RightQueue() throws Exception {
		rtpPacketQueue.addPacket(rtpPacket1, "right");
		rtpPacketQueue.clear();
		assertNull(rtpPacketQueue.getPacket("right"));
	}

}
