(function() {
    'use strict';

    angular
        .module('clockinApp', [
            'ngStorage',
            'ngMaterial',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', '$window', '$rootScope'];

    function run(stateHandler, translationHandler, $window, $rootScope) {
        stateHandler.initialize();
        translationHandler.initialize();

        $rootScope.$on('$locationChangeStart', function(event, toState, toParams, fromState, fromParams) {
            $rootScope.firstName = $window.localStorage.firstName;
        });
    }
})();