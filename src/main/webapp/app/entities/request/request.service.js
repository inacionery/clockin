(function() {
    'use strict';
    angular
        .module('clockinApp')
        .factory('Request', Request);

    Request.$inject = ['$resource'];

    function Request($resource) {
        var resourceUrl = 'api/request/:year/:semester';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true
            }
        });
    }
})();