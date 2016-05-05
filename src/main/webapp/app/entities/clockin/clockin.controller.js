(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinController', ClockinController);

    ClockinController.$inject = ['$scope', '$state', 'Clockin', 'ClockinSearch'];

    function ClockinController ($scope, $state, Clockin, ClockinSearch) {
        var vm = this;
        vm.workdays = [];
        var today = new Date();
        var month = eval(today.getMonth())+1;
        var year = eval(today.getFullYear());
        vm.previousYear = year;
        vm.previousMonth = (month - 1);
        vm.nextYear = year;
        vm.nextMonth = (month + 1);
        if (month == 1){
        	vm.previousYear = (year - 1);
        	vm.previousMonth = 12;
        } else if (month == 12){
        	 vm.nextYear = (year + 1);
             vm.nextMonth = 1;
        }
        vm.date = new Date(vm.previousYear, vm.previousMonth);
        vm.previousDate = new Date(vm.previousYear, vm.previousMonth - 1);
        vm.nextDate = new Date(vm.nextYear, vm.nextMonth -1);
        vm.loadAll = function() {
            Clockin.query(function(result) {
                vm.workdays = result;
            });
        };

        vm.isWeekend = function isWeekend(date) {
    		date = new Date(date);
    	 	var day = date.getDay();
    	 	return day == 5 || day == 6;
    	};
        vm.loadAll();
        
    }
})();
