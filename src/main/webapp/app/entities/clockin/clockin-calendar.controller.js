(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinCalendarController', ClockinCalendarController);

    ClockinCalendarController.$inject = ['ClockinCalendar','entity','$scope','$state','$locale',
    '$rootScope','$stateParams','$mdMedia','$mdDialog','$filter','DateUtils'];

    function ClockinCalendarController (ClockinCalendar, entity, $scope, $state, $locale, $rootScope, $stateParams,
    $mdMedia, $mdDialog, $filter, DateUtils) {
        var vm = this;

        var month = eval($stateParams.month);
        var year = eval($stateParams.year);

        vm.previousYear = year;
        vm.previousMonth = (month - 1);
        vm.nextYear = year;
        vm.nextMonth = (month + 1);
        if (month == 1){
        	vm.previousYear = (year - 1);
        	vm.previousMonth = 12;
        } else if (month == 12){
        	 vm.nextYear = (year + 1);
             vm.nextMonth = 1;
        }
        vm.date = new Date(vm.previousYear, vm.previousMonth);

        vm.workdays = entity;

        vm.shortDays = $locale.DATETIME_FORMATS.SHORTDAY.map(function(shortDay){
            return {
                header: $filter('capitalize')(shortDay),
                fstChar: shortDay.charAt(0).toUpperCase()
            };
        });

        function weekDay(strDate) {
            var date = DateUtils.convertLocalDateFromServer(strDate);
            var weekDay = $locale.DATETIME_FORMATS.DAY[date.getDay()];

            return $filter('capitalize')(weekDay);
        }

        function padDate(strDate) {
            var date = DateUtils.convertLocalDateFromServer(strDate);

            if (date.getDate() < 10) {
                return "0" + date.getDate();
            }

            return date.getDate();
        }

        $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
        $scope.showTabDialog = function(event, weekCell) {

            if (weekCell.clockinValues.length > 0) {
                $mdDialog.show({
                    controller: DialogController,
                    templateUrl: '/app/entities/clockin/clockin-calendar.tab-dialog.html',
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    disableParentScroll: false,
                    locals: {
                        clockinValues: weekCell.clockinValues,
                        weekDay: weekDay(weekCell.date),
                        padDate: padDate(weekCell.date)
                    }
                });
           }
        };

        function DialogController($scope, $mdDialog, clockinValues, weekDay, padDate) {
            $scope.clockinValues = clockinValues;
            $scope.weekDay = weekDay;
            $scope.padDate = padDate;
            $scope.hide = function() {
              $mdDialog.hide();
            };
        }
    }
})();
