angular.module("notesApp.controllers", []).controller("LoginController", ["$scope", "$http", "$log",
    function ($scope, $http, $log) {
        $scope.user = {};
        $scope.salut = function () {
        };

        $scope.login = function () {
            var fd = new FormData();
            //Take the first selected file
            fd.append("username", $scope.user.login);
            fd.append("password", $scope.user.password);
            $http.post('/', fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (data) {
                $scope.result = data;
                $log.console.log(data);
            }).error(function (data) {
                $scope.errors = data;
                $log.console.log(data);
            });
        };
    }]).controller("MainController", ["$log", "$scope", "$http", "$modal", "Utilisateur", function ($log, $scope, $http, $modal, Utilisateur) {
        $scope.utilisateur = null;
        $http.get("api/utilisateurs/logged").success(function (data) {
           $scope.utilisateur = data;
        });
        
        // Utilisateur.logged(function(data){
        //     $scope.utilisateur = data;
        // });

        $scope.showProfile = function () {
            var modelInstance = $modal.open({
                templateUrl: 'modules/utilisateur/views/profile.html',
                controller: 'ProfilController',
                controllerAs: 'profil',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        return $scope.utilisateur;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                $log.log(item);
                Utilisateur.update(item, function (data) {
                    $scope.utilisateur = data;
                });
            });
        };
        $scope.changePassword = function () {
            var modelInstance = $modal.open({
                templateUrl: 'modules/utilisateur/views/changepassword.html',
                controller: 'PasswordController',
                controllerAs: 'password',
                keyboard: true,
                backdrop: false,
                resolve: {
                    element: function () {
                        return $scope.utilisateur.id;
                    }
                }
            });
            modelInstance.result.then(function (item) {
                
                
            });
        };
    }]).controller("ProfilController", ["$log", "$scope", "$modalInstance", "element",  function ($log, $scope, $modalInstance, element) {
        $scope.utilisateur = element;
        $scope.valider = function () {
            $modalInstance.close($scope.utilisateur);
        };
       
        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };

    }]).controller("PasswordController", ["$scope","$log","$modalInstance","$http","element",function ($scope, $log, $modalInstance, $http,element) {
        
        $scope.value = element;
        $scope.element = {};
        
        $scope.error = null;
        
        $scope.dirty = function(){
            return ($scope.element.npassword !== null) && $scope.element.npassword !== $scope.element.rnpassword;
        };
        
        $scope.valider = function () {
            $modalInstance.close($scope.element);
        };
        var fd = new FormData();
        
        $scope.changer = function(){            
            fd.append("opassword", $scope.element.password);
            fd.append("npassword", $scope.element.npassword);
            $http.post("api/utilisateurs/"+$scope.value+"/changePassword", fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function(data){
                if(data.error){
                    $scope.error = data.error;
                }else{
                    $scope.error = null;
                    $modalInstance.close($scope.element);
                }
            }).error(function(data){
                $scope.error =  data;
            });
        };


        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
    }]);
