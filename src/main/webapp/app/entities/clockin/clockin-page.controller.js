(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinPageController', ClockinPageController);

    ClockinPageController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Clockin', 'Employee'];

    function ClockinPageController($scope, $rootScope, $stateParams, entity, Clockin, Employee) {
        var vm = this;
        vm.workdays = entity;
    }
})();
