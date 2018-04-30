angular.module("notesApp.home",['notesApp.home.controllers','notesApp.home.services']);
angular.module("notesApp.home").config(function($stateProvider,$locationProvider){
	$stateProvider.state("home", {
		url:'/home',
		controller:'HomeController',
		templateUrl:'modules/home/views/home.html'
	});
});

