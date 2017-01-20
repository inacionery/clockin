(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('email', {
                parent: 'entity',
                url: '/email?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'clockinApp.email.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/email/emails.html',
                        controller: 'EmailController',
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
                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('email');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('email-detail', {
                parent: 'entity',
                url: '/email/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'clockinApp.email.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/email/email-detail.html',
                        controller: 'EmailDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('email');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Email', function($stateParams, Email) {
                        return Email.get({
                            id: $stateParams.id
                        }).$promise;
                    }],
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'email',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('email-detail.edit', {
                parent: 'email-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Email', function(Email) {
                                return Email.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('^', {}, {
                            reload: false
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('email.new', {
                parent: 'email',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function() {
                                return {
                                    subject: null,
                                    content: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('email', null, {
                            reload: 'email'
                        });
                    }, function() {
                        $state.go('email');
                    });
                }]
            })
            .state('email.edit', {
                parent: 'email',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Email', function(Email) {
                                return Email.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('email', null, {
                            reload: 'email'
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('email.delete', {
                parent: 'email',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/email/email-delete-dialog.html',
                        controller: 'EmailDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Email', function(Email) {
                                return Email.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('email', null, {
                            reload: 'email'
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();