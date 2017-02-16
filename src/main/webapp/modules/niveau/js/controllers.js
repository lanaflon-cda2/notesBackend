angular.module("notesApp.niveaux.controllers", []).controller("NiveauController", ["$scope", "$modal", "$log", "Niveau",
    function ($scope, $modal, $log, Niveau) {
        var nivs = Niveau.query(function () {
            $scope.niveaux = nivs;
        });

        $scope.afficherFenetre = function (item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/niveau/views/nouveau.html',
                controller: 'NiveauFenetreController',
                controllerAs: 'depart',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt;
                        if (item)
                            tt = item;
                        else
                            tt = new Niveau();
                        $log.log(tt);
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                if (item.id) {
                    item.$update(function () {
                        var id;
                        for (var i = 0; i < $scope.niveaux.length; i++) {
                            if ($scope.niveaux[i].id == item.id) {
                                id = i;
                                break;
                            }
                        }
                        if (id) {
                            $scope.niveaux.splice(id, 1, item);
                        }
                    });
                } else {
                    Niveau.save(item, function () {
                        $scope.niveaux.push(item);
                    });
                }
            }, function () {

            });

        }
        $scope.supprimerNiveau = function (item) {
            if (confirm("Voulez vous vraiment supprimer ce niveau d'etude?")) {
                Niveau.remove({
                    id: item.id
                }, function () {
                    var id;
                    for (var i = 0; i < $scope.niveaux.length; i++) {
                        if ($scope.niveaux[i].id == item.id) {
                            id = i;
                            break;
                        }

                    }
                    if (id) {
                        $scope.niveaux.splice(id, 1);
                    }
                })
            }
        }
    }]).controller("NiveauFenetreController", ["$log", "$scope", "$modalInstance", "Cycle", "element",
    function ($log, $scope, $modalInstance, Cycle, element) {
        var tt = Cycle.query(function () {
            $scope.cycles = tt;
        });
        $scope.element = element;
        $log.log(element);
        $scope.valider = function () {
            $modalInstance.close($scope.element);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]);
