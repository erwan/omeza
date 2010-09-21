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

Periods.chart = function() {
    $.get(jsonURL,
        function(result) {
            // For some reason, on app engine JsonNull becomes 0. Curse you!!
            for (var i = 0; i < result.temperature.length; i++) {
                if (result.temperature[i][1] === 0) {
                    result.temperature[i][1] = null;
                }
            }
            $.plot($("#chart"),
                [
                    {
                        label: i18n["temperature"],
                        data: result.temperature
                    },
                    {
                        label: i18n["sex"],
                        data: result.sex,
                        points: { show: true },
                        color: "#FFB5D7"
                    },
                    {
                        label: i18n["special"],
                        data: result.special,
                        points: { show: true }
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
        }
    );
}