angular.module("notesApp.typecours.controllers", []).controller("TypecoursController", ["$log", "$scope", "$modal", "TypeCours", "EvaluationDetailService",
    function ($log, $scope, $modal, TypeCours, EvaluationDetailService) {
        var deps = TypeCours.query(function () {
            $scope.typecours = deps;
        });
        $scope.ajouterEvaluation = function (typ, eva) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/typecours/views/nouveau_evaluation.html',
                controller: 'EvaluationDetailController',
                controllerAs: 'evaluation',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt;
                        $scope.typeCours = typ;
                        if (eva) {
                            tt = eva;
                            tt.typeCours = Object.create(typ);
                            tt.typeCours.evaluations = null;
                        }
                        else {
                            tt = {};
                            tt.typeCours = typ;
                        }
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                if (item.id) {
                    // I'm updating
                    $log.log("I'm updating an evaluation");
                    $log.log("The value of item " + JSON.stringify(item));
                    $log.log("The value of typeCours " + item.typeCours);
                    EvaluationDetailService.modifierEvaluation(item.typeCours, item).then(function () {

                    }, function () {

                    });
                } else {
                    // I'm creating a new value
                    $log.log("I'm adding a new evaluation to a typeCours");
                    $log.log("The value of item " + JSON.stringify(item));
                    $log.log("The value of typeCours " + item.typeCours);
                    EvaluationDetailService.addEvaluation(item.typeCours, item).then(function (data) {
                        $scope.typeCours.evaluations.push(data.data);
                    }, function () {

                    });
                }
            }, function () {

            });
        };

        $scope.supprimerEvaluation = function (typ, eva) {
            if (confirm("Voulez vous vraiment supprimer cette evaluation du cours ?")) {
                EvaluationDetailService.supprimerEvaluation(typ, eva).then(function () {
                    $log.log("I'm deleting an evaluation from typeCours");
                    var id;
                    for (var i = 0; i < typ.evaluations.length; i++) {
                        if (typ.evaluations[i].id === eva.id) {
                            id = i;
                            break;
                        }
                    }
                    if (id) {
                        typ.evaluations.splice(id, 1);
                    }
                }, function () {

                });
            }
        };
        $scope.afficherFenetre = function (item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/typecours/views/nouveau.html',
                controller: 'TypecoursFenetreController',
                controllerAs: 'semestre',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt;
                        if (item)
                            tt = item;
                        else
                            tt = {};
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                if (item.id) {
                    item.$update(function (toto) {
                        var id;
                        for (var i = 0; i < $scope.typecours.length; i++) {
                            if ($scope.typecours[i].id === item.id) {
                                id = i;
                                break;
                            }
                        }
                        if (id) {
                            $scope.typecours.splice(id, 1, toto);
                        }
                    });
                } else {
                    TypeCours.save(item, function () {
                        $scope.typecours.push(item);
                    });
                }
            }, function () {

            });

        };
        $scope.supprimerTypecours = function (item) {
            if (confirm("Voulez vous vraiment supprimer ce type de cours?")) {
                TypeCours.remove({
                    id: item.id
                }, function () {
                    var id;
                    for (var i = 0; i < $scope.typecours.length; i++) {
                        if ($scope.typecours[i].id === item.id) {
                            id = i;
                            break;
                        }

                    }
                    if (id) {
                        $scope.typecours.splice(id, 1);
                    }
                });
            }
            ;
        };
    }]).controller("TypecoursFenetreController", ["$scope", "$modalInstance", "element",
    function ($scope, $modalInstance, element) {
        $scope.element = element;
        $scope.valider = function () {
            $modalInstance.close($scope.element);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]).controller("EvaluationDetailController", ["$scope", "Evaluation", "$modalInstance", "element", function ($scope, Evaluation, $modalInstance, element) {
        var ops = Evaluation.query(function () {
            $scope.evaluations = ops;
        });
        $scope.element = element;
        $scope.valider = function () {
            $modalInstance.close($scope.element);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
    }]);
