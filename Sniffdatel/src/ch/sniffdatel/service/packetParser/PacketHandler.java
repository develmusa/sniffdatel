package ch.sniffdatel.service.packetParser;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.voip.Rtp;
import org.jnetpcap.protocol.voip.Sdp;
import org.jnetpcap.protocol.voip.Sip;

import ch.sniffdatel.basis.processedData.RtpPacketQueue;
import ch.sniffdatel.basis.processedData.SipSdpQueue;
import ch.sniffdatel.basis.rawDataType.RtpPacket;
import ch.sniffdatel.basis.rawDataType.SdpPacket;
import ch.sniffdatel.basis.rawDataType.SipPacket;

public class PacketHandler {
	private Udp udp = new Udp();
	private Sip sip = new Sip();
	private Sdp sdp = new Sdp();
	private Ip4 ip = new Ip4();
	private Rtp rtp = new Rtp();
	private Ethernet ethernet = new Ethernet();

	private RtpPacketQueue rtpPacketQueue;
	private SipSdpQueue sipSdpQueue;
	
	private int portRight = -1;
	private int portLeft = -1;
	private volatile Boolean isRunning = true;
	private final static Logger LOGGER = Logger.getGlobal();

	public PacketHandler() {
		this.rtpPacketQueue = RtpPacketQueue.getInstance();
		this.sipSdpQueue = SipSdpQueue.getInstance();
	}

	private PcapPacketHandler<Pcap> jpacketHandler = new PcapPacketHandler<Pcap>() {
		public void nextPacket(PcapPacket packet, Pcap pcap) {

			if (packet.hasHeader(sip)) {
				String sipMessage = new String(packet.getHeader(udp).getPayload());

				String callId = patternMatcher("Call-ID: ([0-9A-Za-z]*)", sipMessage, 1);
				String method = patternMatcher("CSeq: [0-9]* ([A-Z]*)", sipMessage, 1);
				String statusname = patternMatcher("SIP\\/2.0 [0-9]* ([]A-Za-z ]*)", sipMessage, 1);
				String statuscode = patternMatcher("SIP\\/2.0 ([0-9]*) []A-Za-z]*", sipMessage, 1);
				String destUri = patternMatcher("To:( \"[a-zA-Z0-9.@ ]*\" | )(<|)sip:([a-zA-Z0-9.@]*)", sipMessage, 3);
				String srcUri = patternMatcher("From:( \"[a-zA-Z0-9.@ ]*\" | )(<|)sip:([a-zA-Z0-9.@]*)", sipMessage, 3);
				byte[] destIp = packet.getHeader(ip).destination();
				byte[] srcIp = packet.getHeader(ip).source();
				byte[] destMac = packet.getHeader(ethernet).destination();
				byte[] srcMac = packet.getHeader(ethernet).source();
				SdpPacket sdpPacket = null;

				if (packet.hasHeader(sdp)) {
					int port = Integer.parseInt(patternMatcher("m=audio ([0-9]*)", sdp.text(), 1));
					String codec = patternMatcher("a=rtpmap:[0-9]* ([A-Za-z-]*)/", sdp.text(), 1);

					sdpPacket = new SdpPacket(port, codec, sdp.text());
				}
				
				SipPacket sipPacket = new SipPacket(callId, destIp, srcIp, destMac, srcMac, destUri, srcUri, statusname, statuscode, method, sdpPacket);

				sipSdpQueue.addPacket(sipPacket);
			}
			if (portLeft != -1 && portRight != -1) {
				try {
					if (packet.getHeader(udp).source() == portLeft)
						rtpPacketQueue.addPacket(parseRtpPacket(packet), "left");
					if (packet.getHeader(udp).source() == portRight)
						rtpPacketQueue.addPacket(parseRtpPacket(packet), "right");
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	};

	public String patternMatcher(String patternString, String attributes, int group) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(attributes);
		if (matcher.find()) {
			return matcher.group(group);
		} else
			return "";
	}

	private RtpPacket parseRtpPacket(PcapPacket packet) {

		JPacket rtpacket = new JMemoryPacket(packet.getHeader(udp).getPayload());
		rtpacket.scan(19);
		String rtpMessage = new String(rtpacket.getHeader(rtp).toString());

		String sequence = patternMatcher("Rtp: *sequence = ([0-9]*)", rtpMessage, 1);
		String timestamp = patternMatcher("Rtp: *timestamp = ([0-9]*)", rtpMessage, 1);
		String ssrc = patternMatcher("Rtp: *ssrc = ([0-9]*)", rtpMessage, 1);
		int payloadType = rtpacket.getHeader(rtp).type();

		return new RtpPacket(ssrc, sequence, timestamp, rtpacket.getHeader(rtp).getPayload(), payloadType, packet.getHeader(udp).source());
	}

	public void setActiveSession(String portLeft, String portRight) {
		this.portLeft = Integer.parseInt(portLeft);
		this.portRight = Integer.parseInt(portRight);
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	public PcapPacketHandler<Pcap> getPacketHandler() {
		return jpacketHandler;
	}
}
