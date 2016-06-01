package ch.sniffdatel.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import ch.sniffdatel.basis.processedData.JitterBuffer;

public class AudioPlayer extends Thread {

	private JitterBuffer jitterBuffer;
	private volatile Boolean isRunning = true;
	private String direction;
	private final static Logger LOGGER = Logger.getGlobal();

	public AudioPlayer(String direction) {
		this.direction = direction;
		this.jitterBuffer = JitterBuffer.getInstance();
	}

	@Override
	public void run() {
		play();
	}

	private void play() {
		try {
			AudioFormat formatpcm = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0f, 16, 1, 2, 8000.0f, true);
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, formatpcm);
			SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

			speakers.open();
			speakers.start();
			while (isRunning) {

				byte[] audio = jitterBuffer.read(direction);
				if (audio != null) {
					synchronized (this) {
						speakers.write(audio, 0, audio.length);
					}
					;
				}
			}
			speakers.drain();
			speakers.close();
		} catch (LineUnavailableException e) {
			LOGGER.log(Level.SEVERE, e.getMessage() + "\n\n Not supported soundcard.\n");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}

	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

}
