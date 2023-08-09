var imageId =  document.getElementById("imageId").innerText;

function onDeleteImage() {
    if (confirm("해당 이미지를 삭제하시겠습니까?") == true) {
        var xhr = new XMLHttpRequest();

        xhr.open('DELETE', "/api/images/" + imageId, true);
        xhr.responseType = "json";
        xhr.send();

        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var result = xhr.response;
                    document.getElementById("imageCard").remove();
                    alert("이미지가 삭제되었습니다.");
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
}