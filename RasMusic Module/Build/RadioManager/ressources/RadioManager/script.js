var isRadioListing = false;

function selectRadio(radioName){
var xmlHttp = new XMLHttpRequest();
	xmlHttp.addEventListener('readystatechange', function() {
			if (xmlHttp.readyState === XMLHttpRequest.DONE && xmlHttp.status==200) { // La constante DONE appartient à l'objet XMLHttpRequest, elle n'est pas globale
				updateMediaDisplay();
	   		}
		});
    xmlHttp.open("HEAD",ip+"/api/RadioManager/selectRadio-"+radioName);
	xmlHttp.send(null);
    console.log("REQUETE HEAD à " + ip+"/api/RadioManager/selectRadio-"+radioName);
}

function getRadioList(){
	var conteneur = document.getElementById("radioList");
	if(isRadioListing){
		conteneur.style.height = "0";
		isRadioListing = false;
	}else{
		var radioList = new XMLHttpRequest();
		radioList.addEventListener('readystatechange', function(){
			console.log(radioList.status);
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
		radioList.open("GET", ip+"/api/RadioManager/radiolist");
		radioList.send(null);
		isRadioListing = true;
	}
}