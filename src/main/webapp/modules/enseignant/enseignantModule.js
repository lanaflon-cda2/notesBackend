angular.module("notesApp.enseignants", ['notesApp.enseignants.controllers', 'notesApp.enseignants.services']);
angular.module("notesApp.enseignants").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("enseignants", {
        url: '/enseignants',
        controller: 'EnseignantController',
        templateUrl: 'modules/enseignant/views/liste.html'
    });
});