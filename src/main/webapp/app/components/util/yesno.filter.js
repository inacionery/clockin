(function() {
    'use strict';

    angular
        .module('clockinApp')
        .filter('yesNo', yesNo);

    function yesNo() {
        return yesNoFilter;

        function yesNoFilter (input) {
            if (input !== null) {
            	  return input ? 'Sim' : 'Não';
            }

            return input;
        }
    }
})();
