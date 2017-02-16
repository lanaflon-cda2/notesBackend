angular.module("notesApp.uniteenseignements.services", []).factory('UniteEns', function ($resource) {
    return $resource("/notesBackend/api/uniteEns/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}).factory('ListeCours', function ($http) {
    return {
        getCoursUe: function (ue) {
            return $http.get('/notesBackend/api/uniteEns/' + ue+ '/cours').then(function (response) {
                return response.data;
            });
        }
    };
}); 