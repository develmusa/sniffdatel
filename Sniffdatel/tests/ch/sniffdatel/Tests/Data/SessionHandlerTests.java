package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import ch.sniffdatel.basis.processedData.Session;
import ch.sniffdatel.basis.processedData.SessionParticipant;
import ch.sniffdatel.basis.processedData.SipSdpQueue;
import ch.sniffdatel.basis.rawDataType.SdpPacket;
import ch.sniffdatel.basis.rawDataType.SipPacket;
import ch.sniffdatel.service.SessionHandler;

public class SessionHandlerTests {
	@Mock
	SipPacket sipPacket;
	@Mock
	SipSdpQueue sipSdpQueue;
	@Mock
	SdpPacket sdpPacket;
	@Mock
	ArrayList<Session> currentSessionsList;

	private SessionHandler sessionHandler;
	private Session session;

	@Before
	public void setUp() {
		SessionParticipant sessionParticipantLeft = new SessionParticipant(new byte[] {}, new byte[] {}, "");
		SessionParticipant sessionParticipantRight = new SessionParticipant(new byte[] {}, new byte[] {}, "");
		sessionHandler = new SessionHandler();
		session = new Session("1234", "ULAW", sessionParticipantRight, sessionParticipantLeft);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateSession_ExistingSession() {
		when(sipPacket.getCallId()).thenReturn("1234");
		sessionHandler.getCurrentSessions().addSession(session);
		sessionHandler.createSession(sipPacket);
		assertEquals(1, sessionHandler.getCurrentSessions().getCurrentSessions().size());
	}

	@Test
	public void testCreateSession_NewSession() {
		when(sipPacket.getSrcIp()).thenReturn(new byte[] {});
		when(sipPacket.getSrcMac()).thenReturn(new byte[] {});
		when(sipPacket.getSrcUri()).thenReturn("");
		when(sipPacket.getDestIp()).thenReturn(new byte[] {});
		when(sipPacket.getDestMac()).thenReturn(new byte[] {});
		when(sipPacket.getDestUri()).thenReturn("");
		when(sipPacket.getSdpPacket()).thenReturn(sdpPacket);
		when(sdpPacket.getPort()).thenReturn(5000);
		when(sipPacket.getCallId()).thenReturn("5678");
		assertEquals(0, sessionHandler.getCurrentSessions().getCurrentSessions().size());
		sessionHandler.createSession(sipPacket);
		assertEquals(1, sessionHandler.getCurrentSessions().getCurrentSessions().size());
	}

	@Test
	public void testUpdateSession_StatusNameStatusCode() {
		session.setStatusname("nameBefore");
		session.setStatuscode("codeBefore");
		sessionHandler.getCurrentSessions().addSession(session);
		assertEquals("nameBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertEquals("codeBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
		sessionHandler.updateSession("1234", "nameAfter", "codeAfter");
		assertEquals("nameAfter", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertEquals("codeAfter", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
	}

	@Test
	public void testUpdateSession_WrongCallIdShortMethod() {
		session.setStatusname("nameBefore");
		session.setStatuscode("codeBefore");
		sessionHandler.getCurrentSessions().addSession(session);
		assertEquals("nameBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertEquals("codeBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
		sessionHandler.updateSession("1235", "nameAfter", "codeAfter");
		assertEquals("nameBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertEquals("codeBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
		assertNotEquals("nameAfter", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertNotEquals("codeAfter", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
	}

	@Test
	public void testUpdateSession_StatusNameStatusCodePort() {
		session.setStatusname("nameBefore");
		session.setStatuscode("codeBefore");
		sessionHandler.getCurrentSessions().addSession(session);
		assertEquals("nameBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertEquals("codeBefore", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
		sessionHandler.updateSession("1234", "nameAfter", "codeAfter", 5500);
		assertEquals("nameAfter", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatusname());
		assertEquals("codeAfter", sessionHandler.getCurrentSessions().getCurrentSessions().get(0).getStatuscode());
	}
}
