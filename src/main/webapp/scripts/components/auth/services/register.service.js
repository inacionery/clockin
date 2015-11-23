'use strict';

angular.module('clockinApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


