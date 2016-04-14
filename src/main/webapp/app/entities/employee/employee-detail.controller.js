(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employee', 'Clockin', 'User'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, entity, Employee, Clockin, User) {
        var vm = this;
        vm.employee = entity;
        vm.load = function (id) {
            Employee.get({id: id}, function(result) {
                vm.employee = result;
            });
        };
        var unsubscribe = $rootScope.$on('clockinApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
