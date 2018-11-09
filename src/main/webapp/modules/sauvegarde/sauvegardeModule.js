angular.module("notesApp.sauvegardes", ['notesApp.sauvegardes.controllers', 'notesApp.sauvegardes.services']);
angular.module("notesApp.sauvegardes").config(function ($stateProvider) {
    $stateProvider.state("sauvegardes", {
        url: '/sauvegarde',
        controller: 'SauvegardeController',
        templateUrl: 'modules/sauvegarde/views/sauvegarde.html'
    });
});