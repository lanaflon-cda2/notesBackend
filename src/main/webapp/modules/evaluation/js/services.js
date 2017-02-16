angular.module("notesApp.evaluations.services", []).factory('Evaluation', function($resource) {
	return $resource("/notesBackend/api/evaluations/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 