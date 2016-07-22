(function() {
    'use strict';
    angular
        .module('clockinApp')
        .factory('Clockin', Clockin);

    Clockin.$inject = ['$resource', 'DateUtils'];

    function Clockin($resource, DateUtils) {
        var resourceUrl = 'api/clockin/:year/:semester';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function(data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT'
            }
        });
    }
})();