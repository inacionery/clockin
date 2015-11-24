'use strict';

angular.module('clockinApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employee', {
                parent: 'entity',
                url: '/employees',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'clockinApp.employee.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employee/employees.html',
                        controller: 'EmployeeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employee');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('employee.detail', {
                parent: 'entity',
                url: '/employee/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'clockinApp.employee.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employee/employee-detail.html',
                        controller: 'EmployeeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employee');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Employee', function($stateParams, Employee) {
                        return Employee.get({id : $stateParams.id});
                    }]
                }
            })
            .state('employee.new', {
                parent: 'employee',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/employee/employee-dialog.html',
                        controller: 'EmployeeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    socialIdentificationNumber: null,
                                    plannedDailyHours: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('employee', null, { reload: true });
                    }, function() {
                        $state.go('employee');
                    })
                }]
            })
            .state('employee.edit', {
                parent: 'employee',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/employee/employee-dialog.html',
                        controller: 'EmployeeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Employee', function(Employee) {
                                return Employee.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employee', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
