var isPlaying = 0;

function updateRadio(){
	//Get current radio
	var nameReq = new XMLHttpRequest();
	nameReq.addEventListener('readystatechange', function() {
		if (nameReq.readyState === XMLHttpRequest.DONE && nameReq.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	document.getElementById("radioName").innerHTML = nameReq.responseText;
   		}
	});
	nameReq.open("GET",ip+"/api/getname");
	nameReq.send(null);

	
	var imgReq = new XMLHttpRequest();
	imgReq.open("GET", ip+"/api/getimage");
	imgReq.send(null);
	imgReq.addEventListener('readystatechange', function() {
    	if (xhr.readyState === XMLHttpRequest.DONE) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        // Votre code…
    	}	
    });
}
updateRadio();

function play(){
    var xmlHttp = new XMLHttpRequest();
    if(isPlaying == 0){
    	document.getElementById("playpauseimg").src="play.svg"
    	xmlHttp.open("HEAD",ip+"/api/play");
    	xmlHttp.send(null);
    	isPlaying = 1;
	    console.log("REQUETE GET à " + ip+"/api/play");
    }else{
     	document.getElementById("playpauseimg").src="pause.svg"
    	xmlHttp.open("HEAD",ip+"/api/pause");
    	xmlHttp.send(null);
    	isPlaying = 0;
	    console.log("REQUETE GET à " + ip+"/api/pause");
    }
}

function processRadioList(){

}
