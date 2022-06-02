console.log("Scrpit file");
const search=()=>{

let query=$("#search-input").val();

if(query==""){
$(".search-result").hide();
}

else{

let url=`http://localhost:8081/search/${query}`;
fetch(url).then((response)=>{
return response.json();

}
)

.then((data)=>{
    console.log(data);
    let text=`<div class='list-group'>`
data.forEach((contact)=>{
    console.log(contact.name);
text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-action'>${contact.name}</a>`


});


text+=`<div>`
$(".search-result").html(text);
$(".search-result").show();

});

$(".search-result").show();
}

};










const inputs =document.querySelectorAll('.login-input');

function focusFunc(){

let parent = this.parentNode.parentNode;

parent.classList.add('focus');



};



function blurFunc(){
let parent = this.parentNode.parentNode;

if(this.value==""){
parent.classList.remove('focus');
}



};

inputs.forEach(input=>{
input.addEventListener('focus',focusFunc);
input.addEventListener('blur',blurFunc);

});


const navSlide =()=>{
const burger = document.querySelector('.burger');
const nav = document.querySelector('.nav-links');
const navLinks=document.querySelectorAll('.nav-links  li');

burger.addEventListener('click',()=>{
nav.classList.toggle('nav-active');
navLinks.forEach((link,index)=>{
if(link.style.animation){
link.style.animation='';
}
else{
link.style.animation='navLinkFade 0.5s ease forwards ${index/7+1.5}s';
}
});
});

}
navSlide();




























