angular.module("notesApp.cours.services", []).factory('Cours', function($resource) {
	return $resource("api/cours/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 