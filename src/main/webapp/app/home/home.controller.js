(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$window'];

    function HomeController ($scope, Principal, LoginService, $state, $window) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

				$window.localStorage.firstName = vm.account.firstName;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
