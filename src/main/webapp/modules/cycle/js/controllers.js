angular.module("notesApp.cycles.controllers",[]).controller("CycleController", ["$scope", "$modal", "Cycle",
function($scope, $modal, Cycle) {
	var deps = Cycle.query(function() {
		$scope.cycles = deps;
	});
	$scope.afficherFenetre = function(item) {
		var modelInstance = $modal.open({
			templateUrl : '/modules/cycle/views/nouveau.html',
			controller : 'CycleFenetreController',
			controllerAs : 'depart',
			keyboard : true,
			backdrop : false,
			resolve : {
				element : function() {
					var tt;
					if(item)
					   tt = item;
					else
					   tt = new Cycle();
					return tt;
				}
			}
		});
		modelInstance.result.then(function(item) {
			if (item.id) {
				item.$update(function() {
					var id;
					for (var i = 0; i < $scope.cycles.length; i++) {
						if ($scope.cycles[i].id == item.id) {
							id = i;
							break;
						}
					}
					if (id) {
						$scope.cycles.splice(id, 1,item);
					}
				});
			} else {
				Cycle.save(item, function() {
					$scope.cycles.push(item);
				});
			}
		}, function() {

		});
	}
	$scope.supprimerCycle = function(item) {
		if (confirm("Voulez vous vraiment supprimer ce cycle?")) {
			Cycle.remove({
				id : item.id
			}, function() {
				var id;
				for (var i = 0; i < $scope.cycles.length; i++) {
					if ($scope.cycles[i].id == item.id) {
						id = i;
						break;
					}

				}
				if (id) {
					$scope.cycles.splice(id, 1);
				}
			})
		}
	}
}]).controller("CycleFenetreController", ["$log","$scope", "$modalInstance", "element",
function($log,$scope, $modalInstance, element) {
	$scope.element = element;
	$scope.valider = function() {
		$modalInstance.close($scope.element);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss("Cancel");
	};
}]);