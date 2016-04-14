(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'Clockin', 'User'];

    function EmployeeDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Employee, Clockin, User) {
        var vm = this;
        vm.employee = entity;
        vm.clockins = Clockin.query();
        vm.users = User.query();
        vm.load = function(id) {
            Employee.get({id : id}, function(result) {
                vm.employee = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clockinApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employee.id !== null) {
                Employee.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save(vm.employee, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
