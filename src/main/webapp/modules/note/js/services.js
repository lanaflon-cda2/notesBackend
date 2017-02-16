angular.module("notesApp.notes.services", []).factory('Note', function ($resource) {
    return $resource("/notesBackendapi/notes/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        },
        toto: {
            method: 'GET',
            url:"/notesBackend/api/options/:first/:second"
        }
    });
});