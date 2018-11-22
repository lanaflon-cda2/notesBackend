angular.module('notesApp', ['notesApp.filters', 'ngResource', 'ngTagsInput', 'checklist-model', 'ui.bootstrap', 'ui.router', 'notesApp.sauvegardes', 'notesApp.restaurations', 'notesApp.uniteenseignements', 'notesApp.controllers', 'notesApp.directives', 'notesApp.etudiants', 'notesApp.cours', 'notesApp.parcours', 'notesApp.typecours', 'notesApp.semestres', 'notesApp.services', 'notesApp.options', 'notesApp.departements', 'notesApp.enseignants', 'notesApp.cycles', 'notesApp.niveaux', 'notesApp.evaluations', 'notesApp.annees', 'notesApp.notes', 'notesApp.enseignements', 'notesApp.programme', 'notesApp.rapports', 'notesApp.credits', 'notesApp.deliberations', 'notesApp.utilisateurs', 'infinite-scroll', 'angularFileUpload'])
angular.module('notesApp').run(function ($rootScope) {
  $rootScope.isViewLoading = false
    $rootScope.$on('$stateChangeStart', function () {
    $rootScope.isViewLoading = true
        $rootScope.isError = false
    })
    $rootScope.$on('$stateChangeSuccess', function () {
    $rootScope.isViewLoading = false
        $rootScope.isError = false
    })
    $rootScope.$on('$viewContentLoading', function () {
    $rootScope.isViewLoading = true
        $rootScope.isError = false
    })
    $rootScope.$on('$viewContentLoaded', function () {
    $rootScope.isViewLoading = false
        $rootScope.isError = false
    })
    $rootScope.$on('$stateChangeError', function () {
    $rootScope.isError = true
        $rootScope.isViewLoading = false
    })
})
/*angular.module('notesApp').config(function ($httpProvider) {
  $httpProvider.interceptors.push(function ($q) {
    return {
      request: function (config) {
          console.log(config);
        if (config.url.indexOf('api') === 0)
          {config.url = "http://localhost:8080/" + config.url;}
        return config
            }
    }
  })
    
})*/
