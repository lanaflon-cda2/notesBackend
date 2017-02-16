angular.module("notesApp.parcours.controllers", []).controller("ParcoursController", ["$scope", "$modal", "Parcours", "Departement","$log",
    function ($scope, $modal, Parcours,Departement,$log) {
        var deps = Parcours.query(function () {
            $scope.parcours_initiale = deps;
            $scope.parcours_initiale = deps.sort(function(a,b){
               if(a.niveau.code === b.niveau.code){
                   if(a.option.departement.code === b.option.departement.code){
                       if (a.option.code === b.option.code){
                           return 0;
                       }else if(a.option.code < b.option.code){
                           return -1;
                       }else{
                           return 1;
                       }
                   }else if (a.option.departement.code < b.option.departement.code){
                       return -1;
                   }else{
                       return 1;
                   }
               }else if(a.niveau.code < b.niveau.code){
                   return -1;
               }else{
                   return 1;
               }
            });
            $scope.parcours = $scope.parcours_initiale;
        });
        var dd = Departement.query(function(){
           $scope.departements = _.sortBy(dd,'code'); 
        });
        $scope.departement = null;
        $scope.afficherFenetre = function (item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/parcours/views/nouveau.html',
                controller: 'ParcoursFenetreController',
                controllerAs: 'parcours',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        var tt;
                        if (item)
                            tt = item;
                        else
                            tt = new Parcours();
                        return tt;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                if (item.id) {
                    item.$update(function () {
                        var id;
                        for (var i = 0; i < $scope.parcours.length; i++) {
                            if ($scope.parcours[i].id === item.id) {
                                id = i;
                                break;
                            }
                        }
                        if (id) {
                            $scope.parcours.splice(id, 1, item);
                        }
                    });
                } else {
                    Parcours.save(item, function () {
                        $scope.parcours.push(item);
                    });
                }
            }, function () {

            });

        };
        $scope.supprimerParcours = function (item) {
            if (confirm("Voulez vous vraiment supprimer ce parcours?")) {
                Parcours.remove({
                    id: item.id
                }, function () {
                    var id;
                    for (var i = 0; i < $scope.parcours.length; i++) {
                        if ($scope.parcours[i].id === item.id) {
                            id = i;
                            break;
                        }
                    }
                    if (id) {
                        $scope.parcours.splice(id, 1);
                    }
                });
            }
        };
        $scope.filtrer = function(){
            $log.log($scope.departement);
            if ($scope.departement !== null){
                $scope.parcours = _.filter($scope.parcours_initiale, function(p){
                    return p.option.departement.code === $scope.departement.code;
                });
            }
        };
    }]).controller("ParcoursFenetreController", ["$scope", "$modalInstance", "element", "Option","Niveau","Departement","$log",
    function ($scope, $modalInstance, element, Option,Niveau,Departement,$log) {
        $log.log(element);
        var ops = Option.query(function () {
            $scope.option_initiale = _.sortBy(ops, 'code');
            $scope.options = $scope.option_initiale;
        });
        var nivs = Niveau.query(function(){
            $scope.niveaux = _.sortBy(nivs, 'code');
        });
        var deps = Departement.query(function(){
            $scope.departements = _.sortBy(deps, 'code');
        });
        var nivs = Niveau.query(function () {
            $scope.niveaux = nivs;
        });
        $scope.element = element;
        if(element.id)
            $scope.departement = element.option.departement;
        else
            $scope.departement = null;
        $scope.valider = function () {
            $modalInstance.close($scope.element);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
        
        $scope.updateOptions = function(){
            $scope.options = _.filter($scope.option_initiale, function(n){
                return n.departement.code === $scope.departement.code;
            });
        };

    }]);
