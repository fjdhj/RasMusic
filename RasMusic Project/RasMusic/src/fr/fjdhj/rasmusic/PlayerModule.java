package fr.fjdhj.rasmusic;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;

public class PlayerModule extends StreamPlayer implements StreamPlayerListener{

	private volatile Thread playerWorker;
	private URL streamURL;
		
	public PlayerModule(URL defaultURL) {
		streamURL = defaultURL;
		this.addStreamPlayerListener(this);
		URLConnection uc;
		try {
			uc = streamURL.openConnection();
			DataInputStream in = new DataInputStream(uc.getInputStream());
			open(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (StreamPlayerException e) {
			e.printStackTrace();
		}
	}
	
	public void setURL(URL url) {
		streamURL = url;
	}
	
	@Override
	public void opened(Object dataSource, Map<String, Object> properties) {
		//System.out.println("Opened");
		
	}

	@Override
	public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {
		//System.out.println("progress");
		
	}

	@Override
	public void statusUpdated(StreamPlayerEvent event) {
		//System.out.println("statusupdated");
		
	}
}
