package fr.fjdhj.rasmusic.module.alarmClock;

public class Alarm {

	private int hour;
	private int minut;
		
	public Alarm(int hour, int minut) {
		this.hour = hour;
		this.minut = minut;
	}
	
	public int getHour() {return hour;}
	public void setHour(int hour) {this.hour = hour;}

	public int getMinut() {return minut;}
	public void setMinut(int minut) {this.minut = minut;}

}
