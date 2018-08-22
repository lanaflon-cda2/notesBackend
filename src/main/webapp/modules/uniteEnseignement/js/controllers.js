angular.module("notesApp.uniteenseignements.controllers", []).controller("UniteEnseignementController", ["$scope", "$modal", "Cours","UniteEns", "Departement", "Niveau", "$http",
    function ($scope, $modal, UniteEns, Departement, Cours, Niveau, $http) {

        $http.get("api/departements").success(function(data){
            $scope.departements = data;
            console.log($scope.departements);
        });

        var nivs = Niveau.query(function () {
            $scope.niveaux = nivs;
        });

        $scope.modificationDepartement = function () {
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
                $scope.option = null;
            }
        };

        $scope.filtrer = function () {
            if (($scope.niveau) && ($scope.option)) {
                $http.get('api/uniteEns/' + $scope.niveau + '/' + $scope.option).success(function (data, status, config, headers) {
                    $scope.unites = data;
                });
            }
            
            if($scope.departement){
                $http.get("api/departements/"+ $scope.departement + "/cours").success(function(data){
                    $scope.cours = data;
                }
                        );
            }
        };

        $scope.departement = null;
        $scope.niveau = null;
        $scope.option = null;

        $scope.afficherFenetre = function (cle,item) {
            var modelInstance = $modal.open({
                templateUrl: 'modules/uniteEnseignement/views/nouveau.html',
                controller: 'UniteEnsFenetreController',
                controllerAs: 'unite',
                keyboard: true,
                backdrop: false,
                resolve: {
                    valeurs: function () {
                        var ret = {};
                        ret.element = item ? item : {hasOptionalChoices:false};
                        ret.cle = cle;
                        ret.departement = $scope.departement;
                        ret.option = $scope.option;
                        ret.niveau = $scope.niveau;
                        ret.cours = $scope.cours;
                        ret.modificationDepartement = $scope.modificationDepartement;
                        return ret;
                    }
                }
            });

            modelInstance.result.then(function (resultat) {
                var item = resultat.element;
                var cle = resultat.cle;
                if (item.id && (cle !== undefined)) {
                    $http.put('api/uniteEns/'+resultat.niveau+'/'+resultat.option+'/'+item.id, item).success(function (data, status, config, headers) {
                        $scope.unites.splice(cle, 1, item);
                    });
                } else {
                    $http.post('api/uniteEns/'+resultat.niveau+'/'+resultat.option, item).success(function (data, status, config, headers) {
                        $scope.unites.push(data);
                    });
                }
            });

        };

        $scope.supprimerUniteEns = function (cle, item) {
            if (confirm("Voulez vous vraiment supprimer cette unité d'enseignement?")) {
                // UniteEns.remove({id: item.id}, function () {
                //     // if (cle){
                //         $scope.unites.splice(cle, 1);
                //     // }
                // });
                $http.delete('api/uniteEns/' + item.id).success(function(data){
                    $scope.unites.splice(cle, 1);
                });
            }
        };

    }]).controller("UniteEnsFenetreController", ["$scope", "$modalInstance", "valeurs", "Departement", "Niveau", "Cours", "$http",
    function ($scope, $modalInstance, valeurs, Departement, Niveau, Cours, $http) {

        $scope.element = valeurs.element;
        $scope.departement = valeurs.departement;
        $scope.niveau = valeurs.niveau;
        $scope.cle = valeurs.cle;

        var deps = Departement.query(function () {
            $scope.departements = deps;
        });

        $scope.modificationDepartement = function () {
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
                $scope.option = {};
            }
        };

        $scope.modificationDepartement();
        $scope.option = valeurs.option;

        if($scope.option){
            $http.get('api/cours').success(function(data){
                $scope.cours = data;
            });
        }

        $scope.modificationOption = function (){
            // if(($scope.option) && ($scope.niveau)){
                $http.get('api/cours').success(function(data){
                    $scope.cours = data;
                });
                // $scope.cour = {};
            // }
        };

       
        $scope.tags = valeurs.element.cours;
  
        console.log("loading out"+$scope.cours);
        var nivs = Niveau.query(function () {
            $scope.niveaux = nivs;
        });

        $scope.loadItems = function (start) {
            return _.filter($scope.cours, function (hello) {
                return hello.intitule.indexOf(start) > -1;
            });
        };

        $scope.valider = function () {
            var result = {};
            result.element = $scope.element;
            result.niveau = $scope.niveau;
            result.option = $scope.option;
            result.element.cours = $scope.tags;
            result.cle = $scope.cle;
            $modalInstance.close(result);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]);
