package fr.fjdhj.rasmusic.module.alarmClock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {
	
	SimpleDateFormat hour = new SimpleDateFormat("hh");
	SimpleDateFormat min = new SimpleDateFormat("mm");
	SimpleDateFormat sec = new SimpleDateFormat("ss");
	
	private long timeUpdate = 60000;
	
	private ArrayList<Alarm> alarm = new ArrayList<Alarm>();
	
	public Clock() {timeUpdater();}
	public Clock(long timeUpdate) {
		this.timeUpdate = timeUpdate;
		timeUpdater();
	}

	private void timeUpdater() {
		long start = (60-Integer.parseInt(sec.format(new Date())))*1000;
		Timer t = new Timer(true);
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Check for alarm at "+ new Date());
				Date d = new Date();
				int h = Integer.parseInt(hour.format(d));
				int m = Integer.parseInt(min.format(d));
				for(Alarm a : alarm) {
					if(a.getHour() == h && a.getMinut()== m) {
						System.out.println("Time's up at "+ new Date());
						
					}
				}
				
			}
		}, start, timeUpdate);
		
	}
	
	public long getTimeUpdate() {return timeUpdate;}
	public void setTimeUpdate(long timeUpdate) {this.timeUpdate = timeUpdate;}
	
	

}
