(function() {
    'use strict';

    angular
        .module('clockinApp')
        .factory('ClockinSearch', ClockinSearch);

    ClockinSearch.$inject = ['$resource'];

    function ClockinSearch($resource) {
        var resourceUrl = 'api/_search/clockins/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true
            }
        });
    }
})();