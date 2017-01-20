(function() {
    'use strict';

    angular
        .module('clockinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('employee', {
                parent: 'entity',
                url: '/employee?hidden&page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'clockinApp.employee.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/employee/employees.html',
                        controller: 'EmployeeController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    hidden: {
                        value: '0',
                        squash: true
                    },
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'socialIdentificationNumber,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                        return {
                            hidden: $stateParams.hidden,
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employee');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('employee-detail', {
                parent: 'entity',
                url: '/employee/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'clockinApp.employee.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/employee/employee-detail.html',
                        controller: 'EmployeeDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employee');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Employee', function($stateParams, Employee) {
                        return Employee.get({
                            id: $stateParams.id
                        }).$promise;
                    }],
                    previousState: ["$state", function($state) {
                        var currentStateData = {
                            name: $state.current.name || 'employee',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('employee-detail.edit', {
                parent: 'employee-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/employee/employee-dialog.html',
                        controller: 'EmployeeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Employee', function(Employee) {
                                return Employee.get({
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
            .state('employee.edit', {
                parent: 'employee',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/employee/employee-dialog.html',
                        controller: 'EmployeeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Employee', function(Employee) {
                                return Employee.get({
                                    id: $stateParams.id
                                }).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('employee', null, {
                            reload: true
                        });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();