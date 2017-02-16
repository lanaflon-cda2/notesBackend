angular.module("notesApp.semestres.services", []).factory('Semestre', function ($resource) {
    return $resource("/notesBackend/api/semestres/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 