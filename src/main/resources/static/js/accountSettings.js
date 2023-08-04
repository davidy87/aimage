var editDiv = document.getElementById("editDiv");
var editBtn = document.getElementById("editBtn");

var updateDiv = document.getElementById("updateDiv");
var updateBtn = document.getElementById("updateBtn");
var cancelBtn = document.getElementById("cancelBtn");

var usernameUpdateError = document.getElementById("usernameUpdateError");
var nameInput = document.getElementById("nameInput");
var nameInputValue = nameInput.value;

var loginUserId = document.getElementById("loginUserId").innerText;

editBtn.onclick = function () {
    if (updateDiv.style.display === "none") {
        updateDiv.style.display = "block";
    }

    if (editDiv.style.display !== "none") {
        editDiv.style.display = "none";
    }

    if (usernameUpdateError.style.display !== "none") {
        usernameUpdateError.style.display = "none";
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

    nameInput.disabled = true;

    var username = document.getElementById("nameInput").value;
    var reqJson = new Object();

    reqJson.username = username;

    var xhr = new XMLHttpRequest();

    xhr.open('PUT', "/api/users/" + loginUserId + "/new-username", true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var result = xhr.response;
                alert("닉네임 변경이 완료되었습니다.");
                document.getElementById("dropdownMenuButton").innerText = result.username;
            } else {
                var errors = xhr.response;

                errors.forEach((error, i) => {
                    var fieldError = error.field + "Error"
                    document.getElementById(fieldError).style.display = "block";
                    document.getElementById(fieldError).innerHTML = error.message;
                });
            }
        }
    };
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

		xhr.open('PUT', "/api/users/" + loginUserId + "/new-pw", true);
        xhr.responseType = "json";
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify(reqJson));

	    xhr.onreadystatechange = () => {
		    if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var result = xhr.response;
                    alert("비밀번호 변경이 완료되었습니다.");
                    document.getElementById("passwordError").style.display = "none";
                    document.getElementById("confirmPasswordError").style.display = "none";
                } else {
                    var errors = xhr.response;

                    errors.forEach((error, i) => {
                        var fieldError = error.field + "Error"
                        document.getElementById(fieldError).style.display = "block";
                        document.getElementById(fieldError).innerHTML = error.message;
                    });
                }

                document.getElementById("passwordInput").value = null;
                document.getElementById("confirmPasswordInput").value = null;
			}
        };
	});
}
