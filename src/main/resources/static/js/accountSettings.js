var editDiv = document.getElementById("editDiv");
var editBtn = document.getElementById("editBtn");

var updateDiv = document.getElementById("updateDiv");
var updateBtn = document.getElementById("updateBtn");
var cancelBtn = document.getElementById("cancelBtn");

var nameInput = document.getElementById("nameInput");
var nameInputValue = nameInput.value;

editBtn.onclick = function () {
    if (updateDiv.style.display == "none") {
        updateDiv.style.display = "block";
    }

    if (editDiv.style.display !== "none") {
        editDiv.style.display = "none";
    }

    nameInput.disabled = false;
};

updateBtn.onclick = function () {
    if (updateDiv.style.display !== "none") {
        updateDiv.style.display = "none";
    }

    if (editDiv.style.display == "none") {
        editDiv.style.display = "block";
    }

//    nameInputValue = document.getElementById("nameInput").value;
    nameInput.disabled = true;

    var username = document.getElementById("nameInput").value;

    var reqJson = new Object();
    reqJson.username = username;

    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200 || xhr.status === 302) {
                var result = xhr.response;
                alert(result.message);
                document.getElementById("dropdownMenuButton").innerText = result.field;
            } else {
                var result = xhr.response;
                document.getElementById("nameUpdateError").innerText = result.message;
            }
        }
    };

    xhr.open('POST', '/userInfo/editUser', true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));
};

cancelBtn.onclick = function () {
    if (updateDiv.style.display !== "none") {
        updateDiv.style.display = "none";
    }

    if (editDiv.style.display == "none") {
        editDiv.style.display = "block";
    }

    nameInput.value = nameInputValue;
    nameInput.disabled = true;
};

window.onload = function() {
	document.getElementById("pwUpdateBtn").addEventListener('click', () => {
		var password = document.getElementById("passwordInput").value;
		var confirmPassword = document.getElementById("confirmPasswordInput").value;

		var reqJson = new Object();
		reqJson.password = password;
		reqJson.confirmPassword = confirmPassword;

		var xhr = new XMLHttpRequest();

	    xhr.onreadystatechange = () => {
		    if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200 || xhr.status === 302) {
                    var result = xhr.response;
                    alert(result.message);
                    document.getElementById("pwError").style.display = "none";
                    document.getElementById("confirmPwError").style.display = "none";
                } else {
                    var result = xhr.response;
//                    alert(JSON.stringify(result));
                    document.getElementById(result.code).style.display = "block";
                    document.getElementById(result.code).innerText = result.message;
//                    document.getElementById("pwUpdateError").innerText = result.message;
                }

                document.getElementById("passwordInput").value = null;
                document.getElementById("confirmPasswordInput").value = null;
			}
        };

        xhr.open('POST', '/userInfo/editPw', true);
        xhr.responseType = "json";
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify(reqJson));
	});
}
