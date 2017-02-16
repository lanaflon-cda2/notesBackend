angular.module("notesApp.cycles",['notesApp.cycles.controllers','notesApp.cycles.services']);
angular.module("notesApp.cycles").config(function($stateProvider,$locationProvider){
	$stateProvider.state("cycles", {
		url:'/cycles',
		controller:'CycleController',
		templateUrl:'modules/cycle/views/liste.html'
	});
});