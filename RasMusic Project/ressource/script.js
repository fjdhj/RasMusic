var isPlaying = 0;

function play(){
    var xmlHttp = new XMLHttpRequest();
    if(isPlaying == 0){
    	document.getElementById("playpauseimg").src="play.svg"
    	xmlHttp.open("GET",ip+"/api/play");
    	xmlHttp.send(null);
    	isPlaying = 1;
	    console.log("REQUETE GET à " + ip+"/api/play");
    }else{
     	document.getElementById("playpauseimg").src="pause.svg"
    	xmlHttp.open("GET",ip+"/api/pause");
    	xmlHttp.send(null);
    	isPlaying = 0;
	    console.log("REQUETE GET à " + ip+"/api/pause");
    }
}

function processRadioList(){

}
