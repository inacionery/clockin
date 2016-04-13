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
            url: '/clockin?page&sort&search',
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
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clockin');
                    $translatePartialLoader.addPart('registryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('clockin-detail', {
            parent: 'entity',
            url: '/clockin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clockinApp.clockin.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clockin/clockin-detail.html',
                    controller: 'ClockinDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clockin');
                    $translatePartialLoader.addPart('registryType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Clockin', function($stateParams, Clockin) {
                    return Clockin.get({id : $stateParams.id});
                }]
            }
        })
        .state('clockin.new', {
            parent: 'clockin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clockin/clockin-dialog.html',
                    controller: 'ClockinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sequentialRegisterNumber: null,
                                dateTime: null,
                                registryType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('clockin', null, { reload: true });
                }, function() {
                    $state.go('clockin');
                });
            }]
        })
        .state('clockin.edit', {
            parent: 'clockin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clockin/clockin-dialog.html',
                    controller: 'ClockinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Clockin', function(Clockin) {
                            return Clockin.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('clockin', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
