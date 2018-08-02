angular.module("notesApp.utilisateurs.services", []).factory('Utilisateur', function ($resource) {
    return $resource("api/utilisateurs/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
        ,
        changePassword: {
            method: 'POST',
            url: "api/utilisateurs/:id/changePassword"
        },
        logged: {
            method: 'GET',
            url: "api/utilisateurs/logged"
        },
        activate: {
            method: 'PUT',
            url: "api/utilisateurs/:id/activate"
        },
        reset: {
            method: 'PUT',
            url: "api/utilisateurs/:id/reset"
        }
    }
    );
});


