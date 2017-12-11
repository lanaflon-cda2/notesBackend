angular.module("notesApp.etudiants.services", []).factory('Etudiant', function ($resource) {
    return $resource("api/etudiants/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 