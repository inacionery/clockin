'use strict';

angular.module('clockinApp')
    .factory('Clockin', function ($resource, DateUtils) {
        return $resource('api/clockins/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateTime = DateUtils.convertDateTimeFromServer(data.dateTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
