var editDiv = document.getElementById("editDiv");
var editBtn = document.getElementById("editBtn");

var updateDiv = document.getElementById("updateDiv");
var updateBtn = document.getElementById("updateBtn");
var cancelBtn = document.getElementById("cancelBtn");

var nameInput = document.getElementById("nameInput");
var emailInput = document.getElementById("emailInput");

var nameInputValue = nameInput.value;
var emailInputValue = emailInput.value;

editBtn.onclick = function () {
    if (updateDiv.style.display == "none") {
        updateDiv.style.display = "block";
    }

    if (editDiv.style.display !== "none") {
        editDiv.style.display = "none";
    }

    nameInput.disabled = false;
    emailInput.disabled = false;
};

updateBtn.onclick = function () {
    if (updateDiv.style.display !== "none") {
        updateDiv.style.display = "none";
    }

    if (editDiv.style.display == "none") {
        editDiv.style.display = "block";
    }

    nameInputValue = nameInput.value;
    emailInputValue = emailInput.value;

    nameInput.disabled = true;
    emailInput.disabled = true;
};

cancelBtn.onclick = function () {
    if (updateDiv.style.display !== "none") {
        updateDiv.style.display = "none";
    }

    if (editDiv.style.display == "none") {
        editDiv.style.display = "block";
    }

    nameInput.value = nameInputValue;
    emailInput.value = emailInputValue;

    nameInput.disabled = true;
    emailInput.disabled = true;
};