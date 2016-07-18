(function() {
    'use strict';
    angular
        .module('clockinApp')
        .factory('ClockinTable', ClockinTable)
    	.factory('ClockinCalendar', ClockinCalendar);

    ClockinTable.$inject = ['$resource', 'DateUtils'];

    function ClockinTable ($resource, DateUtils) {
        var resourceUrl =  'api/workdays/:year/:month';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.time = DateUtils.convertDateTimeFromServer(data.time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    };

    ClockinCalendar.$inject = ['$resource', 'DateUtils'];
    
    function ClockinCalendar ($resource, DateUtils) {
    	var resourceUrl =  'api/workdays-calendar/:year/:month';
    	
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
