angular.module("notesApp.programme.controllers", []).controller("ProgrammeController", ["$scope", "$modal", "Programme", "Annee", "Departement", "Niveau", "$http",
    function ($scope, $modal, Programme, Annee, Departement, Niveau, $http) {

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
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.semestre !== undefined) && ($scope.option !== undefined)) {
                $http.get('/notesBackend/api/programmes/' + $scope.annee.id + '/' + $scope.niveau + '/' + $scope.option + '/' + $scope.semestre.id).success(function (data) {
                    $scope.programmes = data;
                });
            }
        }

        $scope.updateOptionsSemestre = function () {
            if ($scope.niveau !== undefined) {
                $http.get('/notesBackend/api/niveaux/' + $scope.niveau + "/semestres").success(function (data) {
                    $scope.semestres = data;
                });
                $scope.semestre = undefined;
                if ($scope.departement !== undefined) {
                    $http.get('/notesBackend/api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data) {
                        $scope.options = data;
                    });
                    $scope.option = undefined;
                }

            }
        };

        $scope.afficherFenetre = function (cle, item) {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.semestre !== undefined) && ($scope.option !== undefined)) {
                var modelInstance = $modal.open({
                    templateUrl: '/modules/programme/views/nouveau.html',
                    controller: 'ProrammeFenetreController',
                    controllerAs: 'programme',
                    keyboard: true,
                    backdrop: false,
                    resolve: {
                        element: function () {
                            var tt = {};
                            tt.cle = cle;
                            tt.element = item;
                            tt.option = $scope.option;
                            tt.niveau = $scope.niveau;
                            return tt;
                        }
                    }
                });
                modelInstance.result.then(function (resultat) {
                    var item = resultat.item;
                    var cle = resultat.cle;
                    item.anneeAcademique = $scope.annee;
                    item.semestre = $scope.semestre;
                    if ((item.id !== undefined) && (cle !== null)) {
                        $http.put('/notesBackend/api/programmes/' + $scope.niveau + '/' + $scope.option + '/' + item.id, item).success(function (data) {
                            $scope.programmes.splice(cle, 1, item);
                        });
                    } else {
                        $http.post('/notesBackend/api/programmes/' + $scope.niveau + '/' + $scope.option, item).success(function (data) {
                            $scope.programmes.push(data);
                        });
                    }
                }, function () {

                });
            }
        };
        $scope.supprimerProgramme = function (cle, item) {
            if (confirm("Voulez vous vraiment supprimer ce programme?")) {
                Programme.remove({
                    id: item.id
                }, function () {
                    $scope.programmes.splice(cle, 1);
                });
            }
        };
    }]).controller("ProrammeFenetreController", ["$scope", "$modalInstance", "element", "$http",
    function ($scope, $modalInstance, element, $http) {

        $scope.element = element.item;
        $scope.cle = element.cle;
        $scope.niveau = element.niveau;
        $scope.option = element.option;

        $http.get('/notesBackend/api/uniteEns/' + $scope.niveau + '/' + $scope.option).success(function (data) {
            $scope.unites = data;
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
