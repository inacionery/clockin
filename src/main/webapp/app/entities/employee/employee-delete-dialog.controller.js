(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmployeeDeleteController',EmployeeDeleteController);

    EmployeeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Employee'];

    function EmployeeDeleteController($uibModalInstance, entity, Employee) {
        var vm = this;
        vm.employee = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Employee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
