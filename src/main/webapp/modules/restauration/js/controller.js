angular.module("notesApp.restaurations.controllers", []).controller("RestaurationController", ["$scope", "$http", 
    function($scope, $http){

        $scope.uploadFile = function (fs) {
            $scope.files = fs
        }

        $scope.restore = function(){
            var fd = new FormData();
            fd.append("fichier", $scope.files[0]);
            $http.post('/api/restauration', fd, {
                withCredentials: true,
                headers: {
                    "Content-Type" : undefined
                },
                transformRequest: angular.identity
            }).success(function(data){
                $scope.restaurationError = data;
                console.log(data);
            })
            console.log("Database restauration");
        }
    }
]);