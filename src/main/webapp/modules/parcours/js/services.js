angular.module("notesApp.parcours.services", []).factory('Parcours', function ($resource) {
    return $resource("/notesBackend/api/parcours/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}); 