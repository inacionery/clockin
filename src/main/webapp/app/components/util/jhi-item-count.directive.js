(function() {
    'use strict';

    var jhiItemCount = {
        template: '<div class="info">' +
                    'Exibindo {{(($ctrl.page-1) * 20)==0 ? 1:(($ctrl.page-1) * 20)}} - ' +
                    '{{($ctrl.page * 20) < $ctrl.queryCount ? ($ctrl.page * 20) : $ctrl.queryCount}} ' +
                    'de {{$ctrl.queryCount}} resultados.' +
                '</div>',
        bindings: {
            page: '<',
            queryCount: '<total'
        }
    };

    angular
        .module('clockinApp')
        .component('jhiItemCount', jhiItemCount);
})();
