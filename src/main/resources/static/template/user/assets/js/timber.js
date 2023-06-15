(function(a) {
    a.fn.prepareTransition = function() {
        return this.each(function() {
            var b = a(this);
            b.one("TransitionEnd webkitTransitionEnd transitionend oTransitionEnd", function() {
                b.removeClass("is-transitioning")
            });
            var c = ["transition-duration", "-moz-transition-duration", "-webkit-transition-duration", "-o-transition-duration"],
                d = 0;
            a.each(c, function(a2, c2) {
                d = parseFloat(b.css(c2)) || d
            }), d != 0 && (b.addClass("is-transitioning"), b[0].offsetWidth)
        })
    }
})(jQuery);

function replaceUrlParam(e, r, a) {
    var n = new RegExp("(" + r + "=).*?(&|$)"),
        c = e;
    return c = e.search(n) >= 0 ? e.replace(n, "$1" + a + "$2") : c + (c.indexOf("?") > 0 ? "&" : "?") + r + "=" + a
}
typeof Shopify == "undefined" && (Shopify = {}), Shopify.formatMoney || (Shopify.formatMoney = function(cents, format) {
    var value = "",
        placeholderRegex = /\{\{\s*(\w+)\s*\}\}/,
        formatString = format || this.money_format;
    typeof cents == "string" && (cents = cents.replace(".", ""));

    function defaultOption(opt, def) {
        return typeof opt == "undefined" ? def : opt
    }

    function formatWithDelimiters(number, precision, thousands, decimal) {
        if (precision = defaultOption(precision, 2), thousands = defaultOption(thousands, ","), decimal = defaultOption(decimal, "."), isNaN(number) || number == null) return 0;
        number = (number / 100).toFixed(precision);
        var parts = number.split("."),
            dollars = parts[0].replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1" + thousands),
            cents2 = parts[1] ? decimal + parts[1] : "";
        return dollars + cents2
    }
    switch (formatString.match(placeholderRegex)[1]) {
        case "amount":
            value = formatWithDelimiters(cents, 2);
            break;
        case "amount_no_decimals":
            value = formatWithDelimiters(cents, 0);
            break;
        case "amount_with_comma_separator":
            value = formatWithDelimiters(cents, 2, ".", ",");
            break;
        case "amount_no_decimals_with_comma_separator":
            value = formatWithDelimiters(cents, 0, ".", ",");
            break
    }
    return formatString.replace(placeholderRegex, value)
}), window.timber = window.timber || {}, timber.cacheSelectors = function() {
    timber.cache = {
        $html: $("html"),
        $body: $(document.body),
        $navigation: $("#AccessibleNav"),
        $mobileSubNavToggle: $(".mobile-nav__toggle"),
        $changeView: $(".change-view"),
        $productImage: $("#ProductPhotoImg"),
        $thumbImages: $("#ProductThumbs").find("a.product-single__thumbnail"),
        $recoverPasswordLink: $("#RecoverPassword"),
        $hideRecoverPasswordLink: $("#HideRecoverPasswordLink"),
        $recoverPasswordForm: $("#RecoverPasswordForm"),
        $customerLoginForm: $("#CustomerLoginForm"),
        $passwordResetSuccess: $("#ResetSuccess")
    }
}, timber.init = function() {
    timber.cacheSelectors(), timber.accessibleNav(), timber.mobileNavToggle(), timber.productImageSwitch(), timber.responsiveVideos(), timber.collectionViews(), timber.loginForms()
}, timber.accessibleNav = function() {
    var $nav = timber.cache.$navigation,
        $allLinks = $nav.find("a"),
        $topLevel = $nav.children("li").find("a"),
        $parents = $nav.find(".site-nav--has-dropdown"),
        $subMenuLinks = $nav.find(".site-nav__dropdown").find("a"),
        activeClass = "nav-hover",
        focusClass = "nav-focus";
    $parents.on("mouseenter touchstart", function(evt) {
        var $el = $(this);
        $el.hasClass(activeClass) || evt.preventDefault(), showDropdown($el)
    }), $parents.on("mouseleave", function() {
        hideDropdown($(this))
    }), $subMenuLinks.on("touchstart", function(evt) {
        evt.stopImmediatePropagation()
    }), $allLinks.focus(function() {
        handleFocus($(this))
    }), $allLinks.blur(function() {
        removeFocus($topLevel)
    });

    function handleFocus($el) {
        var $subMenu = $el.next("ul"),
            hasSubMenu = !!$subMenu.hasClass("sub-nav"),
            isSubItem = $(".site-nav__dropdown").has($el).length,
            $newFocus = null;
        isSubItem ? ($newFocus = $el.closest(".site-nav--has-dropdown").find("a"), addFocus($newFocus)) : (removeFocus($topLevel), addFocus($el))
    }

    function showDropdown($el) {
        $el.addClass(activeClass), setTimeout(function() {
            timber.cache.$body.on("touchstart", function() {
                hideDropdown($el)
            })
        }, 250)
    }

    function hideDropdown($el) {
        $el.removeClass(activeClass), timber.cache.$body.off("touchstart")
    }

    function addFocus($el) {
        $el.addClass(focusClass)
    }

    function removeFocus($el) {
        $el.removeClass(focusClass)
    }
}, timber.mobileNavToggle = function() {
    timber.cache.$mobileSubNavToggle.on("click", function() {
        $(this).parent().toggleClass("mobile-nav--expanded")
    })
}, timber.getHash = function() {
    return window.location.hash
}, timber.productPage = function(options) {
    var moneyFormat = options.money_format,
        variant = options.variant,
        selector = options.selector,
        $productImage = $("#ProductPhotoImg"),
        $addToCart = $(".btn.ajax-spin-cart"),
        $productPrice = $("#ProductPrice"),
        $comparePrice = $("#ComparePrice"),
        $productDiscount = $("#ProductDiscount"),
        $quantityElements = $(".quantity-selector, label + .js-qty"),
        $variantSku = $("#VariantSku"),
        $barCode = $("#BarCode"),
        $weight = $("#Weight"),
        $addToCartText = $("#AddToCartText"),
        $btn_theme = $(".btn_theme"),
        $affiliate_product = $(".affiliate-product"),
        $qty_plus_minus = $(".cart-plus-minus"),
        $productSelect = $("#productSelect"),
        $productSelect_Option = $("#productSelect option"),
        $stock_zero = $(".stock-zero"),
        $stock_more = $(".stock-more"),
        $available_stock = $(".available-stock"),
        $in_stock = $(".in-stock"),
        $out_stock = $(".out-stock"),
        $in_out_stock = $(".in-out-stock"),
        $data_value = $("#data-value");
    if (variant) {
        if (variant && variant.featured_media && jQuery('#pro-detail-slider .slick-slide[data-image-id="' + variant.featured_media.id + '"]').trigger("click"), variant.featured_media) {
            var newImg = variant.featured_media,
                el = $productImage[0];
            Shopify.Image.switchImage(newImg, el, timber.switchImage)
        }
        variant.available ? ($addToCart.removeClass("disabled").prop("disabled", !1), $addToCartText.html("Add to cart"), $btn_theme.removeClass("disabled").prop("disabled", !1), $affiliate_product.removeClass("disabled").prop("disabled", !1), $qty_plus_minus.removeClass("disabled").prop("disabled", !1), $quantityElements.show(), $variantSku.text(variant.sku), $barCode.text(variant.barcode), $weight.text(variant.weight), $in_stock.show(), $out_stock.hide()) : ($addToCart.addClass("disabled").prop("disabled", !0), $addToCartText.html("Not available"), $btn_theme.addClass("disabled").prop("disabled", !0), $affiliate_product.addClass("disabled").prop("disabled", !0), $qty_plus_minus.addClass("disabled").prop("disabled", !0), $quantityElements.hide(), $variantSku.empty(), $barCode.empty(), $weight.empty(), $in_stock.hide(), $out_stock.show()), $productPrice.html(Shopify.formatMoney(variant.price, moneyFormat)), variant.compare_at_price > variant.price ? ($productDiscount.html(((variant.compare_at_price - variant.price) * 100 / variant.compare_at_price).toFixed()).show(), $comparePrice.html(" " + Shopify.formatMoney(variant.compare_at_price, moneyFormat)).show()) : ($productDiscount.hide(), $comparePrice.hide()), variant.id && $productSelect_Option.each(function() {
            if ($(this).val() == variant.id) {
                var in_stocks = $(this).attr("data-stocks");
                variant.inventory_management == "shopify" && variant.inventory_policy != "continue" ? in_stocks > 0 ? ($stock_zero.hide(), $stock_more.show(), $available_stock.text(in_stocks), $in_out_stock.hide()) : ($stock_zero.show(), $stock_more.hide(), $available_stock.text(in_stocks), $in_out_stock.hide()) : ($stock_more.hide(), $stock_zero.hide(), $in_out_stock.show())
            }
        }), variant.id && $(".swatch-element").on("click", function(evt) {
            var data_value = $(this).attr("data-value");
            $(this).parents(".variant-wrap").parent().find("#data-value").text(data_value)
        })
    } else $addToCart.addClass("disabled").prop("disabled", !0), $addToCartText.html("Not available"), $btn_theme.addClass("disabled").prop("disabled", !0), $affiliate_product.addClass("disabled").prop("disabled", !0), $qty_plus_minus.addClass("disabled").prop("disabled", !0), $quantityElements.hide(), $variantSku.empty(), $barCode.empty(), $weight.empty(), $stock_zero.show(), $stock_more.hide(), $available_stock.text("0"), $in_stock.show(), $out_stock.hide(), $in_out_stock.hide()
};
var Shopify = Shopify || {};
Shopify.optionsMap = {}, Shopify.updateOptionsInSelector = function(selectorIndex) {
    switch (selectorIndex) {
        case 0:
            var key = "root",
                selector = jQuery(".single-option-selector:eq(0)");
            break;
        case 1:
            var key = jQuery(".single-option-selector:eq(0)").val(),
                selector = jQuery(".single-option-selector:eq(1)");
            break;
        case 2:
            var key = jQuery(".single-option-selector:eq(0)").val();
            key += " / " + jQuery(".single-option-selector:eq(1)").val();
            var selector = jQuery(".single-option-selector:eq(2)")
    }
    var initialValue = selector.val();
    selector.empty();
    for (var availableOptions = Shopify.optionsMap[key], i = 0; i < availableOptions.length; i++) {
        var option = availableOptions[i],
            newOption = jQuery("<option></option>").val(option).html(option);
        selector.append(newOption)
    }
    jQuery('.swatch[data-option-index="' + selectorIndex + '"] .swatch-element').each(function() {
        jQuery.inArray($(this).attr("data-value"), availableOptions) !== -1 ? $(this).removeClass("soldout").show().find(":radio").removeAttr("disabled", "disabled").removeAttr("checked") : $(this).addClass("soldout").hide().find(":radio").removeAttr("checked").attr("disabled", "disabled")
    }), jQuery.inArray(initialValue, availableOptions) !== -1 && selector.val(initialValue), selector.trigger("change")
}, $(document).ready(function() {
    setTimeout(function() {
        $(".swatch-element.first-variant").trigger("click")
    }, 10)
}), Shopify.linkOptionSelectors = function(product) {
    for (var i = 0; i < product.variants.length; i++) {
        var variant = product.variants[i];
        if (variant.available) {
            if (Shopify.optionsMap.root = Shopify.optionsMap.root || [], Shopify.optionsMap.root.push(variant.option1), Shopify.optionsMap.root = Shopify.uniq(Shopify.optionsMap.root), product.options.length > 1) {
                var key = variant.option1;
                Shopify.optionsMap[key] = Shopify.optionsMap[key] || [], Shopify.optionsMap[key].push(variant.option2), Shopify.optionsMap[key] = Shopify.uniq(Shopify.optionsMap[key])
            }
            if (product.options.length === 3) {
                var key = variant.option1 + " / " + variant.option2;
                Shopify.optionsMap[key] = Shopify.optionsMap[key] || [], Shopify.optionsMap[key].push(variant.option3), Shopify.optionsMap[key] = Shopify.uniq(Shopify.optionsMap[key])
            }
        }
    }
    Shopify.updateOptionsInSelector(0), product.options.length > 1 && Shopify.updateOptionsInSelector(1), product.options.length === 3 && Shopify.updateOptionsInSelector(2), jQuery(".single-option-selector:eq(0)").change(function() {
        return Shopify.updateOptionsInSelector(1), product.options.length === 3 && Shopify.updateOptionsInSelector(2), !0
    }), jQuery(".single-option-selector:eq(1)").change(function() {
        return product.options.length === 3 && Shopify.updateOptionsInSelector(2), !0
    })
}, timber.productImageSwitch = function() {
    timber.cache.$thumbImages.length && timber.cache.$thumbImages.on("click", function(evt) {
        var img_src_id = $(this).find("img").attr("data-image-id");
        jQuery('.slider-big .slick-slide[data-image-id="' + img_src_id + '"]').trigger("click")
    })
}, timber.switchImage = function(src, imgObject, el) {
    var $el = $(el);
    $el.attr("src", src)
}, timber.formCart = function() {
    $("form.cart-").submit()
}, timber.responsiveVideos = function() {
    var $iframeVideo = $('iframe[src*="youtube.com/embed"], iframe[src*="player.vimeo"]'),
        $iframeReset = $iframeVideo.add("iframe#admin_bar_iframe");
    $iframeVideo.each(function() {
        $(this).wrap('<div class="video-wrapper"></div>')
    }), $iframeReset.each(function() {
        this.src = this.src
    })
}, timber.collectionViews = function() {
    timber.cache.$changeView.length && timber.cache.$changeView.on("click", function() {
        var view = $(this).data("view"),
            url = document.URL,
            hasParams = url.indexOf("?") > -1;
        hasParams ? window.location = replaceUrlParam(url, "view", view) : window.location = url + "?view=" + view
    })
}, timber.loginForms = function() {
    function showRecoverPasswordForm() {
        timber.cache.$recoverPasswordForm.show(), timber.cache.$customerLoginForm.hide()
    }

    function hideRecoverPasswordForm() {
        timber.cache.$recoverPasswordForm.hide(), timber.cache.$customerLoginForm.show()
    }
    timber.cache.$recoverPasswordLink.on("click", function(evt) {
        evt.preventDefault(), showRecoverPasswordForm()
    }), timber.cache.$hideRecoverPasswordLink.on("click", function(evt) {
        evt.preventDefault(), hideRecoverPasswordForm()
    }), timber.getHash() == "#recover" && showRecoverPasswordForm()
}, timber.resetPasswordSuccess = function() {
    timber.cache.$passwordResetSuccess.show()
}, timber.Drawers = function() {
    var Drawer = function(id, position, options) {
        var defaults = {
            close: ".js-drawer-close",
            open: ".js-drawer-open-" + position,
            openClass: "js-drawer-open",
            dirOpenClass: "js-drawer-open-" + position
        };
        if (this.$nodes = {
                parent: $("body, html"),
                page: $("#PageContainer"),
                moved: $(".is-moved-by-drawer")
            }, this.config = $.extend(defaults, options), this.position = position, this.$drawer = $("#" + id), !this.$drawer.length) return !1;
        this.drawerIsOpen = !1, this.init()
    };
    return Drawer.prototype.init = function() {
        $(this.config.open).on("click", $.proxy(this.open, this)), this.$drawer.find(this.config.close).on("click", $.proxy(this.close, this))
    }, Drawer.prototype.open = function(evt) {
        var externalCall = !1;
        if (evt ? evt.preventDefault() : externalCall = !0, evt && evt.stopPropagation && (evt.stopPropagation(), this.$activeSource = $(evt.currentTarget)), this.drawerIsOpen && !externalCall) return this.close();
        timber.cache.$body.trigger("beforeDrawerOpen.timber", this), this.$nodes.moved.addClass("is-transitioning"), this.$drawer.prepareTransition(), this.$nodes.parent.addClass(this.config.openClass + " " + this.config.dirOpenClass), this.drawerIsOpen = !0, this.trapFocus(this.$drawer, "drawer_focus"), this.config.onDrawerOpen && typeof this.config.onDrawerOpen == "function" && (externalCall || this.config.onDrawerOpen()), this.$activeSource && this.$activeSource.attr("aria-expanded") && this.$activeSource.attr("aria-expanded", "true"), this.$nodes.page.on("touchmove.drawer", function() {
            return !1
        }), this.$nodes.page.on("click.drawer", $.proxy(function() {
            return this.close(), !1
        }, this)), timber.cache.$body.trigger("afterDrawerOpen.timber", this)
    }, Drawer.prototype.close = function() {
        this.drawerIsOpen && (timber.cache.$body.trigger("beforeDrawerClose.timber", this), $(document.activeElement).trigger("blur"), this.$nodes.moved.prepareTransition({
            disableExisting: !0
        }), this.$drawer.prepareTransition({
            disableExisting: !0
        }), this.$nodes.parent.removeClass(this.config.dirOpenClass + " " + this.config.openClass), this.drawerIsOpen = !1, this.removeTrapFocus(this.$drawer, "drawer_focus"), this.$nodes.page.off(".drawer"), timber.cache.$body.trigger("afterDrawerClose.timber", this))
    }, Drawer.prototype.trapFocus = function($container, eventNamespace) {
        var eventName = eventNamespace ? "focusin." + eventNamespace : "focusin";
        $container.attr("tabindex", "-1"), $container.focus(), $(document).on(eventName, function(evt) {
            $container[0] !== evt.target && !$container.has(evt.target).length && $container.focus()
        })
    }, Drawer.prototype.removeTrapFocus = function($container, eventNamespace) {
        var eventName = eventNamespace ? "focusin." + eventNamespace : "focusin";
        $container.removeAttr("tabindex"), $(document).off(eventName)
    }, Drawer
}(), $(timber.init);
