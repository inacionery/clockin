(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    var today = new Date();

    function stateConfig($stateProvider) {
        $stateProvider
            .state('manager', {
                parent: 'app',
                url: '/manager/{year}/{semester}',
                params: {
                    year: {
                        value: eval(today.getFullYear()).toString(),
                        squash: false,
                    },
                    semester: {
                        value: eval(today.getMonth() > 5 ? 1 : 0).toString(),
                        squash: false,
                    },
                    manager: {},
                },
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'clockinApp.manager.home.title',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/manager/manager.html',
                        controller: 'ManagerController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('manager');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Manager', function($stateParams, Manager) {
                        return Manager.query({
                            year: $stateParams.year,
                            semester: $stateParams.semester
                        }).$promise;
                    }]
                }
            });
    }
})();