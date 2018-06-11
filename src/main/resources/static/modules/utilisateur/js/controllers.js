angular.module("notesApp.utilisateurs.controllers", []).controller("UtilisateurController", ["$scope", "$modal", "$log", "Utilisateur", "Departement",
    function ($scope, $modal, $log, Utilisateur, Departement) {
        var uts = Utilisateur.query(function () {
            $scope.utilisateurs = uts;
        });

        var deps = Departement.query(function () {
            $scope.departements = deps;
        });

        $scope.afficherFenetre = function () {
            var modelInstance = $modal.open({
                templateUrl: 'modules/utilisateur/views/nouveau.html',
                controller: 'UtilisateurFenetreController',
                controllerAs: 'utilisateur',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt = {};
                        tt.departements = $scope.departements;
                        tt.element = {};
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (resultat) {
                var item = resultat.element;
                if (item.id !== undefined) {
                    var toto = Utilisateur.save(item, function () {
                        $scope.utilisateurs.push(toto);
                    });
                }
            }, function () {

            });
        };
    }]).controller("UtilisateurFenetreController", ["$scope", "$modalInstance","element", function ($scope, $modalInstance, element) {
        $scope.element = element.element;
        $scope.departements = element.departements;
        $scope.tags = [];
        $scope.loadItems = function (query) {
            return _.filter($scope.departements, function (de) {
                return de.code.toLowerCase().indexOf(query.toLowerCase()) > -1;
            });
        };

        $scope.valider = function () {
            var resultat = {};
            $scope.element.departements = $scope.tags;
            resultat.item = $scope.element;
            $modalInstance.close(resultat);
        };
    }]);





