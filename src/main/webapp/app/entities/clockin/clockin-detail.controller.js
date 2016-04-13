(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinDetailController', ClockinDetailController);

    ClockinDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Clockin', 'Employee'];

    function ClockinDetailController($scope, $rootScope, $stateParams, entity, Clockin, Employee) {
        var vm = this;
        vm.clockin = entity;
        vm.load = function (id) {
            Clockin.get({id: id}, function(result) {
                vm.clockin = result;
            });
        };
        var unsubscribe = $rootScope.$on('clockinApp:clockinUpdate', function(event, result) {
            vm.clockin = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
