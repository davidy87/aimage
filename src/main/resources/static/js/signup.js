var fields = ["username", "email", "password", "confirmPassword"];

function fieldReset() {
    fields.forEach((field, i) => {
        var fieldError = field + "Error";
        var fieldDiv = document.getElementById(field);
        var fieldErrorDiv = document.getElementById(fieldError);

        if (fieldDiv.classList.contains("field-error")) {
            fieldDiv.classList.remove("field-error");
        }

        if (fieldErrorDiv.style.display === "block") {
            fieldErrorDiv.style.display = "none";
        }
    });
}

function onSignup() {
    fieldReset();

    var reqJson = new Object();
    reqJson.username = document.getElementById("username").value;
    reqJson.email = document.getElementById("email").value;
    reqJson.password = document.getElementById("password").value;
    reqJson.confirmPassword = document.getElementById("confirmPassword").value;

    var xhr = new XMLHttpRequest();

    xhr.open('POST', '/api/user', true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var result = xhr.response;
                alert("회원가입이 완료되었습니다.");
                window.location.replace('/');
            } else {
                var errors = xhr.response;

                errors.forEach((error, i) => {
                    var fieldError = error.field + "Error";
                    document.getElementById(error.field).classList.add("field-error");
                    document.getElementById(fieldError).style.display = "block";
                    document.getElementById(fieldError).innerHTML = error.message;
                });
            }
        }
    };
}