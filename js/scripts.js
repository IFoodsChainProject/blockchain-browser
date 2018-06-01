
function scroll_to(clicked_link, nav_height) {
	var element_class = clicked_link.attr('href').replace('#', '.');
	var scroll_to = 0;
	if(element_class != '.top-content') {
		element_class += '-container';
		scroll_to = $(element_class).offset().top - nav_height;
	}
	if($(window).scrollTop() != scroll_to) {
		$('html, body').stop().animate({scrollTop: scroll_to}, 1000);
	}
}


jQuery(document).ready(function() {
	
	// toggle "navbar-no-bg" class
	$('.top-content .text').waypoint(function() {
		$('nav').toggleClass('navbar-no-bg');
	},{ offset: 0});
	
    var loadImg = new Image();
    /*loadImg.src = 'img/backgrounds/newbannerBg2.jpg';*/
    loadImg.onload = function(){

    }

    $('.solution-container').backstretch("img/backgrounds/pork.jpg");
    
    /*
        Wow
    */
    new WOW().init();


    /*
        btm animation
    */
    $(".vision-container .features-box-icon").hover(
        function () {
            $(this).addClass("swing");
        },
        function () {
            $(this).removeClass("swing");
        }
    );
    $(".activityEnterBtn").hover(
        function () {
            $(this).addClass("shake");
        },
        function () {
            $(this).removeClass("shake");
        }
    );

    $(".disclaimerBox").addClass("wow bounceIn");
});

function showDis() {
    $(".disclaimerContianer").removeClass("wow bounceOutUp");
    $("body").addClass("showDisclaimer");
    $(".disclaimerBox").addClass("wow bounceIn");
}


jQuery(window).load(function() {

});

