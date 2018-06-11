angular.module("notesApp.deliberations", ['notesApp.deliberations.controllers', 'notesApp.deliberations.services']);
angular.module("notesApp.deliberations").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("ndeliberation", {
        url: '/deliberations',
        controller: 'DeliberationController',
        templateUrl: 'modules/deliberation/views/deliberations.html'
    });
});


