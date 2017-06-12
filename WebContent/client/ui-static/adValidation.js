/**
 * @author Sourabh
 * postAd()
 * Description : 
 * The method is used to call the rest service to POST the ad
 * by taking 3 input paramters from the form i.e. partner_id,
 * duration and ad_content
 */
function postAd() {

	var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8080/SimpleAd/webapp/ad", false);
    xhttp.setRequestHeader("Content-type", "application/json");

    xhttp.onreadystatechange = function () {
    	//var response = xhttp.responseText;
    	openModal("adPoster");
    	messageContent(xhttp.responseText, xhttp.status);
    };
    if(document.getElementById("adPoster")[0].value === ""
    	|| document.getElementById("adPoster")[1].value  === "" 
    	|| document.getElementById("adPoster")[2].value  === "")
    	return false;
    var data = JSON.stringify({"partner_id": document.getElementById("adPoster")[0].value,
    	                      "duration": document.getElementById("adPoster")[1].value,
    	                      "ad_content": document.getElementById("adPoster")[2].value});    
    xhttp.send(data);
}

/**
 * @author Sourabh
 * getAd() 
 * Description : 
 * The method is used to call the rest service to GET the ad
 * based on partner_id it also check if the partner_id value is
 * present is client storage, then it returns from the client storage
 */
function getAd() {
	var xhttp = new XMLHttpRequest();
	var data = document.getElementById("adGetter")[0].value;

	if (data == "")
		return false;
	else if (sessionStorage.getItem(data)) {
		var cache = JSON.parse(sessionStorage.getItem(data));
		var date = new Date(cache.creationTime);
		date.setSeconds(date.getSeconds()+ cache.duration);
		if (date > new Date()) {
			openModal( "adGetter");
			messageContent(sessionStorage.getItem(data), 200);
			return false;
		} else 
			sessionStorage.removeItem(data);
	}

    xhttp.open("GET", "http://localhost:8080/SimpleAd/webapp/ad/" + data , false);
    xhttp.setRequestHeader("Content-type", "application/json");

    xhttp.onreadystatechange = function () {
    	openModal("adGetter");
    	messageContent(xhttp.responseText, xhttp.status);
        //openClildWindow("ADvertise page", 200, 300, xhttp.status, xhttp.responseText)
    };
    xhttp.send(data);
}


/**
 * @author Sourabh
 * getAllAds() 
 * Description : 
 * The method is used to call the rest service to GET all active ad
 */
function getAllActiveAd() {
	url = "active/";
	getAllAd(url);
}

/**
 * @author Sourabh
 * getAllAds() 
 * Description : 
 * The method is used to call the rest service to GET all ad
 */
function getAllAds() {
	url ="";
	getAllAd(url);
}

/**
 * @author Sourabh
 * getAllAd() 
 * Description : 
 * The method is used to call the rest service to GET all the ad
 */
function getAllAd(url) {

	var xhttp = new XMLHttpRequest();;

    xhttp.open("GET", "http://localhost:8080/SimpleAd/webapp/ad/" + url , false);
    xhttp.setRequestHeader("Content-type", "application/json");

    xhttp.onreadystatechange = function () {
    	openModal("adGetterAll");
    	allMessageContent(xhttp.responseText, xhttp.status);
    };
    xhttp.send();
}

/**
 * @author Sourabh
 * openModal() 
 * Description : 
 * The method is used to open a modal window on parent screen to
 * display the message content and error messages
 */
function openModal(formID) {
	var modal = document.getElementById('adMessage');
	var span = document.getElementsByClassName("close")[0];
	
	modal.style.display = "block";
	
	span.onclick = function() {
	    modal.style.display = "none";
	    document.forms[formID].reset();
	    document.getElementById("partnerText").focus();
	}
	
	window.onclick = function(event) {
	    if (event.target == modal) {
	        modal.style.display = "none";
	        document.forms[formID].reset();
	        document.getElementById("partnerText").focus();
	    }
	}
}

/**
 * @author Sourabh
 * messageContent() 
 * Description : 
 * The method is used when status code is 200
 * to display a single message
 */
function messageContent(adContent, statusCode) {
	
	if(statusCode === 200) { //OK
		var response = JSON.parse(adContent);
		var innerText = "";
		if (typeof(Storage) !== "undefined")
			sessionStorage.setItem(response.partner_id, adContent);
		innerText = response.ad_content;
		
		document.getElementById("modalPara").innerHTML = innerText;
		document.getElementById("modalPara").style.color = "blueviolet";
	
	} else {
		messageOtherStatus(adContent, statusCode);
	}
}

/**
 * @author Sourabh
 * allMessageContent() 
 * Description : 
 * The method is used when status code is 200
 * to display all message
 */
function allMessageContent(adContent, statusCode) {
	if(statusCode === 200) { //OK
		var response = JSON.parse(adContent);
		var innerText = "";
			for (i = 0; i < response.length; i++) 
			    innerText += "PartnerID: " + response[i].partner_id 
			                 +"; AdContent: " + response[i].ad_content + "<br>";
		
		document.getElementById("modalPara").innerHTML = innerText;
		document.getElementById("modalPara").style.color = "blueviolet";
	
	} else {
		messageOtherStatus(adContent, statusCode);
	}
}

/**
 * @author Sourabh
 * messageOtherStatus() 
 * Description : 
 * The method is used when status code
 * other than 200 like error and other 
 * generic success code
 */
function messageOtherStatus(adContent, statusCode) {
	var response = JSON.parse(adContent);
	if(statusCode === 201) { //CREATED
		var partnerId = response.partner_id;
		document.getElementById("modalPara").innerHTML = "Advertise Created for Partner: " + partnerId;
		document.getElementById("modalPara").style.color = "blueviolet";
	
	} else if(response.errorCode != undefined) {
		document.getElementById("modalPara").innerHTML = "ERROR: " + response.errorCode +  "<br>" 
		            + response.errorMessage +  "<br>"  + "DOCUMENTATION: " + response.documentation;
		document.getElementById("modalPara").style.color = "red";
	// 400 -- BAD REQUEST
	// 204 -- NO CONTENT
    // 404 -- NO FOUND
	} else {
		document.getElementById("modalPara").innerHTML = "ERROR:  Occured Please contact Administrator";
		document.getElementById("modalPara").style.color = "red";
	}
}

/**
 * @author Sourabh
 * elementfocus() 
 * Description : 
 * The method is used for focusing the first form element
 */
function elementfocus() {
	document.getElementById("partnerText").focus();
}