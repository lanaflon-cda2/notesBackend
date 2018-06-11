angular.module("notesApp.programme.controllers", []).controller("ProgrammeController", ["$scope", "$modal", "Programme", "Annee", "Departement", "Niveau", "$http", "$log",
    function ($scope, $modal, Programme, Annee, Departement, Niveau, $http, $log) {

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
                $http.get('api/programmes/' + $scope.annee.id + '/' + $scope.niveau + '/' + $scope.option + '/' + $scope.semestre.id).success(function (data) {
                    $scope.programmes = data;
                });
            }
            if (($scope.niveau !== undefined) && ($scope.option !== undefined)) {
                $http.get('api/uniteEns/' + $scope.niveau + '/' + $scope.option).success(function (data) {
                    $scope.unites = data;
                });
            }
        };

        $scope.updateOptionsSemestre = function () {
            if ($scope.niveau !== undefined) {
                $http.get('api/niveaux/' + $scope.niveau + "/semestres").success(function (data) {
                    $scope.semestres = data;
                });
                $scope.semestre = undefined;
                if ($scope.departement !== undefined) {
                    $http.get('api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data) {
                        $scope.options = data;
                    });
                    $scope.option = undefined;
                }

            }
        };

        var difference = function (a, b) {
            var arr1 = _.pluck(a, "id");
            var arr2 = _.pluck(b, "id");
            var diff = _.difference(arr1, arr2);
            return _.filter(a, function (obj) {
                return diff.indexOf(obj.id) >= 0;
            });
        }

        $scope.afficherFenetre = function () {
            if (($scope.departement !== undefined) && ($scope.niveau !== undefined) && ($scope.annee !== undefined) && ($scope.semestre !== undefined) && ($scope.option !== undefined)) {

                var modelInstance = $modal.open({
                    templateUrl: 'modules/programme/views/nouveau.html',
                    controller: 'ProrammeFenetreController',
                    controllerAs: 'programme',
                    keyboard: true,
                    backdrop: false,
                    resolve: {
                        element: function () {
                            var tt = {};
                            tt.unites = difference($scope.unites, _.pluck($scope.programmes,"uniteEnseignement"));
                            tt.option = $scope.option;
                            tt.niveau = $scope.niveau;
                            return tt;
                        }
                    }
                });
                modelInstance.result.then(function (resultat) {
                    var selection = resultat.selection;
                    if ((selection !== null) && (selection.length > 0)) {
                        // I should probably not do it this way 
                        _.each(selection, function (elt) {
                            var item = {};
                            item.anneeAcademique = $scope.annee;
                            item.semestre = $scope.semestre;
                            item.uniteEnseignement = elt;
                            $http.post('api/programmes/' + $scope.niveau + '/' + $scope.option, item).success(function (data) {
                                $scope.programmes.push(data);
                            });
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
    }]).controller("ProrammeFenetreController", ["$log","$scope", "$modalInstance", "element",
    function ($log, $scope, $modalInstance, element) {
        $scope.niveau = element.niveau;
        $scope.option = element.option;
        $scope.selectedUnite = [];
        $scope.unites = element.unites;
        $log.log("Hello boy");
        $scope.valider = function () {
            var resultat = {};
            resultat.selection = $scope.selectedUnite;
            $log.log($scope.selectedUnite.length);
            $modalInstance.close(resultat);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]);