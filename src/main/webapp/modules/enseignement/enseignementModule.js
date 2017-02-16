angular.module("notesApp.enseignements", ['notesApp.enseignements.controllers', 'notesApp.enseignements.services']);
angular.module("notesApp.departements").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("enseignements", {
        url: '/enseignements',
        controller: 'EnseignementController',
        templateUrl: 'modules/enseignement/views/liste.html'
    });
});