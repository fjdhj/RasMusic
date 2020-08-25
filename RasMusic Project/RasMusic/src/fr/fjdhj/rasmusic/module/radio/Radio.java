package fr.fjdhj.rasmusic.module.radio;

import java.net.URL;

public class Radio {
	
	private String name;
	private URL url;
	private URL icon;
	
	public Radio(String name, URL url, URL icon){
		setName(name);
		setUrl(url);
		setIcon(icon);
	}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public URL getUrl() {return url;}
	public void setUrl(URL url) {this.url = url;}

	public URL getIcon() {return icon;}
	public void setIcon(URL icon) {this.icon = icon;}

}
