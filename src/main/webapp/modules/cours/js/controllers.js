angular.module("notesApp.cours.controllers", []).controller("CoursController", ["$scope", "$modal", "Cours", "Departement", "$http", "$log",
    function ($scope, $modal, Cours, Departement, $http, $log) {
//        var cours = Cours.query(function () {
//            $scope.cours = cours;
//            $scope.totalItems = cours.length;
//        });

        var deps = Departement.query(function () {
            //$scope.departements = _.sortBy(deps,'code'); 
            $scope.departements = deps;
        });

        $scope.departement = {};


        $scope.filtrer = function () {
            if ($scope.departement) {
                var query = "/notesBackend/api/departements/" + $scope.departement + "/cours";
                $http.get(query).success(function (data) {
                    $log.log(data);
                    $scope.cours = data;
                    $scope.totalItems = data.length;
                });
            }

        };

        $scope.itemsPerPage = 15;
        $scope.currentPage = 1;
        $scope.afficherFenetre = function (cle, item) {
            if ($scope.departement) {
                var modelInstance = $modal.open({
                    templateUrl: '/modules/cours/views/nouveau.html',
                    controller: 'CoursFenetreController',
                    controllerAs: 'depart',
                    keyboard: true,
                    backdrop: false,
                    resolve: {
                        element: function () {
                            var tt = {};
                            tt.cle = cle;
                            tt.item = item;
                            return tt;
                        }
                    }
                });
                modelInstance.result.then(function (resultat) {
                    var item = resultat.item;
                    var cle = resultat.cle;
                    if ((item.id !== undefined) && (cle !== undefined)) {
                        Cours.update({
                            id: item.id
                        }, item, function () {
                            $scope.cours.splice(cle, 1, item);
                        });
                    } else {
                        Cours.save(item, function () {
                            $scope.cours.push(item);
                        });
                    }
                }, function () {

                });
            }

        }
        $scope.supprimerCours = function (cle, item) {
            if (confirm("Voulez vous vraiment supprimer ce cours?")) {
                Cours.remove({
                    id: item.id
                }, function () {

                    if (cle !== undefined) {
                        $scope.cours.splice(cle, 1);
                    }
                })
            }
        }
    }]).controller("CoursFenetreController", ["$scope", "$modalInstance", "element", "TypeCours", "Departement",
    function ($scope, $modalInstance, element, TypeCours, Departement) {
        $scope.element = element.item;
        $scope.cle = element.cle;
        var type = TypeCours.query(function () {
            $scope.types = type;
        });
        var dep = Departement.query(function () {
            $scope.departements = _.sortBy(dep, 'code');
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
