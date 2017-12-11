angular.module("notesApp.departements.services", []).factory('Departement', function ($resource) {
    return $resource("api/departements/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
});

