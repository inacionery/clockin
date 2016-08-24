(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
    
    var today = new Date();

    function stateConfig($stateProvider) {
        $stateProvider
            .state('report', {
                parent: 'entity',
                url: '/report/{year}/{semester}',
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
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'clockinApp.report.home.title',
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/report/report.html',
                        controller: 'ReportController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('report');
                        $translatePartialLoader.addPart('registryType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Report', function($stateParams, Report) {
                        return Report.query({
                            year: $stateParams.year,
                            semester: $stateParams.semester
                        }).$promise;
                    }]
                }
            });
    }
})();