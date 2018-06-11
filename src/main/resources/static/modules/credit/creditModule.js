angular.module("notesApp.credits",['notesApp.credits.controllers','notesApp.credits.services']);
angular.module("notesApp.cycles").config(function($stateProvider,$locationProvider){
	$stateProvider.state("credits", {
		url:'/credits',
		controller:'CreditController',
		templateUrl:'modules/credit/views/liste.html'
	});
});


