 'use strict';

angular.module('clockinApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-clockinApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-clockinApp-params')});
                }
                return response;
            }
        };
    });
