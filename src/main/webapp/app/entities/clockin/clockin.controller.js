(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinController', ClockinController);

    ClockinController.$inject = ['Clockin', 'entity', '$scope', '$state', '$locale',
        '$rootScope', '$stateParams', '$mdMedia', '$mdDialog', '$filter', 'DateUtils'
    ];

    function ClockinController(Clockin, entity, $scope, $state, $locale, $rootScope, $stateParams,
        $mdMedia, $mdDialog, $filter, DateUtils) {
        var vm = this;

        var semester = eval($stateParams.semester);
        var year = eval($stateParams.year);

        vm.previousYear = year;
        vm.previousSemester = (semester - 1);
        vm.nextYear = year;
        vm.nextSemester = (semester + 1);

        if (semester == 0) {
            vm.previousYear = (year - 1);
            vm.previousSemester = 1;
        } else {
            vm.nextYear = (year + 1);
            vm.nextSemester = 0;
        }

        vm.months = entity;

        vm.today = new Date();

        if (!$scope.selectedIndex) {
            $scope.selectedIndex = 1;
        }

        if (year == vm.today.getFullYear() && ((vm.today.getMonth() > 5 ? 1 : 0) == semester)) {
        	$scope.selectedIndex = vm.today.getMonth() - (5 * semester);
        }

        vm.semesterHours = function semesterHours(months) {
            var hours = 0;

            for (var i in months) {
                if (months[i].hours) {
                    hours = hours + months[i].hours;
                }
            }

            return vm.balanceHours(hours);
        };

        vm.selectMonth = function selectMonth(select) {
            $rootScope.selectedIndex = select;
        };

        vm.shortDays = $locale.DATETIME_FORMATS.SHORTDAY.map(function(shortDay) {
            return {
                header: $filter('capitalize')(shortDay),
                fstChar: shortDay.charAt(0).toUpperCase()
            };
        });

        vm.balanceHours = function balanceHours(time) {
            var minus = "";
            if (time < 0) {
                minus = "-";
                time = time * -1;
            }
            var hours = Math.floor(time / 60);
            var minutes = time % 60;
            hours = hours < 10 ? '0' + hours : hours;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            return minus + hours + ":" + minutes;
        };

        vm.firstClockin = function firstClockin(clockins) {
            if (clockins && clockins.length > 1) {
                return [clockins[0], clockins[1]];
            } else if (clockins && clockins.length > 0) {
                return [clockins[0]];
            } else {
                return [];
            }
            return [clockins[0], clockins[1]];
        };

        vm.lastClockin = function lastClockin(clockins) {
            if (clockins && clockins.length > 3) {
                return [clockins[clockins.length - 2], clockins[clockins.length - 1]];
            } else if (clockins && clockins.length > 2) {
                return [clockins[2]];
            } else {
                return [];
            }
        };
    }
})();