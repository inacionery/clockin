(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    var today = new Date();

    function stateConfig($stateProvider) {
        $stateProvider
        .state('request', {
            parent: 'app',
            url: '/request/{year}/{semester}',
            params: {
                year: {
                    value: eval(today.getFullYear()).toString(),
                    squash: false,
                },
                semester: {
                    value: eval(today.getMonth() > 5 ? 1 : 0).toString(),
                    squash: false,
                },
                request: {},
            },
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.request.home.title',
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/request/request.html',
                    controller: 'RequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('request');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Request', function($stateParams, Request) {
                    return Request.query({
                        year: $stateParams.year,
                        semester: $stateParams.semester
                    }).$promise;
                }]
            }
        });
    }
})();