angular.module("notesApp.departements.controllers", []).controller("DepartementController", ["$scope", "$modal", "$log", "Departement",
    function ($scope, $modal, $log, Departement) {

        var deps = Departement.query(function () {

            $scope.departements = deps;
        });

        $scope.afficherFenetre = function (key,item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/departement/views/nouveau.html',
                controller: 'DepartementFenetreController',
                controllerAs: 'depart',
                keyboard: true,
                backdrop: false,
                resolve: {
                    departe: function () {
                        var ret = {};
                        ret.key = key;
                        ret.element = item;
                        return ret;
                    }
                }
            });

            modelInstance.result.then(function (departe) {
                if (departe.element && departe.element.id) {
                    departe.element.$update(function () {
                            $scope.departements.splice(departe.key, departe.element);
                    });
                } else {
                    var toto = Departement.save(departe.element, function () {
                            $scope.departements.push(toto);
                    });
                }
            }, function () {

            });
        };
        
        $scope.supprimerDepartement = function (key, item) {
            if (confirm("Voulez vous vraiment supprimer ce departement?")) {
                Departement.remove({
                    id: item.id
                }, function () {
                      $scope.departements.splice(key, 1);                    
                });
            }
        };
    }]).controller("DepartementFenetreController", ["$scope", "$modalInstance", "departe",
    function ($scope, $modalInstance, departe) {
        $scope.departe = departe;
        $scope.valider = function () {
            $modalInstance.close($scope.departe);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
    }]).controller("PaginationDemoCtrl", ["$scope", "$log",
    function ($scope, $log) {
        $scope.totalItems = 180;
        $scope.currentPage = 1;

        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };

        $scope.pageChanged = function () {
            $log.log('Page changed to: ' + $scope.currentPage);
        };

        $scope.maxSize = 5;
        $scope.bigTotalItems = 175;
        $scope.bigCurrentPage = 1;
    }]);




















