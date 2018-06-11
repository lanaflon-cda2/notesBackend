angular.module("notesApp.utilisateurs.services", []).factory('Utilisateur', function ($resource) {
    return $resource("api/utilisateurs/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
});


