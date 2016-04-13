(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinController', ClockinController);

    ClockinController.$inject = ['$scope', '$state', 'Clockin', 'ClockinSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function ClockinController ($scope, $state, Clockin, ClockinSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
    }
})();
