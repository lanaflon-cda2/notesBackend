angular.module('notesApp.notes.services', []).factory('Note', function ($resource) {
  return $resource('api/notes/:id', {
    id: '@id'
  }, {
    update: {
      method: 'PUT'
    },
    toto: {
      method: 'GET',
      url: 'api/options/:first/:second'
    }
  })
}).factory('NoteData', function () {
  return {
    data: {
      annee: null,
      departement: null,
      niveau: null,
      option: null,
      cour: null,
      fichier: null,
      mapping: []
    } }
}
).service('ImportationService', function ($http, NoteData) {
  this.importData = function (header, sessionExam, importNow) {
    var fd = new FormData()
    var data = NoteData.data
    fd.append('fichier', data.fichier[0])
    fd.append('courId', data.cour)
    fd.append('niveauId', data.niveau)
    fd.append('optionId', data.option)
    fd.append('headers', JSON.stringify(header))
    fd.append('anneeId', data.annee)
    fd.append('session', JSON.stringify(sessionExam))
    fd.append('importNow', importNow)
    return $http.post('api/notes/import', fd, {
      withCredentials: true,
      headers: {
        'Content-Type': undefined
      },
      transformRequest: angular.identity
    })
  }
})
