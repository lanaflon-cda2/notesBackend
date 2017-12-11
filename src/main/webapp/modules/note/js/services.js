angular.module("notesApp.notes.services", []).factory('Note', function ($resource) {
    return $resource("api/notes/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        },
        toto: {
            method: 'GET',
            url:"api/options/:first/:second"
        }
    });
});