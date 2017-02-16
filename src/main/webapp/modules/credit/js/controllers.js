angular.module("notesApp.credits.controllers", []).controller("CreditController", ["$scope", "$http", "$modal", "Annee", "Departement", "Niveau","Credit",
    function ($scope, $http, $modal, Annee, Departement, Niveau, Credit) {
        var ans = Annee.query(function () {
            $scope.annees = ans;
        });

        var deps = Departement.query(function () {
            $scope.departements = deps;
        });

        var niveaux = Niveau.query(function () {
            $scope.niveaux = niveaux;
        });

        $scope.filtrer = function () {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.option !== undefined)) {
                $http.get('/notesBackend/api/credits/' + $scope.annee.id + '/' + $scope.niveau + '/' + $scope.option).success(function (data) {
                    $scope.credits = data;
                });
            }
        };

        $scope.updateOptionsSemestre = function () {
            if (($scope.niveau !== undefined) && ($scope.departement !== undefined)) {
                $http.get('/notesBackend/api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data) {
                    $scope.options = data;
                });
                $scope.option = undefined;
            }
        };
        $scope.afficherFenetre = function (cle, item) {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.option !== undefined)) {
                var modelInstance = $modal.open({
                    templateUrl: '/modules/credit/views/nouveau.html',
                    controller: 'CreditFenetreController',
                    controllerAs: 'credit',
                    keyboard: true,
                    backdrop: false,
                    resolve: {
                        element: function () {
                            var tt = {};
                            tt.cle = cle;
                            tt.item = item;
                            tt.option = $scope.option;
                            tt.niveau = $scope.niveau;
                            return tt;
                        }
                    }
                });
                modelInstance.result.then(function (resultat) {
                    var item = resultat.item;
                    var cle = resultat.cle;
                    item.academique = $scope.annee;
                    item.semestre = $scope.semestre;
                    if ((item.id !== undefined) && (cle !== null)) {
                        $http.put('/notesBackend/api/credits/' + $scope.niveau + '/' + $scope.option + '/' + item.id, item).success(function (data) {
                            $scope.credits.splice(cle, 1, item);
                        });
                    } else {
                        $http.post('/notesBackend/api/credits/' + $scope.niveau + '/' + $scope.option, item).success(function (data) {
                            $scope.credits.push(data);
                        });
                    }
                }, function () {

                });
            }
        };
        $scope.supprimerProgramme = function (cle, item) {
            if (confirm("Voulez vous vraiment supprimer ce crédit?")) {
                Credit.remove({
                    id: item.id
                }, function () {
                    $scope.credits.splice(cle, 1);
                });
            }
        };
    }]).controller("CreditFenetreController", ["$scope", "$modalInstance", "element", "$http",
    function ($scope, $modalInstance, element, $http) {

        $scope.element = element.item;
        $scope.cle = element.cle;
        $scope.niveau = element.niveau;
        $scope.option = element.option;

        $http.get('/notesBackend/api/cours/' + $scope.niveau + '/' + $scope.option).success(function (data) {
            $scope.cours = data;
        });

        $scope.valider = function () {
            var resultat = {};
            resultat.item = $scope.element;
            resultat.cle = $scope.cle;
            $modalInstance.close(resultat);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]);


