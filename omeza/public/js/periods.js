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
            $.plot($("#chart"), [result.temperature], {
                xaxis: {
                    min: 0,
                    max: result.temperature.length
                },
                yaxis: {
                    min: 35.5,
                    max: 40
                }
            });
        }
    );
}