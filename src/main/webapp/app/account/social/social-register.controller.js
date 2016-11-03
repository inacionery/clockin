(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('SocialRegisterController', SocialRegisterController);

    SocialRegisterController.$inject = ['$filter', '$stateParams', '$state'];

    function SocialRegisterController ($filter, $stateParams, $state) {
        var vm = this;

        vm.success = $stateParams.success;
        vm.error = !vm.success;
        vm.provider = $stateParams.provider;
        vm.providerLabel = $filter('capitalize')(vm.provider);
        vm.success = $stateParams.success;

        $state.go('home');
    }
})();
