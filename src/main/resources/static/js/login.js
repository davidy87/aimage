function onLogin() {
    var reqJson = new Object();
    reqJson.email = document.getElementById("email").value;
    reqJson.password = document.getElementById("password").value;

    var xhr = new XMLHttpRequest();

    xhr.open('POST', '/api/users/login', true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var result = xhr.response;
                alert("로그인이 완료되었습니다.");
                location.assign("/");
            } else {
                var errors = xhr.response.errors;

                errors.forEach((error, i) => {
                    document.getElementById(error.field).style.display = "block";
                    document.getElementById(error.field).innerHTML = error.message;
                });
            }
        }
    };
}
