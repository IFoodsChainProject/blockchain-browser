var app = angular.module("myApp",['pascalprecht.translate','ngSanitize']);

app.config(['$translateProvider',
	function($translateProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: projectName + '/i18n/',
			suffix: '.json'
		});

		var lang = window.localStorage.getItem("lang")||'cn';
		$translateProvider.preferredLanguage(lang);
		$translateProvider.useSanitizeValueStrategy('escapeParameters');
	}]);

app.controller("search-Controller",function ($scope, $translate, $http) {

	$scope.langs = [{
		name: "English",
		lang: "cn"
	},
		{
			name: "中文",
			lang: "en"
		}];

	var lang = window.localStorage.getItem("lang");

	if( lang == "en"){
		$scope.langSelectIndex = 1;
	}else {
		$scope.langSelectIndex = 0;
	}

	$scope.changeLangSelectIndex = function() {
		if ($scope.langSelectIndex === 0) {
			$scope.langSelectIndex = 1;
		} else {
			$scope.langSelectIndex = 0;
		}
		$translate.use($scope.langs[$scope.langSelectIndex].lang);
		window.localStorage.setItem("lang",$scope.langs[$scope.langSelectIndex].lang);
		$("#top-navbar-1").removeClass("in");
	};

	/**
	 * search
	 * @type {string}
	 */
	var searchVar = window.location.hash;
	var index = searchVar .lastIndexOf("#");
	searchVar = searchVar .substring(index + 1, searchVar.length);
	searchVar = $.trim(searchVar)

	$http({
		method: 'POST',
		url:   host + '/api/ifood/search/' + searchVar,
	}).then(function successCallback(response) {
		if(response.data.code == "0003") {
			console.log(response.data.msg)
			window.location.href= '/html/searchNoFound.html'
			return;
		}
		if(response.data.code != "0000" || response.data.data==null || response.data.data == 0) {
			console.log(response.data.msg)
			window.location.href= '/html/404.html'
			return;
		}
		$scope.searchInfo = response.data.data;
	}, function errorCallback(response) {
		console.log("search error");
	});

	/**
	 * tx info by hash
	 * @param txHash
	 */
	$scope.getTxByHash=function(txHash) {
		window.location.href= '/html/tx-info.html?hash=' + txHash
	}

})