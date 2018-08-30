angular.module('notesApp.notes', ['notesApp.notes.controllers', 'notesApp.notes.services', 'ngAnimate'])
angular.module('notesApp.notes').config(function ($stateProvider) {
  $stateProvider.state('nimportation', {
    url: '/importation',
    abstract: true,
    controller: 'NoteImportationController',
    templateUrl: 'modules/note/views/importation.html'
  }).state('nexportation', {
    url: '/exportation',
    controller: 'NoteController',
    templateUrl: 'modules/note/views/exportation.html'
  }).state('nmodification', {
    url: '/modification',
    controller: 'NoteModificationController',
    templateUrl: 'modules/note/views/modification.html'
  }).state('nimportation.etape1', {
    url: '/etape1',
    views: {
      'importation': {
        templateUrl: 'modules/note/views/step1.html',
        controller: 'NoteImportationOneController'
      }
    }
  }).state('nimportation.etape2', {
    url: '/etape2',
    views: {
      'importation': {
        templateUrl: 'modules/note/views/step2.html',
        controller: 'NoteImportation2Controller'
      }
    }
  }).state('nimportation.etape3', {
    url: '/etape3',
    views: {
      'importation': {
        templateUrl: 'modules/note/views/step3.html',
        controller: 'NoteImportation3Controller'
      }
    }
  }).state('nimportation.etape4', {
    url: '/etape4',
    views: {
      'importation': {
        templateUrl: 'modules/note/views/step4.html',
        controller: 'NoteImportation4Controller'
      }
    }
  })
})
