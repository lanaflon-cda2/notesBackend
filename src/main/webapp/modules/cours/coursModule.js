angular.module("notesApp.cours", ['notesApp.cours.controllers', 'notesApp.cours.services']);
angular.module("notesApp.cours").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("cours", {
        url: '/cours',
        controller: 'CoursController',
        templateUrl: 'modules/cours/views/liste.html'
    });
});