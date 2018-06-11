angular.module("notesApp.services", []).factory('Login', function($resource) {
	return $resource("/");
}); ; 