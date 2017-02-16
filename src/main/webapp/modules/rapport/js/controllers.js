angular.module("notesApp.rapports.controllers", []).controller("ProcesVerbalController", ["$scope", "$http", "Departement", "Niveau", "Annee",
    function ($scope, $http, Departement, Niveau, Annee) {
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });
        var niv = Niveau.query(function () {
            $scope.niveaux = niv;
        });
        var anns = Annee.query(function () {
            $scope.annees = anns;
        });
        $scope.updateOptions = function () {
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('/notesBackend/api/options/' + $scope.departement.id + '/' + $scope.niveau.id).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
            }
        };
        $scope.changerOption = function () {
            if (($scope.option) && ($scope.niveau)) {
                $http.get('/notesBackend/api/cours/' + $scope.niveau.id + '/' + $scope.option.id).success(function (data, status, config, headers) {
                    $scope.cours = data;
                });
            }
        }
        $scope.produirePV = function () {
            var toto = "/notesBackend/api/rapport/pv/";
            toto = toto + $scope.niveau.id + "/";
            toto = toto + $scope.option.id + "/";
            toto = toto + $scope.cour.id + "/";
            toto = toto + $scope.annee + "/";
            toto = toto + $scope.session;
            $http.get(toto).success(function (data, status, headers, config) {
                if ((status === 200) && (headers('content-type') == 'text/pdf')) {
                    var element = angular.element('<a>');
                    element.attr({
                        href: config.url,
                        target: '_blank',
                        download: 'pv.pdf'
                    })[0].click();
                }
            });
        };
    }]).controller("SyntheseController", ["$log","$scope", "$http", "Departement", "Niveau", "Annee",
    function ($log,$scope, $http, Departement, Niveau, Annee) {
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });
        var niv = Niveau.query(function () {
            $scope.niveaux = niv;
        });
        var anns = Annee.query(function () {
            $scope.annees = anns;
        });
        $scope.updateOptions = function () {
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('/notesBackend/api/options/' + $scope.departement.id + '/' + $scope.niveau.id).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
                $http.get('/notesBackend/api/niveaux/' + $scope.niveau.id + '/semestres').success(function (data, status, config, headers) {
                    $scope.semestres = data;
                });
            }
        };
        $scope.produireSynthese = function () {
            var toto;
            $log.log($scope.semestre);
            $log.log($scope.semestre !== undefined);
            $log.log(!$scope.semestre);
            if (($scope.semestre !== undefined) && ($scope.semestre)) {
                toto = "/notesBackend/api/rapport/synthese/semestre/";
                toto = toto + $scope.niveau.id + "/";
                toto = toto + $scope.option.id + "/";
                toto = toto + $scope.annee + "/";
                toto = toto + $scope.semestre;
            } else {
                toto = "/notesBackend/api/rapport/synthese/annuelle/";
                toto = toto + $scope.niveau.id + "/";
                toto = toto + $scope.option.id + "/";
                toto = toto + $scope.annee;
            }
            $http.get(toto).success(function (data, status, headers, config) {
                if ((status === 200) && (headers('content-type') === 'text/pdf')) {
                    var element = angular.element('<a>');
                    element.attr({
                        href: config.url,
                        target: '_blank',
                        download: 'synthese.pdf'
                    })[0].click();
                }
            });
        };
    }]).controller("RelevesNoteController", ["$log","$scope", "$http", "Departement", "Niveau", "Annee",
    function ($log,$scope, $http, Departement, Niveau, Annee) {
        var deps = Departement.query(function () {
            $scope.departements = deps;
        });
        var niv = Niveau.query(function () {
            $scope.niveaux = niv;
        });
        var anns = Annee.query(function () {
            $scope.annees = anns;
        });
        $scope.etudiant = undefined;

        $scope.updateOptions = function () {
            console.log('/notesBackend/api/options/' + $scope.departement.id + '/' + $scope.niveau.id);
            if (($scope.departement) && ($scope.niveau)) {
                $http.get('/notesBackend/api/options/' + $scope.departement.id + '/' + $scope.niveau.id).success(function (data, status, config, headers) {
                    $scope.options = data;
                });
            }
        };
        $scope.produireReleve = function () {
            var toto;
            $log.log($scope.semestre);
            $log.log($scope.semestre !== undefined);
            $log.log(!$scope.semestre);
            if (($scope.etudiant !== undefined) && ($scope.option)) {
                //le releve de notes de notes de l etudiant
                toto = "/notesBackend/api/rapport/relevet/";
                toto = toto + $scope.niveau.id + "/";
                toto = toto + $scope.option.id + "/";
                toto = toto + $scope.annee.id + "/";
                //toto = toto + $scope.etudiant.id;
            } else {
                //le releve de notes de notes de l option
                toto = "/notesBackend/api/rapport/relevet/";
                toto = toto + $scope.niveau.id + "/";
                toto = toto + $scope.option.id + "/";
                toto = toto + $scope.annee;

                console.log('je veux avoir les releves' + toto);
            }

            $http.get(toto).success(function (data, status, headers, config) {
                if ((status === 200) && (headers('content-type') === 'text/pdf')) {
                    var element = angular.element('<a>');
                    element.attr({
                        href: config.url,
                        target: '_blank',
                        download: 'releve.pdf'
                    })[0].click();
                }
            });
        };
    }]);


