var isPlaying = 0;

function updateMediaDisplay(){
	//Get current radio
	var nameReq = new XMLHttpRequest();
	nameReq.addEventListener('readystatechange', function() {
		if (nameReq.readyState === XMLHttpRequest.DONE && nameReq.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	document.getElementById("mediaName").innerHTML = nameReq.responseText;
   		}
	});
	nameReq.open("GET",ip+"/api/nativemodule/getname");
	nameReq.send(null);

	
	var imgReq = new XMLHttpRequest();
	imgReq.addEventListener('readystatechange', function() {
    	if (imgReq.readyState === XMLHttpRequest.DONE && imgReq.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	document.getElementById("mediaImage").src = imgReq.responseText;
    	}	
    });
	imgReq.open("GET", ip+"/api/nativemodule/getimage");
	imgReq.send(null);
	
	var stateReq = new XMLHttpRequest();
	stateReq.addEventListener('readystatechange', function() {
    	if (stateReq.readyState === XMLHttpRequest.DONE && stateReq.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	if(stateReq.responseText=="true"){
        		setPlayingState();
        	}else{
        		setPauseState();
        	}
    	}	
    });
	stateReq.open("GET", ip+"/api/nativemodule/isplaying");
	stateReq.send(null);

}

function setPlayingState(){
	    isPlaying = 1;
    	document.getElementById("playpauseimg").src="pause.svg";
}

function setPauseState(){
     	document.getElementById("playpauseimg").src="play.svg";
		isPlaying=0;
}

function play(){
    var xmlHttp = new XMLHttpRequest();
    if(isPlaying == 0){
    	xmlHttp.open("HEAD",ip+"/api/nativemodule/play");
    	xmlHttp.send(null);
	    console.log("REQUETE GET à " + ip+"/api/nativemodule/play");
		setPlayingState();
    }else{
    	xmlHttp.open("HEAD",ip+"/api/nativemodule/pause");
    	xmlHttp.send(null);
	    console.log("REQUETE GET à " + ip+"/api/nativemodule/pause");
	    setPauseState();
    }
}

//au lancement de la page web
function initialisation(){
	updateRadio();
	startTime();
}
