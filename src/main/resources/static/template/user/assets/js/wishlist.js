(function(Wishlist, $) {
    var $wishlistButton = $(".wishlist-btn"),
        $wishlistTile = $(".wishlist-tile-container"),
        $wishlitcounter = $(".wishlist-counter"),
        numProductTiles = $wishlistTile.length,
        wishlist = localStorage.getItem("user_wishlist") || [],
        wishlist_count_set = localStorage.getItem("user_wishlist_count");
    if (wishlist_count_set == null) {
        var wishlist_count = "0";
        $wishlitcounter.html(wishlist_count)
    } else $wishlitcounter.html(wishlist_count_set);
    wishlist.length > 0 && (wishlist = JSON.parse(localStorage.getItem("user_wishlist")));
    var animateWishlist = function(self2) {
            $(self2).toggleClass("is-active")
        },
        updateWishlist = function(self2) {
            var productHandle = $(self2).attr("data-product-handle"),
                isRemove = $(self2).hasClass("is-active"),
                wishlist_value = localStorage.getItem("user_wishlist_count");
            if (isRemove) {
                var removeIndex = wishlist.indexOf(productHandle);
                wishlist.splice(removeIndex, 1);
                var wishlist_count2 = localStorage.getItem("user_wishlist_count");
                if (wishlist_count2 != null) {
                    var minus_val = -1,
                        wish_ct = Math.max(parseInt(wishlist_count2) + minus_val, 0);
                    localStorage.setItem("user_wishlist_count", wish_ct)
                }
                localStorage.setItem("user_wishlist", JSON.stringify(wishlist));
                var icon_length = $(self2).find("span").length;
                icon_length != 0 && attr != "product" && $wishlistButton.html('<span class="add-wishlist"><i class="bi bi-heart"></i></span><span class="loading-wishlist animated infinite rotateOut"><i class="bi bi-brightness-high"></i></span><span class="remove-wishlist"><i class="bi bi-x"></i></span>');
                var wishlist_count_value = localStorage.getItem("user_wishlist_count");
                $wishlitcounter.html(wishlist_count_value)
            } else {
                wishlist.push(productHandle);
                var wishlist_count2 = localStorage.getItem("user_wishlist_count");
                if (wishlist_count2 == null) var plus_val = 1,
                    wish_ct = plus_val;
                else var plus_val = 1,
                    wish_ct = Math.max(parseInt(wishlist_count2) + plus_val, 0);
                localStorage.setItem("user_wishlist_count", wish_ct), localStorage.setItem("user_wishlist", JSON.stringify(wishlist));
                var icon_length = $(self2).find("span").length;
                icon_length != 0 && attr != "product" && $wishlistButton.html('<span class="add-wishlist"><i class="bi bi-heart"></i></span><span class="loading-wishlist animated infinite rotateOut"><i class="bi bi-brightness-high"></i></span><span class="remove-wishlist"><i class="bi bi-x"></i></span>');
                var wishlist_count_value = localStorage.getItem("user_wishlist_count");
                $wishlitcounter.html(wishlist_count_value)
            }
        },
        activateItemsInWishlist = function() {
            $wishlistButton.each(function() {
                var productHandle = $(this).attr("data-product-handle");
                if (wishlist.indexOf(productHandle) > -1) {
                    $(this).addClass("is-active");
                    var icon_length = $(this).find("span").length;
                    $(this).hasClass("is-active") ? icon_length != 0 && attr != "product" && $(this).html('<span class="add-wishlist"><i class="bi bi-heart"></i></span><span class="loading-wishlist animated infinite rotateOut"><i class="bi bi-brightness-high"></i></span><span class="remove-wishlist"><i class="bi bi-x"></i></span>') : icon_length != 0 && attr != "product" && $(this).html('<span class="add-wishlist"><i class="bi bi-heart"></i></span><span class="loading-wishlist animated infinite rotateOut"><i class="bi bi-brightness-high"></i></span><span class="remove-wishlist"><i class="bi bi-x"></i></span>')
                }
            })
        },
        displayOnlyWishlistItems = function() {
            $wishlistTile.each(function() {
                var productHandle = $(this).attr("data-product-handle");
                wishlist.indexOf(productHandle) === -1 && ($(this).remove(), numProductTiles--)
            })
        },
        loadWishlist = function() {
            window.location.href.indexOf("pages/wishlist") > -1 && (displayOnlyWishlistItems(), $(".wishlist-loader").fadeOut(2e3, function() {
                $(".wishlist-grid").addClass("is_visible"), $(".wishlist-hero").addClass("is_visible"), numProductTiles == 0 ? ($(".wishlist-grid-empty-list").addClass("is_visible"), $(".wishlist-grid").hide()) : $(".wishlist-grid-empty-list").hide()
            }))
        },
        bindUIActions = function() {
            $wishlistButton.click(function(e) {
                e.preventDefault(), updateWishlist(this), animateWishlist(this)
            })
        };
    Wishlist.init = function() {
        bindUIActions(), activateItemsInWishlist(), loadWishlist()
    }, $("#remove-done").click(function() {
        var raw = localStorage.getItem("user_wishlist"),
            length = raw.length,
            i, productHandle = $(self).attr("data-product-handle"),
            isRemove = $(self).hasClass("is-active");
        for (i = length - 1; i >= 0; i--) {
            var removeIndex = wishlist.indexOf(productHandle);
            wishlist.splice(removeIndex, 1);
            var wishlist_count2 = localStorage.getItem("user_wishlist_count");
            if (wishlist_count2 != null) {
                var wish_ct = Math.max(parseInt(0), 0);
                localStorage.setItem("user_wishlist_count", wish_ct)
            }
            localStorage.setItem("user_wishlist", JSON.stringify(wishlist))
        }
        location.reload(!0)
    })
})(window.Wishlist = window.Wishlist || {}, jQuery, void 0);

function reloadPage() {
    location.reload(!0)
}
