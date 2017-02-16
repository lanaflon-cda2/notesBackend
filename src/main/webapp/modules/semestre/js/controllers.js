angular.module("notesApp.semestres.controllers", []).controller("SemestreController", ["$scope", "$modal", "$log", "Semestre",
    function ($scope, $modal, $log, Semestre) {
        var deps = Semestre.query(function () {
            $scope.semestres = deps;
        });
        $scope.afficherFenetre = function (item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/semestre/views/nouveau.html',
                controller: 'SemestreFenetreController',
                controllerAs: 'semestre',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt;
                        if (item)
                            tt = item;
                        else
                            tt = new Semestre();
                        $log.log(tt);
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                if (item.id) {
                    item.$update(function () {
                        var id;
                        for (var i = 0; i < $scope.semestres.length; i++) {
                            if ($scope.semestres[i].id === item.id) {
                                id = i;
                                break;
                            }
                        }
                        if (id) {
                            $scope.semestres.splice(id, 1, item);
                        }
                    });
                } else {
                    Semestre.save(item, function () {
                        $scope.semestres.push(item);
                    });
                }
            }, function () {

            });

        };
        $scope.supprimerSemestre = function (item) {
            if (confirm("Voulez vous vraiment supprimer ce semestre?")) {
                Semestre.remove({
                    id: item.id
                }, function () {
                    var id;
                    for (var i = 0; i < $scope.semestres.length; i++) {
                        if ($scope.semestres[i].id === item.id) {
                            id = i;
                            break;
                        }

                    }
                    if (id) {
                        $scope.semestres.splice(id, 1);
                    }
                });
            }
        };
    }]).controller("SemestreFenetreController", ["$log", "$scope", "$modalInstance", "element","Niveau",
    function ($log, $scope, $modalInstance, element,Niveau) {
        $scope.element = element;
        var niveau = Niveau.query(function () {
            $scope.niveaux = niveau;
        });
        $log.log(element);
        $scope.valider = function () {
            $log.log("version ok");
            $modalInstance.close($scope.element);
        };

        $scope.cancel = function () {
            $log.log("version cancel");
            $modalInstance.dismiss("Cancel");
        };

    }]);
