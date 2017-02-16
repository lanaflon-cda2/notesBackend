angular.module("notesApp.annees.services", []).factory('Annee', function ($resource) {
    return $resource("/notesBackend/api/annees/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
});

