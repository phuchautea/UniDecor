(function($) {
    "use strict";
    jQuery(document).ready(function() {
        document.onkeydown = function(e) {
            if (event.keyCode == 123 || e.ctrlKey && e.shiftKey && e.keyCode == "I".charCodeAt(0) || e.ctrlKey && e.shiftKey && e.keyCode == "C".charCodeAt(0) || e.ctrlKey && e.shiftKey && e.keyCode == "J".charCodeAt(0) || e.ctrlKey && e.keyCode == "U".charCodeAt(0)) return !1
        }, $("button.navbar-toggler").on("click", function() {
            $(".main-menu-area").addClass("active"), $(".mm-fullscreen-bg").addClass("active"), $("body").addClass("hidden")
        }), $(".close-box").on("click", function() {
            $(".main-menu-area").removeClass("active"), $(".mm-fullscreen-bg").removeClass("active"), $("body").removeClass("hidden")
        }), $("button.filter-button").on("click", function() {
            $(".filter-sidebar").addClass("active"), $(".mm-fullscreen-bg").addClass("active")
        }), $("button.close-filter-sidebar").on("click", function() {
            $(".filter-sidebar").removeClass("active"), $(".mm-fullscreen-bg").removeClass("active")
        }), $(".mm-fullscreen-bg").on("click", function() {
            $(".main-menu-area").removeClass("active"), $(".filter-sidebar").removeClass("active"), $(".mm-fullscreen-bg").removeClass("active"), $("body").removeClass("hidden")
        }), $(".full-view, .zoom").on("click", function() {
            $(".product_img_top").magnificPopup({
                delegate: "a",
                type: "image",
                showCloseBtn: !0,
                closeBtnInside: !1,
                midClick: !0,
                tLoading: "Loading image #%curr%...",
                mainClass: "mfp-img-mobile",
                gallery: {
                    enabled: !0,
                    navigateByImgClick: !0,
                    preload: [0, 1]
                }
            }).magnificPopup("open")
        })
    })
})(jQuery);
