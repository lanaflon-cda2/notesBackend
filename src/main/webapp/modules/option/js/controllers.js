angular.module("notesApp.options.controllers", []).controller("OptionController", ["$scope", "$modal", "$log", "Option","Departement",
    function ($scope, $modal, $log, Option, Departement) {
        var op = Option.query(function () {
            //$scope.option_initiale = _.sortBy(op,'code');
            //$scope.optionss = $scope.option_initiale;
            $scope.optionss = op;
        });
        
        var deps = Departement.query(function(){
           //$scope.departements = _.sortBy(deps,'code'); 
           $scope.departements = deps;
        });
        
        $scope.department = null;

        $scope.afficherFenetre = function (item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/option/views/nouveau.html',
                controller: 'OptionFenetreController',
                controllerAs: 'opt',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt;
                        if (item)
                            tt = item;
                        else
                            tt = new Option();
                        tt.departement = $scope.department;
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                if (item.id) {
                    item.$update(function () {
                        var id;
                        for (var i = 0; i < $scope.optionss.length; i++) {
                            if ($scope.optionss[i].id === item.id) {
                                id = i;
                                break;
                            }
                        }
                        if (id) {
                            $scope.optionss.splice(id, 1, item);
                        }
                    });
                } else {
                    Option.save(item, function () {
                        $scope.optionss.push(item);
                    });
                }
            }, function () {

            });

        };
        
        
        $scope.supprimerOption = function (item) {
            if (confirm("Voulez vous vraiment supprimer cette option ?")) {
                Option.remove({
                    id: item.id
                }, function () {
                    var id;
                    for (var i = 0; i < $scope.optionss.length; i++) {
                        if ($scope.optionss[i].id === item.id) {
                            id = i;
                            break;
                        }

                    }
                    if (id) {
                        $log.log("l id a supprimer est " + id);
                        $scope.optionss.splice(id, 1);

                    }
                });
            }
        };
    }]).controller("OptionFenetreController", ["$scope", "$modalInstance", "element", "Departement",
    function ($scope, $modalInstance, element, Departement) {
        $scope.element = element;
        var dep = Departement.query(function () {
            $scope.departements = _.sortBy(dep,'code');
        });
        $scope.valider = function () {
            $modalInstance.close($scope.element);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]);
