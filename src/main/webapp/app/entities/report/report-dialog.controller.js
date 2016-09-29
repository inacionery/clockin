(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ReportDialogController', ReportDialogController);

    ReportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Report', 'Email'];

    function ReportDialogController($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Report, Email) {
        var vm = this;

        vm.clear = clear;
        vm.save = save;
        vm.email = Email.query();
        vm.report = entity;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            Report.send(vm.report, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(result) {
            $scope.$emit('clockinApp:reportSend', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();