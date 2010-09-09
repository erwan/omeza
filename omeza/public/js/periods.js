goog.provide('omeza.periods');

goog.require('goog.dom');
goog.require('goog.i18n.DateTimeFormat');
goog.require('goog.i18n.DateTimeParse');
goog.require('goog.ui.InputDatePicker');

periods = {};

periods.init = function() {
  var PATTERN = "dd'/'MM'/'yyyy";
  var formatter = new goog.i18n.DateTimeFormat(PATTERN);
  var parser = new goog.i18n.DateTimeParse(PATTERN);
 
  var idp1 = new goog.ui.InputDatePicker(formatter, parser);
  idp1.decorate(goog.dom.getElement('date-field'));
};

// Ensures the symbol will be visible after compiler renaming.
goog.exportSymbol('omeza.periods', periods);
