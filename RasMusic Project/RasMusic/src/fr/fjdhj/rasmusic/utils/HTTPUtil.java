package fr.fjdhj.rasmusic.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.google.api.client.util.Charsets;

import fr.fjdhj.rasmusic.webserv.HTTPMethod;
import fr.fjdhj.rasmusic.webserv.HTTPMimeType;
import fr.fjdhj.rasmusic.webserv.HTTPRequest;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;
import fr.fjdhj.rasmusic.webserv.HTTPStatusCode;

public class HTTPUtil {
	
	/**
	 * Parse the request to build a HTTPRequest object.
	 * @return null if the message can't be parsed
	 * */
	public static HTTPRequest parseRequest(ArrayList<String> lines, byte[] body) {
		System.out.println("[DEBUG]: On parse la requete.");
		String requestURI = null;
		String method = null;
		HashMap<String,String> headers = new HashMap<String,String>();
		
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			if(i==0) {
				StringTokenizer parse = new StringTokenizer(line);
				method = parse.nextToken().toUpperCase(); 
				try {
					requestURI= URLDecoder.decode(parse.nextToken(),Charsets.UTF_8.name());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else {
				try {
					String key = line.substring(0, line.lastIndexOf(":"));
					String value = line.substring(line.lastIndexOf(":")+2);
					headers.put(key, value);
				}catch(IndexOutOfBoundsException ex) {
					ex.printStackTrace();
				}
			}
		}
		System.out.println("[DEBUG] : method: " + method + " requestURI: " + requestURI + "");
		HTTPMethod methodObj = parseMethod(method);
		if(methodObj == null || requestURI==null) {
			return null;
		}else {
			return new HTTPRequest(methodObj, requestURI, headers, body);
		}
	}
	/**
	 * Parse the request to build a HTTPRequest object from a BufferedInputStream
	 * @return null if the message can't be parsed
	 * */
	public static HTTPRequest parseRequest(BufferedInputStream inSource){
		ArrayList<String> lines;
		byte[] body = new byte[0];
		
		try {
			int val;
			int len = 0;
			int bodyOffset = 0;
			System.out.print("DATA = ");
			StringBuilder builder = new StringBuilder();
			byte[] charBuffer = new byte[1];
			boolean continueReading = true;
			boolean isBody = false;
			while(continueReading  && (val= inSource.read(charBuffer))!= -1) {
				if(isBody && body!=null) {
					//Si on est ici c'est que le content-length existe et qu'il y a un body
					//On lis donc autant que prévu.
					//On peuple le body[] avec l'octet lu à chhaque itération.
					System.out.println("Octet : " + bodyOffset + " val: " + new String(charBuffer) + " len: " + len);
					body[bodyOffset] = charBuffer[0];
					bodyOffset ++;
					if(bodyOffset >= len) {
						continueReading = false;
					}
				}else {
					//On traite l'en tête
					builder.append(new String(charBuffer));
					if(builder.toString().contains("\r\n\r\n") && !isBody) {
						System.out.println("----BODY----");
						//S'execute une unique fois
						isBody = true;
						//On prépare la lecture de l'en tete;
						String header = builder.toString();
						int start = header.indexOf("Content-Length");
						if(start!=-1) {
							//Le content-length existe, on le récupère.
							len = Integer.parseInt(header.substring(header.indexOf(":", start)+2, header.indexOf("\r\n", start)));
							body = new byte[len];
						}else {
							//Il n'existe pas donc on ignore le body
							continueReading = false;
						}
					}else {
						System.out.print(new String(charBuffer));
					}
				}
			}
			//Si on arrive ici, on a lu l'en tete et on a un body vide ou non.
			//On traite l'en tete pour le transformer ensuite.
			String rawHeader = builder.toString();
			String[] lineArray = rawHeader.split("\r\n");
			lines = new ArrayList<String>();
			for(String line : lineArray) {
				lines.add(line);
			}
		} catch (Exception e) {
			//La requete est malformée.
			e.printStackTrace();
			return null;
		}
		return parseRequest(lines, body);
	}
	/**
	 * Parse the given method to build a HTTPMethod object
	 * Example: "GET" into HTTPMetgod.get
	 * @param a String representing the method
	 * @return a HTTPMethod object / null if the parsing fails
	 */
	
	public static HTTPMethod parseMethod(String meth) {
		HTTPMethod[] codes = HTTPMethod.values();

		HTTPMethod method = codes[0];
		for(int i=1;i<codes.length;i++) {
			if(codes[i].message.equals(meth)) {
				method = codes[i];
			}
		}

		return method;

	}
	
	/**
	 * Parse the given code to build a HTTPStatusCode object
	 * null if the parsing fails or 
	 * the HTTPStatusCode corresponding to the code given
	 * @param code
	 * @return HTTPStatusCode or null
	 */
	public static HTTPStatusCode parseStatusCode(String code) {
		HTTPStatusCode[] codes = HTTPStatusCode.values();

		HTTPStatusCode statusCode = null;
		for(int i=0;i<codes.length;i++) {
			if(codes[i].message.equals(code)) {
				statusCode = codes[i];
				break;
			}
		}

		return statusCode;
	}
	
	/**
	 * Parse the given mimetype/extension to build a HTTPMimeType object
	 * null if the parsing fails or
	 * the HTTPMimeType corresponding to the mime string given<br>
	 * Exemple :<br>
	 * svg <b>return</b> HTTPMimeType.svg<br>
	 * text/css <b>return</b> HTTPMimeType.css
	 * @param mime : extension file or the value
	 * @return a HTTPMimeType or a null value
	 */
	public static HTTPMimeType parseMimeType(String mime) {
		HTTPMimeType[] mimeTypes = HTTPMimeType.values();
		HTTPMimeType mimeType = null;
		for(int i=0;i<mimeTypes.length;i++) {
			if(mimeTypes[i].extension.equals(mime) || mimeTypes[i].MIMEType.equals(mime)) {
				mimeType = mimeTypes[i];
				break;
			}
		}
		
		return mimeType;
	}
	
	/**
	 * Serialize the HTTPRequest object through the given outputStream
	 * @param request
	 * @param out
	 * @throws IOException
	 */
	public static void sendHTTPRequest(HTTPRequest request, OutputStream out) throws IOException {
		if(request != null && out != null) {
			BufferedOutputStream dataout = new BufferedOutputStream(out);
			dataout.write(request.getRequestAsByteArray());
			dataout.flush();
		}
	}
	

	/**
	 * Serialize the HTTPResponse object through the given outputStream
	 * @param response
	 * @param out
	 * @throws IOException
	 */
	public static void sendHTTPResponse(HTTPResponse response, OutputStream out) throws IOException {
		if(response != null && out != null) {
			BufferedOutputStream dataout = new BufferedOutputStream(out);
			dataout.write(response.getRequestAsByteArray());
			dataout.flush();
		}
	}
	
	//Copié depuis Server.java from fjdhj
	
	/**
	 * Get the data from the file to upload
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static byte[]readFileData(File file) throws IOException, FileNotFoundException{
		FileInputStream fileIn = null;
		byte[] data = new byte[(int) file.length()];
			fileIn = new FileInputStream(file);
			fileIn.read(data);
			if(fileIn != null)
				try {
					fileIn.close();
				} catch (IOException e) {e.printStackTrace();}
		
		
		return data;
		
	}
	
	//Renvoie le type MIME du fichier (text, vidéo, image, ...)
	public static String MIMEtype(String fileRequested) {
		switch(fileRequested.substring(fileRequested.lastIndexOf("."))) {
		case ".html":
			return "text/html";
		case ".css":
			return "text/css";
		case ".js":
			return "text/javascript";
		case ".svg":
			return "image/svg+xml";
		default:
			return "text/plain";
		}
	}
}
