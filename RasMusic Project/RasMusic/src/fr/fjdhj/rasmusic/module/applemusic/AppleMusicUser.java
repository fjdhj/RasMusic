package fr.fjdhj.rasmusic.module.applemusic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import fr.fjdhj.rasmusic.utils.HTTPrequest;

public class AppleMusicUser {
	
	private final String APPLE_MUSIC_LOGIN_URL = "https://idmsa.apple.com/appleauth/auth/signin?isRememberMeEnabled=false";
	private final String APPLE_MUSICVERIFY_URL = "https://idmsa.apple.com/appleauth/auth/verify/trusteddevice/securitycode";
	
	private String id;
	private String password;
	
	public AppleMusicUser() {
		id ="";
		password="";
		
	}
	
	public AppleMusicUser(String id, String password) {
		this.id = id;
		this.password = password;
	}
	
	public void login() {
		Scanner myObj = new Scanner(System.in);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Origin", "www.icloud.com");
		Object[]  obj = HTTPrequest.POST(APPLE_MUSIC_LOGIN_URL, "application/json", "{\"accountName\":\"theo.kalifa@icloud.com\",\"rememberMe\":false}", "", map);
		obj = HTTPrequest.POST(APPLE_MUSIC_LOGIN_URL, "application/json", "{\"accountName\":\"theo.kalifa@icloud.com\",\"rememberMe\":false,\"password\":\"Bocal1844\"}", obj[0].toString().substring(1, obj[0].toString().length()-1), (Map<String, String>) obj[1]);
		System.out.println(obj[0]);
		String code = myObj.nextLine();
		HTTPrequest.POST(APPLE_MUSICVERIFY_URL, "application/json", "{\"securityCode\":{\"code\":\""+code+"\"}}", obj[0].toString().substring(1, obj[0].toString().length()-1), (Map<String, String>) obj[1]);
	}

	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
}
