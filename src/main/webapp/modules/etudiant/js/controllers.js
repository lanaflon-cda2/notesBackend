angular.module("notesApp.etudiants.controllers", []).controller("EtudiantController", ["$http", "$scope", "$modal", "Etudiant", "Annee", "Departement", "Niveau", "Option",
    function ($http, $scope, $modal, Etudiant, Annee, Departement, Niveau, Option) {
        $scope.taille = 30;
        $scope.etudiants = [];
        var etds = Etudiant.query(function () {
            $scope.etudiants = _.sortBy(etds,'nom');
            $scope.totalItems = $scope.etudiants.length;
        });
        var ans = Annee.query(function () {
            $scope.annees = ans;
        });
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });
        var niveaux = Niveau.query(function () {
            $scope.niveaux = niveaux;
        });
        $scope.itemsPerPage = 15;
        $scope.currentPage = 1;
        
        $scope.annee = null;
        $scope.departement = null;
        $scope.niveau = null;
        $scope.option = null;
        $scope.currentPage = 1;
        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };
        $scope.updateOptions = function () {
            if (($scope.departement !== null) && ($scope.niveau !== null)) {
                $http.get('/notesBackend/api/options/' + $scope.departement + '/' + $scope.niveau).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
            }
        };

// on teste les infity liste
        $scope.loadMore = function() {
            if( ($scope.taille + 10) < $scope.totalItems ){
                $scope.taille = $scope.taille + 10;
            } 
        };

        $scope.filtrer = function () {
            var queries = {};
            if ($scope.annee !== null) {
                queries['anneeId'] = $scope.annee;
            }
            if ($scope.departement !== null) {
                queries['departementId'] = $scope.departement;
            }
            if ($scope.niveau !== null) {
                queries['niveauId'] = $scope.niveau;
            }
            if ($scope.option !== null) {
                queries['optionId'] = $scope.option;
            }
            $http.get('/notesBackend/api/etudiants/inscrits', {params: queries}).success(function (data, status, config, headers) {
                $scope.etudiants = data;
                $scope.totalItems = data.length;
            });
        };

    // la modification d'un etudiant

    $scope.afficherFenetre = function (key,item) {
            var modelInstance = $modal.open({
                templateUrl: '/modules/etudiant/views/nouveau.html',
                controller: 'EtudiantFenetreController',
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
                console.log('je suis ici mat'+ departe.element.id);
                if (departe.element.id) {
                    departe.element.$update(function () {
                        $scope.etudiants.splice(departe.key, departe.element);
                    });
                } else {
                    var toto = Etudiant.save(departe.element, function () {
                        $scope.etudiants.push(toto);
                    });
                }
            }, function () {

            });
        };


    }]).controller('EtudiantImportController', ["$scope", "$http", "Annee", "$log", "FileUploader", function ($scope, $http, Annee, $log, FileUploader) {
        $scope.fichier = null;
        $scope.annee = null;
        $scope.files = null;
        $scope.uploadFile = function (fs) {
            $scope.files = fs;
        };
        var ans = Annee.query(function () {
            $scope.annees = ans;
        });
        var fd = new FormData();
        $scope.valider = function () {
            
            //var fd = new FormData();
            //Take the first selected file
            fd.append("fichier", $scope.files[0]);
            fd.append("annee", $scope.annee);
            $http.post('/notesBackend/api/etudiants/import', fd, {
                withCredentials: true,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function () {
                
            }).error(function () {
               
            });
        };

        //je suis en train de tester 

        var uploader = $scope.uploader = new FileUploader({
            url: '/notesBackend/api/etudiants/import',
            method: 'POST',
            headers: {'Content-Type': undefined},
            formData:fd
        });

        // FILTERS

        uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS

        uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function(fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function(addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function(item) {
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function(fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function(progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function(fileItem, response, status, headers) {
            console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function(fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function(fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function(fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
        };
        uploader.onCompleteAll = function() {
            console.info('onCompleteAll');
        };

        console.info('uploader', uploader);


        //fin de mes teste
    }]).controller("EtudiantFenetreController", ["$scope", "$modalInstance", "departe",
    function ($scope, $modalInstance, departe) {
        $scope.etudiant = departe.element;
        $scope.genres =  [
            { value: "masculin", label: "Homme" },
            { value: "feminin", label: "Femme" }
        ];
        
        $scope.valider = function () {
            console.log('Matricule'+ $scope.etudiant.matricule);
            console.log('Noms'+ $scope.etudiant.nom);
            $modalInstance.close(departe);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss("Cancel");
        };
    }]);
