angular.module("notesApp.enseignements.services", []).factory('Enseignement', function ($resource) {
    return $resource("/notesBackend/api/enseignements/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 

