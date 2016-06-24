(function() {
    'use strict';

    angular
        .module('clockinApp')
        .controller('ClockinCalendarPaginationController', ClockinCalendarPaginationController);

    ClockinCalendarPaginationController.$inject = ['Clockin','entity','$scope','$state','$locale',
    '$rootScope','$stateParams','$mdMedia','$mdDialog'];

    function ClockinCalendarPaginationController (Clockin, entity, $scope, $state, $locale, $rootScope, $stateParams,
    $mdMedia, $mdDialog) {
        var vm = this;
        vm.previousYear = eval($stateParams.year);
        vm.previousMonth = (eval($stateParams.month) - 1);
        vm.nextYear = vm.previousYear;
        vm.nextMonth = (eval($stateParams.month) + 1);
        if (vm.previousMonth === 0){
            vm.previousYear = (vm.previousYear - 1);
            vm.previousMonth = 12;
        } else if (vm.nextMonth === 13){
             vm.nextYear = (vm.nextYear + 1);
             vm.nextMonth = 1;
        }

        vm.isWeekend = function(date) {
    		date = new Date(date);
    	 	var day = date.getDay();
    	 	return day == 0 || day == 6;
    	};

        vm.pad = function(n) {
            return (n < 10) ? ("0" + n) : n;
        };

        vm.captalize = function(word) {
            return word.charAt(0).toUpperCase() + word.slice(1);
        };

        // parse a date in yyyy-mm-dd format
        vm.parseDate = function(input) {
            var parts = input.split('-');
            // new Date(year, month [, day [, hours[, minutes[, seconds[, ms]]]]])
            return new Date(parts[0], parts[1] - 1, parts[2]);
        };

        vm.calendarRangeDays = function(date) {
            var fstMonthDay = new Date(date);
            fstMonthDay.setDate(1);

            var lstMonthDay = new Date(date);
            lstMonthDay.setMonth(date.getMonth() + 1);
            lstMonthDay.setDate(0);
            lstMonthDay.setMonth(date.getMonth());

            var fstDisplayableDate = new Date(fstMonthDay);
            fstDisplayableDate.setDate(1 - fstMonthDay.getDay());

            var lstDisplayableDate = new Date(lstMonthDay);
            lstDisplayableDate.setDate(6 - lstMonthDay.getDay());

            if ( fstDisplayableDate.getDate() > fstMonthDay.getDate() )
                fstDisplayableDate.setMonth(fstMonthDay.getMonth() - 1);

            if ( lstDisplayableDate.getDate() < lstMonthDay.getDate() )
                lstDisplayableDate.setMonth(lstMonthDay.getMonth() + 1);

            return {
                first: fstDisplayableDate,
                last: lstDisplayableDate
            };
        };

        //TODO criar uma função para avanção e retornar com os meses.
        //TODO verificar se há dados no presente dia (clicável ou não).
        vm.getWeeksOfMonth = function() {
            var workday = vm.parseDate(entity[0].date);
            var displayableDates = vm.calendarRangeDays(workday);
            var dateCursor = new Date(displayableDates.first);
            var weeks = [];
            var d = 0;

            while (dateCursor < displayableDates.last) {
                var week = [];

                for (var i = 0; i < 7; i++) {
                    week.push({
                        date: vm.pad(dateCursor.getDate()),
                        clockinValues: []
                    });

                    if (dateCursor.getTime() === workday.getTime()) {
                       workday = vm.parseDate(dateList[d].date);
                       week.clockinValues = dateList[d].clockinValues;
                       d = d + 1;
                    }

                    dateCursor.setDate(dateCursor.getDate() + 1);
                }
                weeks.push(week);
            }

            return weeks;
        };

        vm.shortDays = $locale.DATETIME_FORMATS.SHORTDAY.map(function(sd){
            return {
                header: vm.captalize(sd),
                fstChar: sd.charAt(0).toUpperCase()
            };
        });

        $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
        $scope.showTabDialog = function(event, weekCell) {

            if (weekCell.clockinValues.length > 0) {
                $mdDialog.show({
                    controller: DialogController,
                    templateUrl: '/app/entities/clockin/clockin-calendar.tab-dialog.html',
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose:true,
                    locals: {
                        clockinValues: weekCell.clockinValues,
                        dayLabel: $locale.DATETIME_FORMATS.SHORTDAY[weekCell.date]
                    }
                });
           }
        };

        function DialogController($scope, $mdDialog, clockinValues, dayLabel) {
            $scope.clockinValues = clockinValues;
            $scope.dayLabel = dayLabel;
            $scope.hide = function() {
              $mdDialog.hide();
            };
        }

      vm.workdays = entity;
      var fstDateMonth = vm.parseDate(entity[0].date);

      vm.month = {
          year : fstDateMonth.getFullYear(),
          name : vm.captalize($locale.DATETIME_FORMATS.MONTH[fstDateMonth.getMonth()]),
          weeks : vm.getWeeksOfMonth()
      };

    }
})();
