package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.sniffdatel.basis.processedData.JitterBuffer;

public class JitterBufferTests {
	private JitterBuffer jitterBuffer = JitterBuffer.getInstance();
	private byte[] samplePacket;

	@Before
	public void setUp() throws InterruptedException {
		samplePacket = new byte[] { -63, -96, -63, -96, -69, -8, -74, -20, -69, -8, -53, -72, -37, -72, -24, -92, -11, -16, -2, -60, 8, 12, 18, 84, 26, 20, 27, 48, 19, 80, 13, 72, 6, 52, 1, 4, 0,
				-72, -4, -28, -6, -84, -7, -80, -3, -5, -24, -14, -36, -21, -8, -21, -8, -43, -48, -69, -8, -74, -20, -48, -60, 0, -56, 17, 56, 18, 84, 19, 80, 22, 36, 35, 16, 43, 48, 46, 4, 21, 8,
				-21, -8, -43, -48, -56, -28, -61, -40, -56, -28, -61, -40, -66, -52, -58, -84, -33, -88, -5, -24, 5, 56 };
	}

	@After
	public void tearDown() {
		jitterBuffer.clear();
	}

	@Test
	public void testWrite() throws Exception {
		jitterBuffer.write(samplePacket, "left");
		assertEquals(samplePacket, jitterBuffer.read("left"));
	}

	@Test
	public void testWrite_LeftDirection() throws Exception {
		jitterBuffer.write(samplePacket, "left");
		assertEquals(samplePacket, jitterBuffer.read("left"));
	}

	@Test
	public void testWrite_RightDirection() throws Exception {
		jitterBuffer.write(samplePacket, "right");
		assertEquals(samplePacket, jitterBuffer.read("right"));
	}

	@Test(expected = Exception.class)
	public void testWrite_EmptyDirection() throws Exception {
		jitterBuffer.write(samplePacket, " ");

	}

	@Test(expected = Exception.class)
	public void testWrite_WrongDirection() throws Exception {
		jitterBuffer.write(samplePacket, "asdf");
	}

	@Test
	public void testMultipleWrite() throws Exception {
		for (int i = 0; i < 100; i++) {
			jitterBuffer.write(samplePacket, "left");
			assertEquals(samplePacket, jitterBuffer.read("left"));
		}

		for (int j = 0; j < 100; j++) {
			jitterBuffer.write(samplePacket, "right");
			assertEquals(samplePacket, jitterBuffer.read("right"));
		}
	}

	@Test
	public void testReadPacket_LeftDirection() throws Exception {
		jitterBuffer.write(samplePacket, "left");
		assertEquals(samplePacket, jitterBuffer.read("left"));

	}

	@Test
	public void testReadPacket_RightDirection() throws Exception {
		jitterBuffer.write(samplePacket, "right");
		assertEquals(samplePacket, jitterBuffer.read("right"));
	}

	@Test(expected = Exception.class)
	public void testReadPacket_EmptyDirection() throws Exception {
		jitterBuffer.write(samplePacket, " ");
	}

	@Test(expected = Exception.class)
	public void testReadPacket_WrongDirection() throws Exception {
		jitterBuffer.write(samplePacket, "asdf");
	}

	@Test
	public void testReadPacket_RightJitterBufferEmpty() throws Exception {
		assertNull(jitterBuffer.read("right"));
	}

	@Test
	public void testReadPacket_LeftJitterBufferEmpty() throws Exception {
		assertNull(jitterBuffer.read("left"));
	}

	@Test
	public void testClear_LeftJitterBuffer() throws Exception {
		jitterBuffer.write(samplePacket, "left");
		jitterBuffer.clear();
		assertNull(jitterBuffer.read("left"));
	}

	@Test
	public void testClear_RightJitterBuffer() throws Exception {
		jitterBuffer.write(samplePacket, "right");
		jitterBuffer.clear();
		assertNull(jitterBuffer.read("right"));
	}

}
