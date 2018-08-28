angular.module("notesApp.notes.controllers", []).controller("NoteController", ["Departement","Cours", "Evaluation","Annee",  "$scope", "$log",
    function (Departement, Cours, Evaluation, Annee,  $scope, $log) {
        var ans = Annee.query(function(){
            $scope.annees = ans;
        });
        var cos = Cours.query(function(){
           $scope.cours = cos; 
        });
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });

        $scope.uploadFile = function (fs) {
            $scope.files = fs;
        };

        $scope.department = null;

    }]).controller("NoteImportationController", ["Departement","Niveau", "Annee", "Cours", "Option", "Evaluation", "$scope", "$http", "$log",
    function (Departement, Niveau, Annee, Cours, Option, Evaluation,$scope, $http, $log) {

        var deps = Departement.query(function(){
            $scope.departements = deps;
        });
        
        var opt = Option.query(function(){
            $scope.options = opt; 
        });
        var nivs = Niveau.query(function(){
            $scope.niveaux = nivs;
        });
        var ans = Annee.query(function () {
            $scope.annees = ans;
        });

        $scope.evaluations = [];

        $scope.importResponse = null;

        $scope.chargementResponse = null;

        $scope.chargementError = null;

        $scope.importError = null;

        $scope.headerNames = [];

        $scope.uploadFile = function (fs) {
            $scope.files = fs;
            $scope.headerNames = [];
            var reader = new FileReader();
            reader.onload = function(){
                var data = reader.result;
                var wb = XLSX.read(data, {type: 'binary'});
                var header = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], { header: 1 })[0];
                var index = 0;
                for(hdr in header){
                    var head = new Object();
                    head.name = header[index];
                    head.index = index++;
                    $scope.headerNames.push(head);
                }
                $scope.$digest();
            };
            reader.onerror = function(ex){
                console.log(ex);
            };
            reader.readAsBinaryString($scope.files[0]);
        };

        $scope.changerDepartement = function(){
            if(($scope.departement !== undefined) && ($scope.niveau !== undefined)){
                $http.get('api/options/' + $scope.departement + '/' + $scope.niveau).success(function(data){
                    $scope.options = data;
                });
            }
        };
        $scope.changerOption = function(){
            if(($scope.option !== undefined) && ($scope.niveau !== undefined)){
                $http.get('api/cours/' + $scope.niveau + '/' + $scope.option).success(function (data, status, config, headers) {
                    $scope.cours = data;
                });
            }
        };

        $scope.changerCours = function(){
            $scope.importResponse = null;
            $scope.chargementResponse = null;
            if($scope.cour){
                $http.get('api/cours/'+$scope.cour+'/evaluations').success(function(data){
                   var evals = new Object();
                   evals.name = "Matricules";
                   evals.index = -1;
                   evals.isExam = false;
                   $scope.evaluations.push(evals);
                   evals = new Object();
                   evals.name = "Noms & Prenoms";
                   evals.index = -1;
                   evals.isExam = false;
                   $scope.evaluations.push(evals);
                   var index = 0;
                   for (i in data) {
                       var evals  = new Object();
                       evals.name = data[index].code;
                       if (data[index].isExam) {
                            evals.session = 0;
                            evals.isExam = true;
                       } else {
                            evals.isExam = false;
                       }
                       evals.index = -1;
                       index++;
                       $scope.evaluations.push(evals);
                       console.log($scope.evaluations);
                   }
                }).error(function(data){
                    console.log(data);
                });
            }
        };
        
        $scope.valider = function (bool) {
            console.log(bool);
            $scope.importResponse = null;
            $scope.chargementResponse = null;
            var fd = new FormData();
            var header = new Object();
            var sessionExam = new Object();
            var index = 0;
            for(i in $scope.evaluations){
                if($scope.evaluations[index].index != -1){
                    header[$scope.evaluations[index].name] = index;
                    if($scope.evaluations[index].isExam){
                        sessionExam[$scope.evaluations[index].name] = $scope.evaluations[index].session;
                    }
                }
                index++;
            }
            fd.append("fichier", $scope.files[0]);
            fd.append("courId", $scope.cour);
            fd.append("niveauId", $scope.niveau);
            fd.append("optionId", $scope.option);
            fd.append("headers", JSON.stringify(header));
            fd.append("anneeId", $scope.annee);
            fd.append("session", JSON.stringify(sessionExam));
            fd.append("importNow", bool);
            $http.post('api/notes/import', fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (data) {
                if(bool)
                    $scope.importResponse = data;
                else
                    $scope.chargementResponse = data;
            }).error(function (data) {
                if(bool)
                    $scope.importError = data;
                else
                    $scope.chargementError = data;
            });
        };

        
    }]).controller("NoteModificationController", ["$scope", "$http", "Departement", "Niveau", "Annee","Note", "$modal",
    function ($scope, $http, Departement, Niveau, Annee, Note, $modal) {
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });
        var niv = Niveau.query(function () {
            $scope.niveaux = niv;
        });
        var anns = Annee.query(function () {
            $scope.annees = anns;
        });
        $scope.cours;
        $scope.updateOptions = function () {
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('api/options/' + $scope.departement.id + '/' + $scope.niveau.id).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
            }
        };
        $scope.changerOption = function () {
            console.log('api/cours/' + $scope.niveau.id + '/' + $scope.option.id);
            if (($scope.option) && ($scope.niveau)) {
                $http.get('api/cours/' + $scope.niveau.id + '/' + $scope.option.id).success(function (data, status, config, headers) {
                    $scope.cours = data;
                });
            }
        };
        $scope.afficher = function(){
            if($scope.matricule){
                $http.get('notes/'+$scope.matricule+'/'+$scope.cour.id+'/'+$scope.annee).success(function(data){
                   $scope.notes = data; 
                });
            }
        };
        $scope.supprimerNote = function (key, item) {
            if (confirm("Voulez vous vraiment supprimer cette note?")) {
                Note.remove({
                    id: item.id
                }, function () {
                      $scope.notes.splice(key, 1);                    
                });
            }
        };

    $scope.afficherFenetre = function (key,item) {
            var modelInstance = $modal.open({
                templateUrl: 'modules/note/views/modificationnote.html',
                controller: 'NotesFenetreController',
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
                if (departe.element.id) {
                    Note.update({
                            id: departe.element.id
                        }, departe.element, function () {
                            $scope.notes.splice(departe.key, departe.element);
                        });
                } else {
                    var toto = Note.save(departe.element, function () {
                        $scope.notes.push(toto);
                    });
                }
            }, function () {

            });
        };


        $scope.modifierNote = function (key, item) {
            $modalInstance.close(departe);
        };
    }]).controller("NotesFenetreController", ["$scope", "$modalInstance", "departe",
    function ($scope, $modalInstance, departe) {
        $scope.note = departe.element;

        $scope.valider = function () {
            $modalInstance.close(departe);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
    }]);
