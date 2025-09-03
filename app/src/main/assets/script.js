// Designed by:  Mauricio Bucardo
// Original image:
// https://dribbble.com/shots/5619509-Animated-Tab-Bar

"use strict";

const body = document.body;
//const bgColorsBody = ["transparent", "transparent", "transparent", "transparent", "transparent"];
const menu = body.querySelector(".menu");
const menuItems = menu.querySelectorAll(".menu__item");
const items = document.querySelectorAll(".menu__item");
const menuBorder = menu.querySelector(".menu__border");
let activeItem = menu.querySelector(".active");


function clickItem(item, index) {

    menu.style.removeProperty("--timeOut");

    if (activeItem == item) return;

    if (activeItem) {
        activeItem.classList.remove("active");
    }


    item.classList.add("active");
    //body.style.backgroundColor = bgColorsBody[index];
    activeItem = item;
    offsetMenuBorder(activeItem, menuBorder);


}

function offsetMenuBorder(element, menuBorder) {

    const offsetActiveItem = element.getBoundingClientRect();
    const left = Math.floor(offsetActiveItem.left - menu.offsetLeft - (menuBorder.offsetWidth - offsetActiveItem.width) / 2) + "px";
    menuBorder.style.transform = `translate3d(${left}, 0 , 0)`;

}

offsetMenuBorder(activeItem, menuBorder);

menuItems.forEach((item, index) => {

    item.addEventListener("click", () => clickItem(item, index));

})

window.addEventListener("resize", () => {
    offsetMenuBorder(activeItem, menuBorder);
    menu.style.setProperty("--timeOut", "none");
});


items.forEach((item, index) => {
    item.addEventListener("click", () => {
        Android.onMenuClick(index); // ¡Aquí Android recibe el clic!
    });
});

//Debug
// Solo para pruebas en navegador
if (typeof Android === 'undefined') {
    console.log("Simulando Android para desarrollo en navegador");
    Android = {
        onMenuClick: function (index) {
            console.log("Simulación: clic en botón", index);
        }
    };
}
//Debug-end  