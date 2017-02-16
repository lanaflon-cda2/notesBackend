angular.module("notesApp.semestres", ['notesApp.semestres.controllers', 'notesApp.semestres.services']);
angular.module("notesApp.semestres").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("semestres", {
        url: '/semestres',
        controller: 'SemestreController',
        templateUrl: 'modules/semestre/views/liste.html'
    });
});