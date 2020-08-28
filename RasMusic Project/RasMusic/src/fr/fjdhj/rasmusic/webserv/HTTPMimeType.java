package fr.fjdhj.rasmusic.webserv;

public enum HTTPMimeType {	
	avi("avi", "video/x-msvideo"),
	css("css", "text/css"),
	csv("csv", "text/csv"),
	gif("gif", "image/gif"),
	html("html", "text/html"),
	ico("ico", "image/x-icon"),
	jar("jar", "application/java-archive"),
	js("js", "application/javascript"),
	json("json", "application/json"),
	mpeg("mpeg", "video/mpeg"),
	png("png", "image/png"),
	pdf("pdf", "application/pdf"),
	rar("rar", "application/x-rar-compressed"),
	svg("svg", "image/svg+xml"),
	text("txt", "text/plain"),
	xml("xml", "application/xml"),
	zip("zip", "application/zip"),
	SevenZip("7z", "application/x-7z-compressed");
	
	public final String extension;
	public final String MIMEType;
	
	HTTPMimeType(String string, String string2) {
		extension = string;
		MIMEType = string2;
	}
}
