'use strict';

angular.module('clockinApp')
    .controller('EmployeeController', function ($scope, Employee, EmployeeSearch, ParseLinks) {
        $scope.employees = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Employee.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.employees = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
                $('#deleteEmployeeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Employee.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEmployeeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EmployeeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employees = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.employee = {
                socialIdentificationNumber: null,
                plannedDailyHours: null,
                id: null
            };
        };
    });
