(function() {
    'use strict';

    var jhiItemCount = {
        template: '<div class="info">' +
                    'Exibindo {{(($ctrl.page - 1) * $ctrl.itemsPerPage) == 0 ? 1 : (($ctrl.page - 1) * $ctrl.itemsPerPage + 1)}} - ' +
                    '{{($ctrl.page * $ctrl.itemsPerPage) < $ctrl.queryCount ? ($ctrl.page * $ctrl.itemsPerPage) : $ctrl.queryCount}} ' +
                    'de {{$ctrl.queryCount}} resultados.' +
                '</div>',
        bindings: {
            page: '<',
            queryCount: '<total',
            itemsPerPage: '<'
        }
    };

    angular
        .module('clockinApp')
        .component('jhiItemCount', jhiItemCount);
})();
