package fr.fjdhj.rasmusic.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HTTPrequest {
	public static Object[] POST(String URL, String contentType, String content, String sentcookie, Map<String, String> customHeader) {
		DataOutputStream wr = null;
		try {
			//On ouvre la connexion
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			
			//On parametre la conexion
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "RasMusic");
			con.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3");
			con.setRequestProperty("Content-Type", contentType);
			con.setRequestProperty("Cookie", sentcookie);
			//On envoie les parametre custome
			if(customHeader != null) for(String key : customHeader.keySet()) {
				con.setRequestProperty(key, customHeader.get(key));
				System.out.println(key + " : " + customHeader.get(key));
			}
			
			//On envoie la requetes
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(content);
			wr.flush();

			
			//On répupère les cookies
			List<String> cookies = con.getHeaderFields().get("Set-Cookie");
			System.out.println(cookies.toString());
			//On récupère le code de réponse du serveur
			int responseCode = con.getResponseCode();
			
			System.out.println("Envoie d'une requetes 'POST' pour l'URL : " + url);
			System.out.println("Corp de la requetes : " + content);
			System.out.println("Code renvoyer par le serveur : " + responseCode);
			
			//On récupère l'entête
			Map<String, String> header = new HashMap<String, String>();
			List<String> value;
			System.out.println("--------------------------------------------------------------------");
			System.out.println("HEADER : ");
			for(String key : con.getHeaderFields().keySet()) {
				value = con.getHeaderFields().get(key);
				System.out.println(key + " : " + value.toString().substring(1, value.toString().length()-1));
				if(key != null && key.startsWith("X-"))
					header.put(key, value.toString().substring(1, value.toString().length()-1));
			}
			
			System.out.println("--------------------------------------------------------------------");
				
			if(responseCode != 200)
				return new Object[] {cookies, header, null};
				
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String output;
			StringBuffer response = new StringBuffer();
			 
			while ((output = in.readLine()) != null) {
			 response.append(output);
			}
			in.close();
			  
			//printing result from response
			System.out.println(response.toString());
			
			return new Object[] {cookies, header, response}; 
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
