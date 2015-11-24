'use strict';

angular.module('clockinApp').controller('ClockinDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Clockin',
        function($scope, $stateParams, $modalInstance, entity, Clockin) {

        $scope.clockin = entity;
        $scope.load = function(id) {
            Clockin.get({id : id}, function(result) {
                $scope.clockin = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clockinApp:clockinUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.clockin.id != null) {
                Clockin.update($scope.clockin, onSaveFinished);
            } else {
                Clockin.save($scope.clockin, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
