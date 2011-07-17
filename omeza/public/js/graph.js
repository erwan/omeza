(function(){

var ChartView = Backbone.View.extend({

    el: $("#chart"),

    initialize: function() {
        _.bindAll(this, "render");
        Period.bind("reset", this.render);
        Period.bind("inplacechange", this.render);
    },

    triangleDown: function(plot, x, y, color) {
        var o = plot.pointOffset({ x: x, y: y - 0.2 });
        var ctx = plot.getCanvas().getContext("2d");
        ctx.beginPath();
        ctx.moveTo(o.left, o.top);
        ctx.lineTo(o.left - 5, o.top + 10);
        ctx.lineTo(o.left + 5, o.top + 10);
        ctx.lineTo(o.left, o.top);
        ctx.fillStyle = color;
        ctx.fill();
    },

    triangleUp: function(plot, x, y, color) {
        var o = plot.pointOffset({ x: x, y: y + 0.2 });
        var ctx = plot.getCanvas().getContext("2d");
        ctx.beginPath();
        ctx.moveTo(o.left, o.top);
        ctx.lineTo(o.left - 5, o.top - 10);
        ctx.lineTo(o.left + 5, o.top - 10);
        ctx.lineTo(o.left, o.top);
        ctx.fillStyle = color;
        ctx.fill();
    },

    render: function() {
        console.log("render graph");
        var temperatures = Period.map(function(day){ return [day.get("idx") - 1, day.get("temperature") == 0 ? null : day.get("temperature")]; })
        var mucus = Period.map(function(day){ return [day.get("idx") - 1, day.get("mucus")]; })
        var blood = Period.map(function(day){ return [day.get("idx") - 1, day.get("blood")]; })
        var plot = $.plot(this.el,
            [
                {
                    label: i18n["temperature"],
                    points: { show: true },
                    lines: { show: true },
                    data: temperatures
                },
                { // Just to get the legend!
                    label: i18n["sex"],
                    data: [],
                    color: "#FFB5D7"
                },
                { // Just to get the legend!
                    label: i18n["special"],
                    data: [],
                    color: "#afd8f8"
                },
                {
                    label: i18n["blood"],
                    lines: { show: true, fill: true },
                    data: blood,
                    color: "#FF0000"
                },
                {
                    label: i18n["mucus"],
                    lines: { show: true, fill: true },
                    data: mucus,
                    color: "#FFFF00"
                }
            ],
            {
                xaxis: {
                    min: 0,
                    max: Period.size()
                },
                yaxis: {
                    min: 35.5,
                    max: 40
                }
            }
        );
        var chart = this;
        Period.each(function(day){
            if (day.get("sex"))
                this.triangleDown(plot, day.get("idx")-1, day.get("temperature") ? day.get("temperature") : 37, "#FFB5D7");
            if (day.get("special"))
                this.triangleUp(plot, day.get("idx")-1, day.get("temperature") ? day.get("temperature") : 37, "#afd8f8");
        }, this);
    }

});

window["Chart"] = new ChartView();

}());
