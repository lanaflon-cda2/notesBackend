angular.module("notesApp.credits.services", []).factory('Credit', function($resource) {
	return $resource("/notesBackend/api/credits/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 


