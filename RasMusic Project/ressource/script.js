var isPlaying = 0;
var isRadioListing = false;

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
	imgReq.addEventListener('readystatechange', function() {
    	if (imgReq.readyState === XMLHttpRequest.DONE && imgReq.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	document.getElementById("radioImage").src = imgReq.responseText;
    	}	
    });
	imgReq.open("GET", ip+"/api/getimage");
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
	stateReq.open("GET", ip+"/api/isplaying");
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

function selectRadio(radioName){
var xmlHttp = new XMLHttpRequest();
	xmlHttp.addEventListener('readystatechange', function() {
			if (xmlHttp.readyState === XMLHttpRequest.DONE && xmlHttp.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
				updateRadio();
	   		}
		});
    xmlHttp.open("HEAD",ip+"/api/selectRadio-"+radioName);
	xmlHttp.send(null);
    console.log("REQUETE HEAD à " + ip+"/api/selectRadio-"+radioName);
}

function play(){
    var xmlHttp = new XMLHttpRequest();
    if(isPlaying == 0){
    	xmlHttp.open("HEAD",ip+"/api/play");
    	xmlHttp.send(null);
	    console.log("REQUETE GET à " + ip+"/api/play");
		setPlayingState();
    }else{
    	xmlHttp.open("HEAD",ip+"/api/pause");
    	xmlHttp.send(null);
	    console.log("REQUETE GET à " + ip+"/api/pause");
	    setPauseState();
    }
}

function processRadioList(){

}

function getRadioList(){
	var conteneur = document.getElementById("radioList");
	if(isRadioListing){
		conteneur.style.height = "0";
		isRadioListing = false;
	}else{
		var radioList = new XMLHttpRequest();
		radioList.addEventListener('readystatechange', function(){
			if (radioList.readyState === XMLHttpRequest.DONE && radioList.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
	        	console.log(radioList);
				var parser = new DOMParser();
				var XMLdocument = parser.parseFromString(radioList.response,"application/xml");
				var radio = XMLdocument.getElementsByTagName('radio');
				var list = "";
				
				var i;
				for(i = 0; i < radio.length; i++){
					var name = radio[i].getAttribute("name");
					console.log(name);
					list += ' <div class="radioElement" value="'+name+'" >'+'<img src="'+radio[i].getAttribute("icon")+'" class="radioElementImage"></img><b>'+radio[i].getAttribute("name")+'</b></div>';
				}
				document.getElementById("radioList").innerHTML = list;
				
				var elements = document.getElementsByClassName('radioElement');
				Array.prototype.forEach.call(elements,function(element){
					element.setAttribute("onclick","selectRadio('"+element.getAttribute("value")+"');");
				});
				conteneur.style.height = "300px";
	   		}
		});
		radioList.open("GET", ip+"/api/radiolist");
		radioList.send(null);
		isRadioListing = true;
	}
}

function startTime() {
	  var today = new Date();
	  var h = today.getHours();
	  var m = today.getMinutes();
	  var s = today.getSeconds();
	  m = checkTime(m);
	  s = checkTime(s);
	  document.getElementById('horloge').innerHTML = h + ":" + m + ":" + s;
	  var t = setTimeout(startTime, 500);
	}
function checkTime(i) {
  if (i < 10) {i = "0" + i};  // ajoute un zero en face du nombre si <10
  return i;
}

//au lancement de la page web
function initialisation(){
	updateRadio();
	startTime();
}



