angular.module("notesApp.notes", ['notesApp.notes.controllers', 'notesApp.notes.services', 'ngAnimate']);
angular.module("notesApp.notes").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("nimportation", {
        url: '/importation/#tab1',
        controller: 'NoteImportationController',
        templateUrl: 'modules/note/views/importation.html'
    }).state("nexportation", {
        url: '/exportation',
        controller: 'NoteController',
        templateUrl: 'modules/note/views/exportation.html'
    }).state("nmodification", {
        url: '/modification',
        controller: 'NoteModificationController',
        templateUrl: 'modules/note/views/modification.html'        
    // }).state("nimportation.etape1", {
    //     url: '/etape1',
    //     controller: 'NoteImportationController',
    //     templateUrl: 'modules/note/views/importation/etape1.html'        
    // }).state("nimportation.etape2", {
    //     url: '/etape2',
    //     controller: 'NoteImportationController',
    //     templateUrl: 'modules/note/views/importation/etape2.html'        
    // }).state("nimportation.etape3", {
    //     url: '/etape3',
    //     controller: 'NoteImportationController',
    //     templateUrl: 'modules/note/views/importation/etape3.html'        
    // }).state("nimportation.etape4", {
    //     url: '/etape4',
    //     controller: 'NoteImportationController',
    //     templateUrl: 'modules/note/views/importation/etape4.html'        
    });
});