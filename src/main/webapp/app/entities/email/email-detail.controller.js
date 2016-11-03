(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('EmailDetailController', EmailDetailController);

    EmailDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Email'];

    function EmailDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Email) {
        var vm = this;

        vm.email = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('clockinApp:emailUpdate', function(event, result) {
            vm.email = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
