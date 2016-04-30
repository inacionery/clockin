(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinController', ClockinController);

    ClockinController.$inject = ['$scope', '$state', 'Clockin', 'ClockinSearch'];

    function ClockinController ($scope, $state, Clockin, ClockinSearch) {
        var vm = this;
        vm.workdays = [];
        vm.loadAll = function() {
            Clockin.query(function(result) {
                vm.workdays = result;
            });
        };

        vm.loadAll();
        
    }
})();
