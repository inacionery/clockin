(function() {
    'use strict';

    angular
        .module('clockinApp')
        .directive('hasNotAuthority', hasNotAuthority);

    hasNotAuthority.$inject = ['Principal'];

    function hasNotAuthority(Principal) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var authority = attrs.hasNotAuthority.replace(/\s+/g, '');

            var setVisible = function () {
                    element.removeClass('hidden');
                },
                setHidden = function () {
                    element.addClass('hidden');
                },
                defineVisibility = function (reset) {

                    if (reset) {
                    	setHidden();
                    }

                    Principal.hasAuthority(authority)
                        .then(function (result) {
                            if (result) {
                            	setHidden();
                            } else {
                            	setVisible();
                            }
                        });
                };

            if (authority.length > 0) {
                defineVisibility(false);

                scope.$watch(function() {
                    return Principal.isAuthenticated();
                }, function() {
                    defineVisibility(true);
                });
            }
        }
    }
})();
