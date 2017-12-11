angular.module("notesApp.enseignements.controllers", []).controller("EnseignementController", ["$scope", "$http", "$modal", "Enseignement", "Departement", "Annee", "Niveau",
    function ($scope, $http, $modal, Enseignement, Departement, Annee, Niveau) {
        var annees = Annee.query(function () {
            $scope.annees = annees;
        });

        var departements = Departement.query(function () {
            $scope.departements = departements;
        });

        var niveaux = Niveau.query(function () {
            $scope.niveaux = niveaux;
        });
        $scope.enseignements = {};

        $scope.modificationDepartement = function () {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined)) {
                $http.get('api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
                $scope.option = null;
            }
        };

        $scope.filtrer = function () {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.option !== undefined)) {
                $http.get('api/enseignements/' + $scope.annee + '/' + $scope.niveau + '/' + $scope.option).success(function (data, status, config, headers) {
                    $scope.enseignements = data;
                });
            }
        }
        $scope.afficherFenetre = function (cle, item) {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.option !== undefined)) {
                var modelInstance = $modal.open({
                    templateUrl: 'modules/enseignement/views/nouveau.html',
                    controller: 'EnseignementFenetreController',
                    controllerAs: 'depart',
                    keyboard: true,
                    backdrop: false,
                    resolve: {
                        element: function () {
                            var tt = {};
                            tt.item = item;
                            tt.cle = cle;
                            tt.niveau = $scope.niveau;
                            tt.option = $scope.option;
                            tt.annee = $scope.annee;
                            return tt;
                        }
                    }
                });
                modelInstance.result.then(function (resultat) {
                    var item = resultat.item;
                    var cle = resultat.cle;
                    if ((item.id !== undefined) && (cle !== undefined)) {
                        $http.put('api/enseignements/' + $scope.annee + '/' + $scope.niveau + '/' + $scope.option + '/' + item.id, item).success(function (data) {
                            $scope.enseignements.splice(cle, 1, data);
                        });
                    } else {
                        $http.post('api/enseignements/' + $scope.annee + '/' + $scope.niveau + '/' + $scope.option, item).success(function (data) {
                            $scope.enseignements.push(data);
                        });
                    }
                }, function () {

                });
            }
        };
        $scope.supprimerEnseignement = function (cle, item) {
            if (confirm("Voulez vous vraiment supprimer cet enseignement?")) {
                Enseignement.remove({
                    id: item.id
                }, function () {
                    $scope.enseignants.splice(cle, 1);
                });
            }
        };
    }]).controller("EnseignementFenetreController", ["$http","$scope", "$modalInstance", "element", "Enseignant", "Annee", "Enseignant",
    function ($http, $scope, $modalInstance, element, Enseignant, Annee, Enseignant) {
        $scope.element = element.item;
        $scope.cle = element.cle;
        $http.get('api/cours/' + element.niveau + '/' + element.option).success(function (data) {
            $scope.cours = data;
        });
        var enseignants = Enseignant.query(function () {
            $scope.enseignants = enseignants;
        });
        var annees = Annee.query(function () {
            $scope.annees = annees;
        });

        var enseignants = Enseignant.query(function () {
            $scope.enseignants = _.sortBy(enseignants, 'nom');
        });
        $scope.loadItems = function (query) {
            return _.filter($scope.enseignants, function (en) {
                return en.nom.indexOf(query) > -1;
            })
        };
        $scope.tags = ($scope.element === undefined) ? [] : $scope.element.enseignants;

        $scope.valider = function () {
            var resultat = {};
            $scope.element.enseignants = $scope.tags;
            resultat.item = $scope.element;
            resultat.cle = $scope.cle;
            $modalInstance.close(resultat);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
    }]);
