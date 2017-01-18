(function() {
    'use strict';

    angular
        .module('clockinApp')
        .directive('isEmployee', isEmployee);

    isEmployee.$inject = ['Principal'];

    function isEmployee(Principal) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var setVisible = function () {
                    element.removeClass('hidden');
                },
                setHidden = function () {
                    element.addClass('hidden');
                },
                defineVisibility = function (reset) {

                    var result;
                    if (reset) {
                        setVisible();
                    }

                    result = Principal.isEmployee();
                    if (result) {
                        setVisible();
                    } else {
                        setHidden();
                    }
                };

            defineVisibility(true);

            scope.$watch(function() {
                return Principal.isAuthenticated();
            }, function() {
                defineVisibility(true);
            });

        }
    }
})();
