(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmailDialogController', EmailDialogController);

    EmailDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Email'];

    function EmailDialogController($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Email) {
        var vm = this;

        vm.email = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function() {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.email.id !== null) {
                Email.update(vm.email, onSaveSuccess, onSaveError);
            } else {
                Email.save(vm.email, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('clockinApp:emailUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();