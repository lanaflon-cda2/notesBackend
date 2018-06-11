angular.module("notesApp.niveaux",['notesApp.niveaux.controllers','notesApp.niveaux.services']);
angular.module("notesApp.niveaux").config(function($stateProvider,$locationProvider){
	$stateProvider.state("niveaux", {
		url:'/niveaux',
		controller:'NiveauController',
		templateUrl:'modules/niveau/views/liste.html'
	});
});