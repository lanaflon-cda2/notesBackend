angular.module("notesApp.uniteenseignements", ['notesApp.uniteenseignements.controllers', 'notesApp.uniteenseignements.services']);
angular.module("notesApp.enseignants").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("uniteEns", {
        url: '/uniteEns',
        controller: 'UniteEnseignementController',
        templateUrl: 'modules/uniteEnseignement/views/liste.html'
    });
});