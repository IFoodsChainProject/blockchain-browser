var app = angular.module("myApp",['pascalprecht.translate','ngSanitize']);

/*var host = 'http://browser.ifoods.com';*/
/*var projectName = "ifoods-explorer-home"*/
/*var lang = window.localStorage.getItem("lang");*/

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

app.controller("block-info-Controller",function ($scope, $translate, $http) {

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

	var height = getQueryString('height')
	/**
	 * 高度详情
	 */
	$http({
		method: 'GET',
		url:   host + '/api/ifood/block/' + height,
	}).then(function successCallback(response) {
		if(response.data.code != "0000" || response.data.data==null) {
			console.log(response.data.msg)
			window.location.href= '/html/404.html'
		}
		var data = response.data.data;
		$scope.blockInfo = data;

	}, function errorCallback(response) {
		console.log("block info error");
	});

	/**
	 * tx info by hash
	 * @param txHash
	 */
	$scope.getTxByHash=function(txHash) {
		window.location.href= '/html/tx-info.html?hash=' + txHash
	}


})