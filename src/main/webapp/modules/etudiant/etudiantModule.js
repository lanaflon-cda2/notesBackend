angular.module("notesApp.etudiants",['notesApp.etudiants.controllers','notesApp.etudiants.services']);
angular.module("notesApp.etudiants")
.config(function($stateProvider,$locationProvider){
	$stateProvider.state("eliste", {
		url:'/etudiants',
		controller:'EtudiantController',
		templateUrl:'modules/etudiant/views/liste.html'
	}).state("eimportation", {
		url:'/etudiants/importation',
		controller:'EtudiantImportController',
		templateUrl:'modules/etudiant/views/importation.html'
	}).state("inscription", {
		url:'/etudiants',
		controller:'EtudiantController',
		templateUrl:'modules/etudiant/views/liste.html'
	});
});