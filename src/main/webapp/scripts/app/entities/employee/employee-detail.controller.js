'use strict';

angular.module('clockinApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, Clockin, User) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
            });
        };
        var unsubscribe = $rootScope.$on('clockinApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
