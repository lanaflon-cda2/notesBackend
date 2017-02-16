angular.module("notesApp.etudiants.services", []).factory('Etudiant', function ($resource) {
    return $resource("/notesBackend/api/etudiants/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 