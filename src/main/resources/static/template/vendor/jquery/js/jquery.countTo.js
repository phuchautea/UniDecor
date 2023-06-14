(function($) {
    var CountTo = function(element, options) {
        this.$element = $(element), this.options = $.extend({}, CountTo.DEFAULTS, this.dataOptions(), options), this.init()
    };
    CountTo.DEFAULTS = {
        from: 0,
        to: 0,
        speed: 1e3,
        refreshInterval: 100,
        decimals: 0,
        formatter: formatter,
        onUpdate: null,
        onComplete: null
    }, CountTo.prototype.init = function() {
        this.value = this.options.from, this.loops = Math.ceil(this.options.speed / this.options.refreshInterval), this.loopCount = 0, this.increment = (this.options.to - this.options.from) / this.loops
    }, CountTo.prototype.dataOptions = function() {
        var options = {
                from: this.$element.data("from"),
                to: this.$element.data("to"),
                speed: this.$element.data("speed"),
                refreshInterval: this.$element.data("refresh-interval"),
                decimals: this.$element.data("decimals")
            },
            keys = Object.keys(options);
        for (var i in keys) {
            var key = keys[i];
            typeof options[key] == "undefined" && delete options[key]
        }
        return options
    }, CountTo.prototype.update = function() {
        this.value += this.increment, this.loopCount++, this.render(), typeof this.options.onUpdate == "function" && this.options.onUpdate.call(this.$element, this.value), this.loopCount >= this.loops && (clearInterval(this.interval), this.value = this.options.to, typeof this.options.onComplete == "function" && this.options.onComplete.call(this.$element, this.value))
    }, CountTo.prototype.render = function() {
        var formattedValue = this.options.formatter.call(this.$element, this.value, this.options);
        this.$element.text(formattedValue)
    }, CountTo.prototype.restart = function() {
        this.stop(), this.init(), this.start()
    }, CountTo.prototype.start = function() {
        this.stop(), this.render(), this.interval = setInterval(this.update.bind(this), this.options.refreshInterval)
    }, CountTo.prototype.stop = function() {
        this.interval && clearInterval(this.interval)
    }, CountTo.prototype.toggle = function() {
        this.interval ? this.stop() : this.start()
    };

    function formatter(value, options) {
        return value.toFixed(options.decimals)
    }
    $.fn.countTo = function(option) {
        return this.each(function() {
            var $this = $(this),
                data = $this.data("countTo"),
                init = !data || typeof option == "object",
                options = typeof option == "object" ? option : {},
                method = typeof option == "string" ? option : "start";
            init && (data && data.stop(), $this.data("countTo", data = new CountTo(this, options))), data[method].call(data)
        })
    }
})(jQuery);
