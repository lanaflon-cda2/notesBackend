angular
  .module('notesApp.notes.controllers', [])
  .controller('NoteImportationOneController', ['$scope', '$http', 'Departement',
    'Niveau',
    'Annee',
    'Cours',
    'Option',
    'NoteData',
    function ($scope, $http, Departement,
      Niveau,
      Annee,
      Cours,
      Option,
      NoteData) {
      var deps = Departement.query(function () {
        $scope.departements = deps
      })

      var nivs = Niveau.query(function () {
        $scope.niveaux = nivs
      })
      var ans = Annee.query(function () {
        $scope.annees = ans
      })
      $scope.data = NoteData.data
      $scope.data.mapping = []
      $scope.changerDepartement = function () {
        if (($scope.data.departement) && ($scope.data.niveau)) {
          $http.get('api/options/' + $scope.data.departement + '/' + $scope.data.niveau).success(function (data) {
            $scope.options = data
          })
        }
      }
      $scope.changerOption = function () {
        if (($scope.data.option) && ($scope.data.niveau)) {
          $http.get('api/cours/' + $scope.data.niveau + '/' + $scope.data.option).success(function (data, status, config, headers) {
            $scope.cours = data
          })
        }
      }
      $scope.uploadFile = function (fs) {
        $scope.data.fichier = fs
      }
      $scope.changerDepartement()
      $scope.changerOption()
    }
  ])
  .controller('NoteImportation2Controller', ['$scope', '$http', 'NoteData', function ($scope, $http, NoteData) {
    $scope.headerNames = []
    $scope.isLoading = true
    $scope.evaluations = []
    $scope.isError = false
    $scope.errorMessage = ''
    $scope.data = NoteData.data
    if ($scope.data.cour) {
      $http.get('api/cours/' + $scope.data.cour + '/evaluations').success(function (data) {
        $scope.data.mapping.push({
          name: 'Matricule',
          index: -1,
          isExam: false
        })
        var i = null
        for (i in data) {
          var evals = {}
          evals.name = data[i].code
          if (data[i].isExam) {
            evals.session = 0
            evals.isExam = true
          } else {
            evals.isExam = false
          }
          evals.index = -1
          $scope.data.mapping.push(evals)
        }
        var reader = new FileReader()
        reader.onload = function () {
          var data = reader.result
          var wb = XLSX.read(data, {
            type: 'binary'
          })
          var header = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], {
            header: 1
          })[0]
          var index = 0
          for (hdr in header) {
            var head = {}
            head.name = header[index]
            head.index = index++
            $scope.headerNames.push(head)
          }
          $scope.isLoading = false
          $scope.$digest()
        }
        reader.onerror = function (ex) {
          $scope.isError = true
          $scope.errorMessage = ex.errorMessage
          console.log(ex)
        }
        reader.readAsBinaryString($scope.data.fichier[0])
      }).error(function (data) {
        console.log(data)
      })
    }
  }])
  .controller('NoteImportation3Controller', ['$scope', 'NoteData', 'ImportationService', function ($scope, NoteData, ImportationService) {
    $scope.chargementResponse = null
    $scope.isLoading = true
    var header = {}
    var sessionExam = {}
    $scope.data = NoteData.data
    var tmp = $scope.data.mapping.filter(function (a) { return a.index !== -1 })
    tmp.forEach(function (a) { header[a.name] = a.index; return 0 })
    tmp.filter(function (b) { return b.isExam }).forEach(function (c) { sessionExam[c.name] = c.session; return 1 })
    ImportationService.importData(header, sessionExam, false).success(function (data) {
      $scope.chargementResponse = data
      $scope.isLoading = false
    }).error(function (data) {
      $scope.chargementError = data
      $scope.isLoading = false
    })
  }])
  .controller('NoteImportation4Controller', ['$scope', 'NoteData', 'ImportationService', function ($scope, NoteData, ImportationService) {
    $scope.chargementResponse = null
    $scope.isLoading = true
    $scope.data = NoteData.data
    var header = {}
    var sessionExam = {}
    var tmp = $scope.data.mapping.filter(function (a) { return a.index !== -1 })
    tmp.forEach(function (a) { header[a.name] = a.index; return 0 })
    tmp.filter(function (b) { return b.isExam }).forEach(function (c) { sessionExam[c.name] = c.session; return 1 })
    ImportationService.importData(header, sessionExam, true).success(function (data) {
      $scope.chargementResponse = data
      $scope.isLoading = false
    }).error(function (data) {
      $scope.chargementError = data
      $scope.isLoading = false
    })
  }])
  .controller('NoteController', [
    'Departement',
    'Cours',
    'Annee',
    '$scope',
    '$log',
    function (Departement, Cours, Annee, $scope, $log) {
      var ans = Annee.query(function () {
        $scope.annees = ans
      })
      var cos = Cours.query(function () {
        $scope.cours = cos
      })
      var deps = Departement.query(function () {
        $scope.departements = deps
      })

      $scope.uploadFile = function (fs) {
        $scope.files = fs
      }

      $scope.department = null
    }
  ])
  .controller('NoteImportationController', [
    '$state',
    '$scope',
    'NoteData',
    '$log',
    function (
      $state,
      $scope,
      NoteData,
      $log
    ) {
      // $state.go('nimportation.etape1')

      $scope.wizard = {
        steps: [
          'nimportation.etape1',
          'nimportation.etape2',
          'nimportation.etape3',
          'nimportation.etape4'
        ],
        currentStep: 0
      }

      $scope.getCurrentStep = function () {
        return $scope.wizard.currentStep
      }
      NoteData.data = {}
      $scope.data = {
      annee: null,
      departement: null,
      niveau: null,
      option: null,
      cour: null,
      fichier: null,
      mapping: []
    }
      
      $scope.status = false
      
      $scope.fichier = false

      $scope.isFirstStep = function () {
        return $scope.wizard.currentStep === 0
      }

      $scope.isLastStep = function () {
        return $scope.wizard.currentStep === $scope.wizard.steps.length - 1
      }

      $scope.getNextLabel = function () {
        return $scope.isLastStep() ? 'Importer' : 'Suivant'
      }

      $scope.handlePrevious = function () {
        $scope.wizard.currentStep -= $scope.isFirstStep() ? 0 : 1
        $state.go($scope.wizard.steps[$scope.wizard.currentStep])
      }
      $scope.handleNext = function (form, dismiss) {
          $scope.status = !form.$valid
          $scope.fichier = true
          if(!$scope.status && (($scope.data.fichier === null) || ($scope.data.fichier === undefined))){
              $scope.fichier = false
        if ($scope.isLastStep()) {
          dismiss()
        } else {
          $scope.wizard.currentStep += 1
          $state.go($scope.wizard.steps[$scope.wizard.currentStep])
        }
      }
  }
    }
  ])
  .controller('NoteModificationController', [
    '$scope',
    '$http',
    'Departement',
    'Niveau',
    'Annee',
    'Note',
    '$modal',
    function ($scope, $http, Departement, Niveau, Annee, Note, $modal) {
      var deps = Departement.query(function () {
        $scope.departements = deps
      })
      var niv = Niveau.query(function () {
        $scope.niveaux = niv
      })
      var anns = Annee.query(function () {
        $scope.annees = anns
      })

      $scope.updateOptions = function () {
        if ($scope.departement && $scope.niveau) {
          $http
            .get(
              'api/options/' + $scope.departement.id + '/' + $scope.niveau.id
            )
            .success(function (data, status, config, headers) {
              $scope.options = data
            })
        }
      }

      $scope.changerOption = function () {
        console.log('api/cours/' + $scope.niveau.id + '/' + $scope.option.id)
        if ($scope.option && $scope.niveau) {
          $http
            .get('api/cours/' + $scope.niveau.id + '/' + $scope.option.id)
            .success(function (data, status, config, headers) {
              $scope.cours = data
            })
        }
      }

      $scope.afficher = function () {
        if ($scope.matricule) {
          $http
            .get(
              'notes/' +
                            $scope.matricule +
                            '/' +
                            $scope.cour.id +
                            '/' +
                            $scope.annee
            )
            .success(function (data) {
              $scope.notes = data
            })
        }
      }
      $scope.supprimerNote = function (key, item) {
        if (confirm('Voulez vous vraiment supprimer cette note?')) {
          Note.remove({
            id: item.id
          },
          function () {
            $scope.notes.splice(key, 1)
          }
          )
        }
      }

      $scope.afficherFenetre = function (key, item) {
        var modelInstance = $modal.open({
          templateUrl: 'modules/note/views/modificationnote.html',
          controller: 'NotesFenetreController',
          controllerAs: 'depart',
          keyboard: true,
          backdrop: false,
          resolve: {
            departe: function () {
              var ret = {}
              ret.key = key
              ret.element = item
              return ret
            }
          }
        })

        modelInstance.result.then(
          function (departe) {
            if (departe.element.id) {
              Note.update({
                id: departe.element.id
              },
              departe.element,
              function () {
                $scope.notes.splice(departe.key, departe.element)
              }
              )
            } else {
              var toto = Note.save(departe.element, function () {
                $scope.notes.push(toto)
              })
            }
          },
          function () {}
        )
      }

      $scope.modifierNote = function (key, item) {
        $modalInstance.close(departe)
      }
    }
  ])
  .controller('NotesFenetreController', [
    '$scope',
    '$modalInstance',
    'departe',
    function ($scope, $modalInstance, departe) {
      $scope.note = departe.element

      $scope.valider = function () {
        $modalInstance.close(departe)
      }

      $scope.cancel = function () {
        $modalInstance.dismiss('Cancel')
      }
    }
  ])
