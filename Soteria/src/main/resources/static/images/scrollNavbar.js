window.addEventListener("scroll", function (){
    var header = document.querySelector("header");
    var logo = document.getElementById("logo");
    var a_element = document.querySelectorAll("a");
    var subnavbtn = document.getElementsByClassName("subnavbtn");
    header.classList.toggle("sticky", window.scrollY > 0);
    if (window.scrollY > 0) {
        logo.src = "/images/spirelogo-black.png";
        a_element.forEach(function(a){
            a.style.color = "white"
        })
        subnavbtn.forEach(function(sub){
            sub.style.color = "white"
        });
    } else {
        logo.src = "/images/spirelogo.png";
        a_element.forEach(function(a){
            a.style.color = "white"
        });
        subnavbtn.forEach(function(sub){
            sub.style.color = "white"
        });
    }
});