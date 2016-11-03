(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    var today = new Date();

    function stateConfig($stateProvider) {
        $stateProvider
            .state('clockin', {
                parent: 'app',
                url: '/clockin/{year}/{semester}',
                params: {
                    year: {
                        value: eval(today.getFullYear()).toString(),
                        squash: false,
                    },
                    semester: {
                        value: eval(today.getMonth() > 5 ? 1 : 0).toString(),
                        squash: false,
                    },
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'clockinApp.clockin.home.title',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/clockin/clockin.html',
                        controller: 'ClockinController',
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
                    entity: ['$stateParams', 'Clockin', function($stateParams, Clockin) {
                        return Clockin.query({
                            year: $stateParams.year,
                            semester: $stateParams.semester
                        }).$promise;
                    }]
                }
            });
    }
})();
