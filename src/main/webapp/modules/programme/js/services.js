angular.module("notesApp.programme.services", []).factory('Programme', function ($resource) {
    return $resource("/notesBackend/api/programmes/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}).factory('NiveauxSemestre', function ($http) {
    return {
        getSemestreNiveaux: function (niveau) {
            return $http.get('/notesBackend/api/niveaux/' + niveau + "/semestres").then(function(response) { //then() returns a promise whichis resolved with return value of success callback
                return response.data; ///extraction des smestres data
            });
        }
    };
}); 