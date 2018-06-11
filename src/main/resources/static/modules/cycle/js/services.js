angular.module("notesApp.cycles.services", []).factory('Cycle', function($resource) {
	return $resource("api/cycles/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 