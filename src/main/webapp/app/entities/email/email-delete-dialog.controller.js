(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmailDeleteController',EmailDeleteController);

    EmailDeleteController.$inject = ['$uibModalInstance', 'entity', 'Email'];

    function EmailDeleteController($uibModalInstance, entity, Email) {
        var vm = this;

        vm.email = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Email.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
