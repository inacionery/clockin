(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['Report', 'entity', '$scope', '$state', '$locale', '$rootScope', '$stateParams'];

    function ReportController(Report, entity, $scope, $state, $locale, $rootScope, $stateParams) {
        var vm = this;

        var semester = eval($stateParams.semester);
        var year = eval($stateParams.year);

        vm.previousYear = year;
        vm.previousSemester = (semester - 1);
        vm.nextYear = year;
        vm.nextSemester = (semester + 1);

        if (semester == 0) {
            vm.previousYear = (year - 1);
            vm.previousSemester = 1;
        } else {
            vm.nextYear = (year + 1);
            vm.nextSemester = 0;
        }

        vm.reports = entity;

        vm.today = new Date();

        if (!$scope.selectedIndex) {
            $scope.selectedIndex = 1;
        }

        if (year == vm.today.getFullYear() && ((vm.today.getMonth() > 5 ? 1 : 0) == semester)) {
            $scope.selectedIndex = vm.today.getMonth() - (6 * semester) + 1;
        }

        vm.selectMonth = function selectMonth(select) {
            $rootScope.selectedIndex = select;
        };

        vm.balanceHours = function balanceHours(time) {
            var minus = " ";
            if (time < 0) {
                minus = "-";
                time = time * -1;
            }
            var hours = Math.floor(time / 60);
            var minutes = time % 60;
            hours = hours < 10 ? '0' + hours : hours;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            return minus + hours + ":" + minutes;
        };

        vm.predicate = "employee.socialIdentificationNumber";
        vm.reverse = true;

        vm.isAllSelected = false;
        vm.toggleAll = function(employees) {
            var toggleStatus = vm.isAllSelected;
            angular.forEach(employees, function(employee) {
                employee.check = toggleStatus;
            });

        }
        vm.optionToggled = function(employees) {
            vm.isAllSelected = employees.every(function(employee) {
                return employee.check;
            })
        }
    }
})();