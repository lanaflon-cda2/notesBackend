angular.module("notesApp.departements.services", []).factory('Departement', function ($resource) {
    return $resource("/notesBackend/api/departements/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
});

