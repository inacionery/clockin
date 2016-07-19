(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    var today = new Date();

    function stateConfig($stateProvider) {
        $stateProvider
            .state('clockin-table', {
                parent: 'entity',
                url: '/workdays/{year}/{month}',
                params: {
                    year: {
                        value: eval(today.getFullYear()).toString(),
                        squash: false,
                    },
                    month: {
                        value: eval(today.getMonth() + 1).toString(),
                        squash: false,
                    },
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'clockinApp.clockin.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/clockin/clockin-table.html',
                        controller: 'ClockinTableController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('clockin');
                        $translatePartialLoader.addPart('registryType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ClockinTable', function($stateParams, ClockinTable) {
                        return ClockinTable.query({
                            year: $stateParams.year,
                            month: $stateParams.month
                        }).$promise;
                    }]
                }
            })
            .state('clockin-calendar', {
                parent: 'entity',
                url: '/workdays-calendar/{year}/{month}',
                params: {
                    year: {
                        value: eval(today.getFullYear()).toString(),
                        squash: false,
                    },
                    month: {
                        value: eval(today.getMonth() + 1).toString(),
                        squash: false,
                    },
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'clockinApp.clockin.home.title',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/clockin/clockin-calendar.html',
                        controller: 'ClockinCalendarController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('clockin');
                        $translatePartialLoader.addPart('registryType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ClockinCalendar', function($stateParams, ClockinCalendar) {
                        return ClockinCalendar.query({
                            year: $stateParams.year,
                            month: $stateParams.month
                        }).$promise;
                    }]
                }
            });
    }
})();