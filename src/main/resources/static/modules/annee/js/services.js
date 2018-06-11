angular.module("notesApp.annees.services", []).factory('Annee', function ($resource) {
    return $resource("api/annees/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
});

