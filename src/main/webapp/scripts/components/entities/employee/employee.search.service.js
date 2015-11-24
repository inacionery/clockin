'use strict';

angular.module('clockinApp')
    .factory('EmployeeSearch', function ($resource) {
        return $resource('api/_search/employees/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
