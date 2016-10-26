(function() {
    'use strict';
    angular
        .module('clockinApp')
        .factory('Manager', Manager);

    Manager.$inject = ['$resource'];

    function Manager ($resource) {
        var resourceUrl = 'api/manager/:year/:semester';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
