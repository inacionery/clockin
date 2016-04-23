(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinDialogController', ClockinDialogController);

    ClockinDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Clockin', 'Employee'];

    function ClockinDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Clockin, Employee) {
        var vm = this;
        vm.clockin = entity;
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('clockinApp:clockinUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.clockin.id !== null) {
                Clockin.update(vm.clockin, onSaveSuccess, onSaveError);
            } else {
                Clockin.save(vm.clockin, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
