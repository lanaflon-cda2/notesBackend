angular.module("notesApp.semestres.services", []).factory('Semestre', function ($resource) {
    return $resource("api/semestres/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 