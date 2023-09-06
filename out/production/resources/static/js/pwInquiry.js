function fieldReset() {
    var fieldDiv = document.getElementById("email");
    var fieldErrorDiv = document.getElementById("emailError");

    if (fieldDiv.classList.contains("field-error")) {
        fieldDiv.classList.remove("field-error");
    }

    if (fieldErrorDiv.style.display === "block") {
        fieldErrorDiv.style.display = "none";
    }

    if (document.getElementById("pwInquiryError") === "block") {
        document.getElementById("pwInquiryError").style.display = "none";
    }
}

function onPwInquiry() {
    fieldReset();

    var reqJson = new Object();
    reqJson.email = document.getElementById("email").value;

    var xhr = new XMLHttpRequest();

    xhr.open('GET', "/api/users/pw-inquiry", true);
    xhr.responseType = "json";
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(reqJson));

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var result = xhr.response;
                location.replace("/pw-inquiry/" + result.id + "/new-pw");
            } else {
                var errors = xhr.response.errors;

                errors.forEach((error, i) => {
                    var fieldError = error.field + "Error"
                    document.getElementById(fieldError).style.display = "block";
                    document.getElementById(fieldError).innerHTML = error.message;
                });
            }
        }
    };
}
