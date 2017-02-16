angular.module("notesApp.enseignements.services", []).factory('Enseignement', function ($resource) {
    return $resource("/api/enseignements/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 

