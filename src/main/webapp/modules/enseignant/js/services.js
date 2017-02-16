angular.module("notesApp.enseignants.services", []).factory('Enseignant', function($resource) {
	return $resource("/notesBackend/api/enseignants/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 