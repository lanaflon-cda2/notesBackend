angular.module("notesApp.utilisateurs", ['notesApp.utilisateurs.controllers', 'notesApp.utilisateurs.services']);
angular.module("notesApp.utilisateurs").config(function ($stateProvider) {
    $stateProvider.state("utilisateurs", {
        url: '/utilisateurs',
        controller: 'UtilisateurController',
        templateUrl: 'modules/utilisateur/views/liste.html'
    });
});


