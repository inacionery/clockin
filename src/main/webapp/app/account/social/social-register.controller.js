(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('SocialRegisterController', SocialRegisterController);

    SocialRegisterController.$inject = ['$filter', '$stateParams', '$state', 'Principal'];

    function SocialRegisterController ($filter, $stateParams, $state, Principal) {
        var vm = this;

        vm.success = $stateParams.success;
        vm.error = !vm.success;
        vm.provider = $stateParams.provider;
        vm.providerLabel = $filter('capitalize')(vm.provider);
        vm.success = $stateParams.success;

        $state.go('home');
    }
})();
