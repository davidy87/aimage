var userId = document.getElementById("id").innerText;

function onUpdatePw() {
    var reqJson = new Object();
    reqJson.password = document.getElementById("password").value;
    reqJson.confirmPassword = document.getElementById("confirmPassword").value;

    var xhr = new XMLHttpRequest();

    xhr.open('PUT', "/api/users/" + userId + "/new-pw", true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var result = xhr.response;
                alert("비밀번호 변경이 완료되었습니다.");
                location.replace("/");
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
}