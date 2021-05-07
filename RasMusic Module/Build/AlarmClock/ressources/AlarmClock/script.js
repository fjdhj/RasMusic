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