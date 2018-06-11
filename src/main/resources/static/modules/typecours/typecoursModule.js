angular.module("notesApp.typecours", ['notesApp.typecours.controllers', 'notesApp.typecours.services']);
angular.module("notesApp.typecours").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("typecours", {
        url: '/typecours',
        controller: 'TypecoursController',
        templateUrl: 'modules/typecours/views/liste.html'
    });
});