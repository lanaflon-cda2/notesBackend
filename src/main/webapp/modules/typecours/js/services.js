angular.module("notesApp.typecours.services", []).factory('TypeCours', function ($resource) {
    return $resource("/notesBackend/api/typeCours/:id", {
        id: '@id'
    }, {
        update: {
            method: 'PUT'
        }
    });
}).service("EvaluationDetailService", function ($http) {
    this.addEvaluation = function (typeCours, evaluation) {
        // invoque une requete de type post vers une url /api/typeCours/idType/evaluations
        var url = "/notesBackend/api/typeCours/" + typeCours.id + "/evaluations";
        return $http.post(url, evaluation);
    };
    this.modifierEvaluation = function (typeCours, evaluation) {
        // invoque une requete de type put vers une url /api/typeCours/idType/evaluations/idEvaluation
        var url = "/notesBackend/api/typeCours/" + typeCours.id + "/evaluations/" + evaluation.id;
        return $http.put(url, evaluation);
    };

    this.supprimerEvaluation = function (typeCours, evaluation) {
        // invoque une requete de type delete vers une url /api/typeCours/idType/evaluation/idEvaluation
        var url = "/notesBackend/api/typeCours/" + typeCours.id + "/evaluations/" + evaluation.id;
        return $http.delete(url);
    };

}); 