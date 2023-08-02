function onLogin() {
    var reqJson = new Object();
    reqJson.email = document.getElementById("email").value;
    reqJson.password = document.getElementById("password").value;

    var xhr = new XMLHttpRequest();

    xhr.open('POST', '/api/user/login', true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var result = xhr.response;
                alert("로그인이 완료되었습니다.");
                window.location.replace('/');
            } else {
                var errors = xhr.response;

                errors.forEach((error, i) => {
                    document.getElementById("login.fail").style.display = "block";
                    document.getElementById("login.fail").innerHTML = error.message;
                });
            }
        }
    };
}
