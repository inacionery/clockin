(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinPageController', ClockinPageController);

    ClockinPageController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Clockin', 'Employee'];

    function ClockinPageController($scope, $rootScope, $stateParams, entity, Clockin, Employee) {
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
    }
})();
