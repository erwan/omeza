Periods = {};

Periods.init = function() {
    $("#date-field").datepicker({ dateFormat: 'yy-mm-dd'});
    $(".inplace").editable(editURL, {
        tooltip: 'Click to edit',
        placeholder: '&nbsp;'
    });
};
