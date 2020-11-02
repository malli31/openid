app.controller('usersController', function($scope) {
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});

app.controller('openidController', function($scope, $http) {
	$scope.headingTitle = "Openid Information for user";
    $http.get('https://nunma09-e7440:8443/oauth').
    then(function(response) {
    	
        $scope.userData = response.data;
    });
});

