angular.module("notesApp.evaluations.controllers",[]).controller("EvaluationController", ["$scope", "$modal", "$log", "Evaluation",
function($scope, $modal, $log, Evaluation) {
	var deps = Evaluation.query(function() {
		$scope.departements = deps;
	});
	$scope.afficherFenetre = function(item) {
		var modelInstance = $modal.open({
			templateUrl : '/modules/evaluation/views/nouveau.html',
			controller : 'EvaluationFenetreController',
			controllerAs : 'depart',
			keyboard : true,
			backdrop : false,
			resolve : {
				element : function() {
					var tt;
					if(item)
					   tt = item;
					else
					   tt = new Evaluation();
					$log.log(tt);
					return tt;
				}
			}
		});
		modelInstance.result.then(function(item) {
			if (item.id) {
				item.$update(function() {
					var id;
					for (var i = 0; i < $scope.departements.length; i++) {
						if ($scope.departements[i].id == item.id) {
							id = i;
							break;
						}
					}
					if (id) {
						$scope.departements.splice(id, 1,item);
					}
				});
			} else {
				Evaluation.save(item, function() {
					$scope.departements.push(item);
				});
			}
		}, function() {

		});

	}
	$scope.supprimerEvaluation = function(item) {
		if (confirm("Voulez vous vraiment supprimer ce departement?")) {
			Evaluation.remove({
				id : item.id
			}, function() {
				var id;
				for (var i = 0; i < $scope.departements.length; i++) {
					if ($scope.departements[i].id == item.id) {
						id = i;
						break;
					}

				}
				if (id) {
					$scope.departements.splice(id, 1);
				}
			})
		}
	}
}]).controller("EvaluationFenetreController", ["$log","$scope", "$modalInstance", "element",
function($log,$scope, $modalInstance, element) {
	$scope.element = element;
	$log.log(element);
	$scope.valider = function() {
		$log.log("version ok");
		$modalInstance.close($scope.element);
	};

	$scope.cancel = function() {
		$log.log("version cancel");
		$modalInstance.dismiss("Cancel");
	};

}]);
