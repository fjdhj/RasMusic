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
	imgReq.addEventListener('readystatechange', function() {
    	if (imgReq.readyState === XMLHttpRequest.DONE && imgReq.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	document.getElementById("radioImage").src = imgReq.responseText;
    	}	
    });
	imgReq.open("GET", ip+"/api/getimage");
	imgReq.send(null);

}

function prev(){
	
}

function next(){
	
}

function selectRadio(radioName){
var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("HEAD",ip+"/api/selectRadio-"+radioName);
	xmlHttp.send(null);
	isPlaying = 1;
    console.log("REQUETE HEAD à " + ip+"/api/selectRadio-"+radioName);
	updateRadio();
}

function play(){
    var xmlHttp = new XMLHttpRequest();
    if(isPlaying == 0){
    	document.getElementById("playpauseimg").src="pause.svg"
    	xmlHttp.open("HEAD",ip+"/api/play");
    	xmlHttp.send(null);
    	isPlaying = 1;
	    console.log("REQUETE GET à " + ip+"/api/play");
    }else{
     	document.getElementById("playpauseimg").src="play.svg"
    	xmlHttp.open("HEAD",ip+"/api/pause");
    	xmlHttp.send(null);
    	isPlaying = 0;
	    console.log("REQUETE GET à " + ip+"/api/pause");
    }
}

function processRadioList(){

}

function getRadioList(){
	var radioList = new XMLHttpRequest();
	radioList.addEventListener('readystatechange', function(){
		if (radioList.readyState === XMLHttpRequest.DONE && radioList.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
        	console.log(radioList);
			var parser = new DOMParser();
			var XMLdocument = parser.parseFromString(radioList.response,"application/xml");
			var radio = XMLdocument.getElementsByTagName('radio');
			var list = "";
			
			list += '<ul class="radioList" padding-left="0">';
			var i;
			for(i = 0; i < radio.length; i++){
				var name = radio[i].getAttribute("name");
				console.log(name);
				list += ' <div class="radioElement" value="'+name+'" >'+'<img src="'+radio[i].getAttribute("icon")+'" width="50" height="50"></img>'+radio[i].getAttribute("name")+'</div>';
			}
			list += '</ul>';
			document.getElementById("radioList").innerHTML = list;
			
			var elements = document.getElementsByClassName('radioElement');
			Array.prototype.forEach.call(elements,function(element){
				element.setAttribute("onclick","selectRadio('"+element.getAttribute("value")+"');");
			});
   		}
	});
	radioList.open("GET", ip+"/api/radiolist");
	radioList.send(null);
	
}

updateRadio();

