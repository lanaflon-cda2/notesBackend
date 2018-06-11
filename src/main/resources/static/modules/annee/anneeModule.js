angular.module("notesApp.annees", ['notesApp.annees.controllers', 'notesApp.annees.services']);
angular.module("notesApp.annees").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("annees", {
        url: '/annees',
        controller: 'AnneeController',
        templateUrl: 'modules/annee/views/liste.html'
    });
});