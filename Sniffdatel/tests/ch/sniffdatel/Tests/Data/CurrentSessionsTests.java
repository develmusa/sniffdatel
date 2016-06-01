package ch.sniffdatel.Tests.Data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javafx.collections.ObservableList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import ch.sniffdatel.basis.processedData.CurrentSessions;
import ch.sniffdatel.basis.processedData.Session;
import ch.sniffdatel.basis.processedData.SessionParticipant;
import ch.sniffdatel.presentation.model.SessionModel;

public class CurrentSessionsTests {
	private CurrentSessions currentSessions;
	private byte[] ipLeft = new byte[] { (byte) 192, (byte) 168, (byte) 2, (byte) 2 };
	private byte[] ipRight = new byte[] { (byte) 192, (byte) 168, (byte) 2, (byte) 5 };
	private byte[] macLeft = new byte[] { (byte) 00, (byte) 187, (byte) 54, (byte) 222 };
	private byte[] macRight = new byte[] { (byte) 00, (byte) 111, (byte) 32, (byte) 244 };
	private String sipUriLeft = "hans@192.168.2.2";
	private String sipUriRight = "petra@192.168.2.5";

	@Mock
	Session session1;
	@Mock
	Session session2;
	@Mock
	SessionParticipant participantLeft;
	@Mock
	SessionParticipant participantRight;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(participantLeft.getIp()).thenReturn(ipLeft);
		when(participantLeft.getMac()).thenReturn(macLeft);
		when(participantLeft.getPort()).thenReturn(5555);
		when(participantLeft.getSipURI()).thenReturn(sipUriLeft);

		when(participantRight.getIp()).thenReturn(ipRight);
		when(participantRight.getMac()).thenReturn(macRight);
		when(participantRight.getPort()).thenReturn(5556);
		when(participantRight.getSipURI()).thenReturn(sipUriRight);

		when(session1.getCallId()).thenReturn("1234");
		when(session1.getCodec()).thenReturn("PCMU");
		when(session1.getSessionParticipantLeft()).thenReturn(participantLeft);
		when(session1.getSessionParticipantRight()).thenReturn(participantRight);

		when(session2.getCallId()).thenReturn("2345");
		when(session2.getCodec()).thenReturn("PCMU");
		when(session2.getSessionParticipantLeft()).thenReturn(participantLeft);
		when(session2.getSessionParticipantRight()).thenReturn(participantRight);

		currentSessions = new CurrentSessions();
	}

	@After
	public void tearDown() {
		currentSessions.clear();
	}

	@Test
	public void testAddSession() {
		currentSessions.addSession(session1);
		currentSessions.addSession(session2);

		ArrayList<Session> list = currentSessions.getCurrentSessions();
		assertEquals(2, list.size());
	}

	@Test
	public void testfindSessionByCallId() {
		currentSessions.addSession(session1);
		assertEquals(session1, currentSessions.findSessionByCallId("1234"));
		currentSessions.addSession(session2);
		assertEquals(session2, currentSessions.findSessionByCallId("2345"));
	}

	@Test
	public void testfindSessionByCallId_Unsuccessful() {
		currentSessions.addSession(session1);
		assertNull(currentSessions.findSessionByCallId("3333"));
	}

	@Test
	public void testAddSessionData() {
		currentSessions.addSession(session1);
		currentSessions.addSession(session2);
		currentSessions.addSessionData();
		ObservableList<SessionModel> sessionList = currentSessions.getSessionData();

		assertEquals(sessionList.get(0).getSessionName(), "1234");
		assertEquals(sessionList.get(1).getSessionName(), "2345");
	}

	@Test
	public void testIsInList() {
		currentSessions.addSession(session1);
		currentSessions.addSessionData();
		ObservableList<SessionModel> sessionList = currentSessions.getSessionData();
		assertEquals(0, sessionList.get(0).getSessionName().compareTo(session1.getCallId()));
	}

	@Test
	public void testIsInList_NotFound() {
		currentSessions.addSession(session1);
		currentSessions.addSession(session2);
		currentSessions.addSessionData();
		ObservableList<SessionModel> sessionList = currentSessions.getSessionData();
		assertEquals(-1, sessionList.get(0).getSessionName().compareTo(session2.getCallId()));
	}
}
