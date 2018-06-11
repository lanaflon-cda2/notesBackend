angular.module("notesApp.parcours", ['notesApp.parcours.controllers', 'notesApp.parcours.services']);
angular.module("notesApp.parcours").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("parcours", {
        url: '/parcours',
        controller: 'ParcoursController',
        templateUrl: 'modules/parcours/views/liste.html'
    });
});