'use strict';

angular.module('clockinApp').controller('EmployeeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Employee', 'Clockin', 'User',
        function($scope, $stateParams, $modalInstance, entity, Employee, Clockin, User) {

        $scope.employee = entity;
        $scope.clockins = Clockin.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Employee.get({id : id}, function(result) {
                $scope.employee = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clockinApp:employeeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.employee.id != null) {
                Employee.update($scope.employee, onSaveFinished);
            } else {
                Employee.save($scope.employee, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
