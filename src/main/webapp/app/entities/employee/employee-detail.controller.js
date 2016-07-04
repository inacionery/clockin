(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employee', 'User'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, entity, Employee, User) {
        var vm = this;
        vm.employee = entity;
        
        var unsubscribe = $rootScope.$on('clockinApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
