package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import ch.sniffdatel.basis.rawDataType.RtpPacket;
import ch.sniffdatel.service.AudioConverter;

public class AudioConverterTests {
	private byte[] samplePayload;
	AudioConverter audioConverter;

	@Mock
	RtpPacket rtpPacket;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		audioConverter = new AudioConverter("left");
	}

	@After
	public void tearDown() {
		audioConverter.clearJitterBuffer();
		audioConverter.clearRtpQueue();
	}

	@Test
	public void testConvertByteData_PayloadType0() {
		samplePayload = new byte[] { -63, -96, -63, -96, -69, -8, -74, -20, -69, -8, -53, -72, -37, -72, -24, -92, -11, -16, -2, -60, 8, 12, 18, 84, 26, 20, 27, 48, 19, 80, 13, 72, 6, 52, 1, 4, 0,
				-72, -4, -28, -6, -84, -7, -80, -3, -5, -24, -14, -36, -21, -8, -21, -8, -43, -48, -69, -8, -74, -20, -48, -60, 0, -56, 17, 56, 18, 84, 19, 80, 22, 36, 35, 16, 43, 48, 46, 4, 21, 8,
				-21, -8, -43, -48, -56, -28, -61, -40, -56, -28, -61, -40, -66, -52, -58, -84, -33, -88, -5, -24, 5, 56 };
		byte[] convertedPayload = { 18, 20, 79, 32, 18, 20, 79, 32, 24, 92, 0, 56, 31, 32, 1, 80, 24, 92, 0, 56, 11, 80, 28, 76, 5, 24, 28, 76, 2, 44, 69, 8, 0, 80, 1, 20, 0, 8, 16, 28, 17, -32, 58,
				-36, 107, -72, -7, -48, -108, -76, 117, -48, -103, -64, -39, 0, 112, -60, -8, -76, 68, -12, -13, -72, -3, -80, -33, -88, -55, 0, -24, -28, -65, -24, 28, 76, 0, 24, 3, 8, 0, 40, 48,
				60, 0, 48, 39, 0, 0, 16, 0, 32, 2, 44, 1, 4, 4, 92, 1, 96, 0, 56, 1, 96, 0, 56, 7, 16, 8, 76, 24, 92, 0, 56, 31, 32, 1, 80, 8, 76, 16, 28, -65, -24, 13, 72, 102, -84, -28, -76, 107,
				-72, -7, -48, 112, -60, -8, -76, 127, -24, -69, -8, -71, -64, 97, -96, -51, -16, -39, 0, -43, -48, -24, -28, 122, -36, 17, -32, 1, 96, 0, 56, 7, 16, 8, 76, 13, 72, 3, 8, 16, 92, 6,
				20, 13, 72, 3, 8, 16, 92, 6, 20, 21, 8, 11, 16, 15, 0, 48, 60, 3, 96, 58, 84, 0, 32, 2, 44, -14, -4, -28, -76 };
		when(rtpPacket.getPayloadType()).thenReturn(0);
		when(rtpPacket.getPayload()).thenReturn(samplePayload);
		for (int i = 0; i < audioConverter.convertByteData(rtpPacket).length; i++) {
			assertEquals(convertedPayload[i], audioConverter.convertByteData(rtpPacket)[i]);
		}

	}

	@Test
	public void testConvertByteData_PayloadType8() {
		samplePayload = new byte[] { -63, -96, -63, -96, -69, -8, -74, -20, -69, -8, -53, -72, -37, -72, -24, -92, -11, -16, -2, -60, 8, 12, 18, 84, 26, 20, 27, 48, 19, 80, 13, 72, 6, 52, 1, 4, 0,
				-72, -4, -28, -6, -84, -7, -80, -3, -5, -24, -14, -36, -21, -8, -21, -8, -43, -48, -69, -8, -74, -20, -48, -60, 0, -56, 17, 56, 18, 84, 19, 80, 22, 36, 35, 16, 43, 48, 46, 4, 21, 8,
				-21, -8, -43, -48, -56, -28, -61, -40, -56, -28, -61, -40, -66, -52, -58, -84, -33, -88, -5, -24, 5, 56 };
		byte[] convertedPayload = { -3, -20, 41, -4, -3, -20, 41, -4, 103, -60, -9, -28, -97, -28, -16, 0, 103, -60, -9, -28, -4, -80, 108, -48, -2, -24, 108, -48, -18, -56, 82, -8, -5, -12, -6, -72,
				-8, -64, -2, -72, 74, 24, 64, 0, 29, 44, 0, 16, 39, 68, 21, 76, 38, 40, 107, 52, 28, 16, 0, 80, 61, 44, 4, 64, 48, 64, 87, 4, 51, 20, 43, 52, 53, 76, 108, -48, -8, 0, -10, -88, -9,
				-92, 0, 0, -8, -96, -107, -52, -7, -68, -9, -60, -18, -56, -7, -36, -1, -44, -19, -20, -9, -28, -19, -20, -9, -28, 0, 0, 0, -80, 103, -60, -9, -28, -97, -28, -16, 0, 0, -80, -2, -72,
				53, 76, -4, -64, 25, 60, -108, 48, 29, 44, 0, 16, 28, 16, 0, 80, 24, 32, -82, 8, -31, 28, 26, 88, 51, 20, 107, 52, 20, 48, 43, 52, 20, 48, 74, 24, -19, -20, -9, -28, 0, 0, 0, -80, -4,
				-64, -10, -88, -3, -52, -2, -8, -4, -64, -10, -88, -3, -52, -2, -8, 118, -24, -4, 0, -3, -4, 0, 0, -1, -60, -40, -96, -9, -60, -18, -56, 40, 96, -108, 48 };
		when(rtpPacket.getPayloadType()).thenReturn(8);
		when(rtpPacket.getPayload()).thenReturn(samplePayload);
		for (int i = 0; i < audioConverter.convertByteData(rtpPacket).length; i++) {
			assertEquals(convertedPayload[i], audioConverter.convertByteData(rtpPacket)[i]);
		}
	}

	@Test
	public void testConvertByteData_PayloadTypeDefault() {
		samplePayload = new byte[] { -63, -96, -63, -96, -69, -8, -74, -20, -69, -8, -53, -72, -37, -72, -24, -92, -11, -16, -2, -60, 8, 12, 18, 84, 26, 20, 27, 48, 19, 80, 13, 72, 6, 52, 1, 4, 0,
				-72, -4, -28, -6, -84, -7, -80, -3, -5, -24, -14, -36, -21, -8, -21, -8, -43, -48, -69, -8, -74, -20, -48, -60, 0, -56, 17, 56, 18, 84, 19, 80, 22, 36, 35, 16, 43, 48, 46, 4, 21, 8,
				-21, -8, -43, -48, -56, -28, -61, -40, -56, -28, -61, -40, -66, -52, -58, -84, -33, -88, -5, -24, 5, 56 };
		byte[] convertedPayload = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		when(rtpPacket.getPayloadType()).thenReturn(7);
		when(rtpPacket.getPayload()).thenReturn(samplePayload);
		for (int i = 0; i < audioConverter.convertByteData(rtpPacket).length; i++) {
			assertEquals(convertedPayload[i], audioConverter.convertByteData(rtpPacket)[i]);
		}
	}

	@Test(expected = Exception.class)
	public void testConvertByteData_Null() {
		rtpPacket = null;
		audioConverter.convertByteData(rtpPacket);
	}

	@Test
	public void testUlaw2linear() {
		assertEquals(AudioConverter.ulaw2linear((byte) 127), 0);
		assertEquals(AudioConverter.ulaw2linear((byte) -128), 32124);
		assertEquals(AudioConverter.ulaw2linear((byte) 0), -32124);
	}

	@Test
	public void testAlaw2linear() {
		assertEquals(AudioConverter.alaw2linear((byte) 127), 832);
		assertEquals(AudioConverter.alaw2linear((byte) -128), -5376);
		assertEquals(AudioConverter.alaw2linear((byte) 0), 5376);
	}

}
