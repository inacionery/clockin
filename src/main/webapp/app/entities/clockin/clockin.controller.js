(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinController', ClockinController);

    ClockinController.$inject = ['$scope', '$state', 'Clockin', 'ClockinSearch'];

    function ClockinController ($scope, $state, Clockin, ClockinSearch) {
        var vm = this;
        vm.clockins = [];
        vm.loadAll = function() {
            Clockin.query(function(result) {
                vm.clockins = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClockinSearch.query({query: vm.searchQuery}, function(result) {
                vm.clockins = result;
            });
        };
        vm.loadAll();
        
    }
})();
