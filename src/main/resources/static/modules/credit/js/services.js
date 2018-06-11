angular.module("notesApp.credits.services", []).factory('Credit', function($resource) {
	return $resource("api/credits/:id", {
		id : '@id'
	}, {
		update : {
			method : 'PUT'
		}
	});
}); 


