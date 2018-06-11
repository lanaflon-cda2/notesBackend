angular.module("notesApp.options", ['notesApp.options.controllers', 'notesApp.options.services','ngAnimate']);
angular.module("notesApp.options").config(function ($stateProvider, $locationProvider) {
    $stateProvider.state("options", {
        url: '/options',
        controller: 'OptionController',
        templateUrl: 'modules/option/views/liste.html'
    });
});