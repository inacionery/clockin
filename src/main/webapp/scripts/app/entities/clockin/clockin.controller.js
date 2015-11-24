'use strict';

angular.module('clockinApp')
    .controller('ClockinController', function ($scope, Clockin, ClockinSearch, ParseLinks) {
        $scope.clockins = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Clockin.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.clockins = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Clockin.get({id: id}, function(result) {
                $scope.clockin = result;
                $('#deleteClockinConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Clockin.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteClockinConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ClockinSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.clockins = result;
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
            $scope.clockin = {
                sequentialRegisterNumber: null,
                dateTime: null,
                registryType: null,
                id: null
            };
        };
    });
