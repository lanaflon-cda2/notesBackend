angular.module("notesApp.controllers", []).controller("MenuController", ["$scope", "$log",
function($scope, $log) {

}]).controller("LoginController", ["$scope","$http", "$log",
    function($scope, $http, $log) {
        $scope.user = {};
        $scope.salut = function(){
            $log.console("Bonjour les problemes");
        };
        
        $scope.login = function(){
            $log.console("Les bonnes choses à l'oeuvre");
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
}]);
