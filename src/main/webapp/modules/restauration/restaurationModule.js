angular.module("notesApp.restaurations", ['notesApp.restaurations.controllers', 'notesApp.restaurations.services']);
angular.module("notesApp.restaurations").config(function ($stateProvider) {
    $stateProvider.state("restaurations", {
        url: '/restauration',
        controller: 'RestaurationController',
        templateUrl: 'modules/restauration/views/restauration.html'
    });
});