'use strict';

angular.module('clockinApp')
    .controller('ClockinDetailController', function ($scope, $rootScope, $stateParams, entity, Clockin, Employee) {
        $scope.clockin = entity;
        $scope.load = function (id) {
            Clockin.get({id: id}, function(result) {
                $scope.clockin = result;
            });
        };
        var unsubscribe = $rootScope.$on('clockinApp:clockinUpdate', function(event, result) {
            $scope.clockin = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
