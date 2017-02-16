angular.module("notesApp.niveaux.services", []).factory('Niveau', function ($resource) {
    return $resource("/notesBackend/api/niveaux/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 