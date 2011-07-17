(function() {

var DayModel = Backbone.Model.extend({
    defaults: {
        temperature: 0,
        sex: "",
        special: "",
        memo: "",
        mucus: 0,
        blood: 0
    }
});

var DayView = Backbone.View.extend({
    tagName: "tr",
/*    initialize: function() {
        _.bindAll(this, "inPlaceEdit");
    },*/
    events: {
        "click .editable": "inPlaceEdit"
    },
    template: _.template($('#day-template').html()),
    render: function() {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    },
    inPlaceEdit: function(e) {
        var attrName = $(e.currentTarget).data("field");
        var newValue = parseInt(prompt("New value for: " + attrName), 10);
        if (newValue) {
            var attrs = {}; attrs[attrName] = newValue;
            this.model.set(attrs);
            console.log("set ", attrName, newValue);
            this.model.save();
            this.model.trigger("inplacechange");
        }
    }
});

var PeriodCollection = Backbone.Collection.extend({
    url: jsonURL,
    model: DayModel,
    parse: function(response) {
        return response.days;
    }
});

var Period = new PeriodCollection();

var PeriodView = Backbone.View.extend({
    el: $("#periodtable"),
    initialize: function() {
        _.bindAll(this, "render");
        Period.bind("reset", this.render);
        Period.bind("inplacechange", this.render);
    },
    render: function() {
        console.log("Render PeriodView");
        this.el.html('');
        var $view = this.el;
        var idx = 1;
        Period.map(function(day) {
            day.set({"idx": idx++});
            return new DayView({model: day});
        }).forEach(function(entry) {
            $view.append(entry.render().el);
        });
    }

});


var AppView = Backbone.View.extend({
    periodView: new PeriodView(),
    initialize: function() {
        Period.fetch();
/*        $("#date-field").datepicker({ dateFormat: 'yy-mm-dd'});
        $("#graphHide").click(function(){
            $("#chart").slideToggle(500);
        });
        $(".inplace").editable(editURL, {
            tooltip: 'Click to edit',
            placeholder: '&nbsp;',
            callback: function() {
                Periods.chart();
            }
        });*/
        // Chart.draw();
    }
});

/* Backbone.sync = function(method, model, options) {
    console.log("sync ", model);
}*/ 

window["Period"] = Period;
window["App"] = new AppView();

}());
