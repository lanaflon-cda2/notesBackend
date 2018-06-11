angular.module("notesApp.evaluations",['notesApp.evaluations.controllers','notesApp.evaluations.services']);
angular.module("notesApp.evaluations").config(function($stateProvider,$locationProvider){
	$stateProvider.state("evaluations", {
		url:'/evaluations',
		controller:'EvaluationController',
		templateUrl:'modules/evaluation/views/liste.html'
	});
});