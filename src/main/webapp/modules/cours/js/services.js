angular.module("notesApp.cours.services", []).factory('Cours', function($resource) {
	return $resource("/notesBackend/api/cours/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 