(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinDeleteController',ClockinDeleteController);

    ClockinDeleteController.$inject = ['$uibModalInstance', 'entity', 'Clockin'];

    function ClockinDeleteController($uibModalInstance, entity, Clockin) {
        var vm = this;
        vm.clockin = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Clockin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
