angular.module("notesApp.deliberations.controllers", []).controller("DeliberationController", ["$scope", "$http", "Annee","Departement", "Niveau",
    function($scope, $http, Annee, Departement, Niveau){
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });
        $scope.selection = true;
        var niv = Niveau.query(function () {
            $scope.niveaux = niv;
        });
        var anns = Annee.query(function () {
            $scope.annees = anns;
        });
        $scope.infInclusive = false;
        $scope.supInclusive = false;
        $scope.updateOptions = function () {
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('api/options/' + $scope.departement.id + '/' + $scope.niveau.id).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
            }
        };
        $scope.changerOption = function () {
            if (($scope.option) && ($scope.niveau)) {
                $http.get('api/cours/' + $scope.niveau.id + '/' + $scope.option.id).success(function (data, status, config, headers) {
                    $scope.cours = data;
                });
            }
        };
        
        $scope.proceder = function(){
            var fd = new FormData();
            //Take the first selected file
            fd.append("departementId", $scope.departement.id);
            fd.append("niveauId", $scope.niveau.id);
            fd.append("optionId", $scope.option.id);
            fd.append("coursId", $scope.cour.id);
            fd.append("anneeId", $scope.annee);
            fd.append("borneInf", $scope.borneInf);
            fd.append("borneSup", $scope.borneSup);
            fd.append("infInclusive", $scope.infInclusive);
            fd.append("supInclusive", $scope.supInclusive);
            fd.append("moyenne", $scope.moyenneFinale);
            if ($scope.session)
                fd.append("session", $scope.session);
            $http.post('api/notes/deliberation', fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (data) {
                $scope.selection = false;
                $scope.deliberations = data; 
            }).error(function () {
            });
            
        };
        
        $scope.valider = function(){
            var fd = new FormData();
            //Take the first selected file
            fd.append("departementId", $scope.departement.id);
            fd.append("niveauId", $scope.niveau.id);
            fd.append("optionId", $scope.option.id);
            fd.append("coursId", $scope.cour.id);
            fd.append("anneeId", $scope.annee);
            fd.append("borneInf", $scope.borneInf);
            fd.append("borneSup", $scope.borneSup);
            fd.append("infInclusive", $scope.infInclusive);
            fd.append("supInclusive", $scope.supInclusive);
            fd.append("moyenne", $scope.moyenneFinale);
            if ($scope.session)
                fd.append("session", $scope.session);
            $http.put('api/notes/deliberation', fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (data) {
                $scope.deliberations = null;
                $scope.nombre = data;
            }).error(function () {
            });
            
        };
    }]);


