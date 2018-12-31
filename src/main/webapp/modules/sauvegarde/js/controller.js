angular.module("notesApp.sauvegardes.controllers", []).controller("SauvegardeController", ["$scope", "$http", 
    function ($scope, $http){

        $http.get('/api/annees').success(function(data){
            $scope.annees = data;
        });

        $scope.sauvegarde = function(){
            var urlpath = '/api/sauvegarde';
            if($scope.year !== undefined){
                urlpath = urlpath+'/'+$scope.year;
            }
            $http.get(urlpath).success(function(data, status, headers, config){
                headers = headers();
                var filename = headers['x-filename'];
                var contentType = headers['content-type'];
                var linkElement = document.createElement('a');
                try {
                    var notes_ENSPM = new Blob([data], { type: contentType });
                    var url = window.URL.createObjectURL(notes_ENSPM);
                    linkElement.setAttribute('href', url);
                    linkElement.setAttribute("download", filename);
                    var clickEvent = new MouseEvent("click", {
                        "view": window,
                        "bubbles": true,
                        "cancelable": false
                    });
                    linkElement.dispatchEvent(clickEvent);
                } catch (ex) {
                    console.log(ex);
                }
            }).error(function(data){
                console.log(data)
            });
        }
    }
]);