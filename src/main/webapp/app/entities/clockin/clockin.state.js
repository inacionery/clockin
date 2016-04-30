(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('clockin', {
            parent: 'entity',
            url: '/clockin',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.clockin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clockin/clockins.html',
                    controller: 'ClockinController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clockin');
                    $translatePartialLoader.addPart('registryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
