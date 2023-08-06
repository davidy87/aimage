function onSaveImage() {
    if (confirm("이미지를 갤러리에 저장하시겠습니까?") == true) {
        var reqJson = new Object();
        reqJson.prompt = document.getElementById("prompt").innerText;
        reqJson.size = document.getElementById("size").innerText;
        reqJson.url = document.getElementById("url").src;

        var xhr = new XMLHttpRequest();

        xhr.open('POST', "/api/images", true);
        xhr.responseType = "json";
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify(reqJson));

        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var result = xhr.response;
                    alert("이미지를 저장했습니다. 갤러리를 확인해 주세요.");
                    location.replace("/generator");
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
}