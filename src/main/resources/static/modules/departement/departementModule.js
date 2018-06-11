angular.module("notesApp.departements", ['notesApp.departements.controllers', 'notesApp.departements.services']);
angular.module("notesApp.departements").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("departements", {
        url: '/departements',
        controller: 'DepartementController',
        templateUrl: 'modules/departement/views/liste.html'
    });
});