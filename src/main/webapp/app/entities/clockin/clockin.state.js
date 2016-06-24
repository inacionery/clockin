(function() {
    'use strict';
                    /* http://stackoverflow.com/questions/27221222/separate-controller-per-tab-in-angular-material-w-ui-router */
    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('clockin-init-page', {
            parent: 'entity',
            url: '/clockin',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.clockin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clockin/clockin-page.html',
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
        })
        .state('clockin-page', {
            parent: 'entity',
            url: '/workdays/{year}/{month}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.clockin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clockin/clockin-page.html',
                    controller: 'ClockinPageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clockin');
                    $translatePartialLoader.addPart('registryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Clockin', function($stateParams, Clockin) {
                    return Clockin.query({year : $stateParams.year, month : $stateParams.month});
                }]
            }
        })
        .state('clockin-init-calendar', {
            parent: 'entity',
            url: '/clockin-calendar',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.clockin.home.title',
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clockin/clockin-calendar.html',
                    controller: 'ClockinCalendarInitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clockin');
                    $translatePartialLoader.addPart('registryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                 entity: ['$stateParams', 'Clockin', function($stateParams, Clockin) {
                    return Clockin.query({year : $stateParams.year, month : $stateParams.month});
                }]
            }
        })
        .state('clockin-calendar', {
            parent: 'entity',
            url: '/workdays-calendar/{year}/{month}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.clockin.home.title',
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clockin/clockin-calendar.html',
                    controller: 'ClockinCalendarInitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clockin');
                    $translatePartialLoader.addPart('registryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Clockin', function($stateParams, Clockin) {
                    return Clockin.query({year : $stateParams.year, month : $stateParams.month});
                }]
            }
        });
    }
})();
