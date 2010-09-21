Periods = {};

Periods.init = function() {
    $("#date-field").datepicker({ dateFormat: 'yy-mm-dd'});
    $(".inplace").editable(editURL, {
        tooltip: 'Click to edit',
        placeholder: '&nbsp;',
        callback: function(){Periods.chart();}
    });
    Periods.chart();
};

Periods.triangleDown = function(plot, x, y, color) {
    var o = plot.pointOffset({ x: x, y: y - 0.2 });
    var ctx = plot.getCanvas().getContext("2d");
    ctx.beginPath();
    ctx.moveTo(o.left, o.top);
    ctx.lineTo(o.left - 5, o.top + 10);
    ctx.lineTo(o.left + 5, o.top + 10);
    ctx.lineTo(o.left, o.top);
    ctx.fillStyle = color;
    ctx.fill();
}

Periods.triangleUp = function(plot, x, y, color) {
    var o = plot.pointOffset({ x: x, y: y + 0.2 });
    var ctx = plot.getCanvas().getContext("2d");
    ctx.beginPath();
    ctx.moveTo(o.left, o.top);
    ctx.lineTo(o.left - 5, o.top - 10);
    ctx.lineTo(o.left + 5, o.top - 10);
    ctx.lineTo(o.left, o.top);
    ctx.fillStyle = color;
    ctx.fill();
}

Periods.chart = function() {
    if (!jsonURL) return; // TODO: Put a placeholder
    $.get(jsonURL,
        function(result) {
            // For some reason, on app engine JsonNull becomes 0. Curse you!!
            for (var i = 0; i < result.temperature.length; i++) {
                if (result.temperature[i][1] === 0) {
                    result.temperature[i][1] = null;
                }
            }
            var plot = $.plot($("#chart"),
                [
                    {
                        label: i18n["temperature"],
                        points: { show: true },
                        lines: { show: true },
                        data: result.temperature
                    },
                    { // Just to get the legend!
                        label: i18n["sex"],
                        data: [],
                        color: "#FFB5D7"
                    },
                    { // Just to get the legend!
                        label: i18n["special"],
                        data: []
                    }
                ],
                {
                    xaxis: {
                        min: 0,
                        max: result.temperature.length
                    },
                    yaxis: {
                        min: 35.5,
                        max: 40
                    }
                }
            );
            for (var i = 0; i < result.sex.length; i++) {
                var day = result.sex[i];
                var y = result.temperature[day-1][1] ? result.temperature[day-1][1] : 37;
                Periods.triangleDown(plot, day, y, "FFB5D7");
            }
            for (var i = 0; i < result.special.length; i++) {
                var day = result.special[i];
                var y = result.temperature[day-1][1] ? result.temperature[day-1][1] : 37;
                Periods.triangleUp(plot, day, y, "afd8f8");
            }
        }
    );
}