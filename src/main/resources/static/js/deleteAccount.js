var loginUserId = document.getElementById("loginUserId").innerText;

function onDeleteAccount() {
    var xhr = new XMLHttpRequest();

    xhr.open('DELETE', "/api/users/" + loginUserId, true);
    xhr.responseType = "text";
    xhr.send();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                alert("계정 삭제가 완료되었습니다.");
                location.assign("/");
            } else {
                var errors = xhr.response;

                errors.forEach((error, i) => {
                    alert(error.message);
                });
            }
        }
    };
}