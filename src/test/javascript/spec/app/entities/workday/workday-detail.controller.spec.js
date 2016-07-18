'use strict';

describe('Controller Tests', function() {

    describe('Workday Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkday, MockEmployee, MockClockin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkday = jasmine.createSpy('MockWorkday');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockClockin = jasmine.createSpy('MockClockin');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Workday': MockWorkday,
                'Employee': MockEmployee,
                'Clockin': MockClockin
            };
            createController = function() {
                $injector.get('$controller')("WorkdayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clockinApp:workdayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
