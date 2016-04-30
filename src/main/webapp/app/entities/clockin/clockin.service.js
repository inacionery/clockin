(function() {
    'use strict';
    angular
        .module('clockinApp')
        .factory('Clockin', Clockin);

    Clockin.$inject = ['$resource', 'DateUtils'];

    function Clockin ($resource, DateUtils) {
        var resourceUrl =  'api/workdays/{date}';

        return $resource(resourceUrl, {}, {
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
    }
})();
