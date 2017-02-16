angular.module("notesApp.options.services", []).factory('Option', function ($resource) {
    return $resource("/notesBackend/api/options/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        },
        toto: {
            method: 'GET',
            url:"/api/options/:first/:second"
        }
    });
}).factory('NiveauxOptions', function ($http) {
    return {
        getOptionsNiveau: function (departement, niveau) {
            return $http.get('/notesBackend/api/options/' + departement + '/' + niveau).then(function(response) { 
                return response.data; 
            });
        }
    };
}); 