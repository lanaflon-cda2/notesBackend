angular.module("notesApp.evaluations.services", []).factory('Evaluation', function($resource) {
	return $resource("api/evaluations/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 