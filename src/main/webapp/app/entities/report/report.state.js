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
                    report: {},
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
            })
            .state('report.send', {
                parent: 'report',
                url: '/send',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/report/report-dialog.html',
                        controller: 'ReportDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Report', function(Report) {
                                return $stateParams.report;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('report', null, {
                            reload: true
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }
})();