angular.module("notesApp.rapports", ['notesApp.rapports.controllers', 'notesApp.rapports.services']);
angular.module("notesApp.rapports").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("pv", {
        url: '/pv',
        controller: 'ProcesVerbalController',
        templateUrl: 'modules/rapport/views/proces_verbal.html'
    }).state("synthese", {
        url: '/synthese',
        controller: 'SyntheseController',
        templateUrl: 'modules/rapport/views/synthese.html'
    }).state("releves", {
        url: '/releves',
        controller: 'RelevesNoteController',
        templateUrl: 'modules/rapport/views/releves.html'
    });
});


