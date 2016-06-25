(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinTableController', ClockinTableController);

    ClockinTableController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Clockin', 'Employee'];

    function ClockinTableController($scope, $rootScope, $stateParams, entity, Clockin, Employee) {
        var vm = this;
        var month = eval($stateParams.month);
        var year = eval($stateParams.year);
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
        vm.workdays = entity;
        vm.isWeekend = function isWeekend(date) {
    		date = new Date(date);
    	 	var day = date.getDay();
    	 	return day == 5 || day == 6;
    	};

    	vm.firstHalf = function firstHalf(workdays){
    		var firstHalf = [];
    		for (var i = 0; i < (workdays.length/2); i++) { 
    			firstHalf[i] = workdays[i];
    		}
    		return firstHalf;
    	};
 
    	vm.lastHalf = function lastHalf(workdays){
    		var lastHalf = [];
    		for (var i = 0; i < (workdays.length/2); i++) { 
    			lastHalf[i] = workdays[i + (workdays.length/2) + (workdays.length%2 * 0.5)];
    		}
    		return lastHalf;
    	};
    }
})();
