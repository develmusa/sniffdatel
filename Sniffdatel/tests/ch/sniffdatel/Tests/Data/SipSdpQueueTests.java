package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.sniffdatel.basis.processedData.SipSdpQueue;
import ch.sniffdatel.basis.rawDataType.SipPacket;

public class SipSdpQueueTests {

	private SipSdpQueue sipSdpQueue = SipSdpQueue.getInstance();

	@Mock
	SipPacket sipPacket1;
	@Mock
	SipPacket sipPacket2;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() {
		sipSdpQueue.clear();
	}

	@Test
	public void testAddPacket() {
		sipSdpQueue.clear();
		sipSdpQueue.addPacket(sipPacket1);
		assertEquals(sipPacket1, sipSdpQueue.getPacket());
		sipSdpQueue.addPacket(sipPacket2);
		assertEquals(sipPacket2, sipSdpQueue.getPacket());
	}

	@Test
	public void testGetPacket() {
		sipSdpQueue.clear();
		sipSdpQueue.addPacket(sipPacket1);
		assertEquals(sipPacket1, sipSdpQueue.getPacket());
		sipSdpQueue.addPacket(sipPacket2);
		assertEquals(sipPacket2, sipSdpQueue.getPacket());
	}

	@Test
	public void testGetPacket_EmptyQueue() {
		assertNull(sipSdpQueue.getPacket());
	}

	@Test
	public void testClear_QueueEmpty() {
		sipSdpQueue.addPacket(sipPacket1);
		sipSdpQueue.clear();
		assertNull(sipSdpQueue.getPacket());
	}
}
