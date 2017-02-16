angular.module("notesApp.enseignants.controllers", []).controller("EnseignantController", ["$scope", "$modal", "$log", "Enseignant",
    function ($scope, $modal, $log, Enseignant) {
        var deps = Enseignant.query(function () {
            $scope.enseignants = deps;
            $scope.totalItems = $scope.enseignants.length;
        });

        //debut de la pagination
        $scope.itemsPerPage = 15;
        $scope.currentPage = 1;



        // fin de la pagination
        $scope.afficherFenetre = function (cle, item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/enseignant/views/nouveau.html',
                controller: 'EnseignantFenetreController',
                controllerAs: 'enseignant',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt = {};
                        tt.item = item;
                        tt.cle = cle;
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (resultat) {
                var item = resultat.item;
                var cle = resultat.cle;
                if ((item.id !== undefined) && (cle !== undefined)) {
                    item.$update(function () {
                        $scope.enseignants.splice(cle, 1, item);
                    });
                } else {
                    Enseignant.save(item, function () {
                        $scope.enseignants.push(item);
                    });
                }
            }, function () {

            });

        };
        $scope.supprimerEnseignant = function (cle, item) {
            if (confirm("Voulez vous vraiment supprimer cet enseinant ?")) {
                Enseignant.remove({
                    id: item.id
                }, function () {
                    if (cle !== undefined) {
                        $scope.enseignants.splice(cle, 1);
                    }
                });
            }
        };
    }]).controller("EnseignantFenetreController", ["$scope", "$modalInstance", "element",
    function ($scope, $modalInstance, element) {
        $scope.element = element.item;
        $scope.cle = element.cle;
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
