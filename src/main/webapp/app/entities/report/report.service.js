(function() {
    'use strict';
    angular
        .module('clockinApp')
        .factory('Report', Report);

    Report.$inject = ['$resource'];

    function Report ($resource) {
        var resourceUrl = 'api/report/:year/:semester';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
