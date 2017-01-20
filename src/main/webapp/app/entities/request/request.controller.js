(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('RequestController', RequestController);

    RequestController.$inject = ['Request', 'entity', '$scope', '$state', '$locale', '$rootScope', '$stateParams'];

    function RequestController(Request, entity, $scope, $state, $locale, $rootScope, $stateParams) {
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
    }
})();