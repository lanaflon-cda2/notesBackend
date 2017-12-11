angular.module("notesApp.niveaux.services", []).factory('Niveau', function ($resource) {
    return $resource("api/niveaux/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 