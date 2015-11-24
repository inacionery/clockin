'use strict';

angular.module('clockinApp')
    .factory('ClockinSearch', function ($resource) {
        return $resource('api/_search/clockins/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
