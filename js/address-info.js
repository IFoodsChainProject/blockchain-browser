var app = angular.module("myApp",['pascalprecht.translate','ngSanitize']);

app.config(['$translateProvider',
	function($translateProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: '/'+projectName+'/i18n/',
			suffix: '.json'
		});

		var lang = window.localStorage.getItem("lang")||'cn';
		$translateProvider.preferredLanguage(lang);
		$translateProvider.useSanitizeValueStrategy('escapeParameters');
	}]);

app.controller("address-info-Controller",function ($scope, $translate, $http) {

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

	var addressId = getQueryString('addressId')
	/**
	 * 地址详情
	 */
	$http({
		method: 'GET',
		url:   host + '/api/ifood/address/' + addressId,
	}).then(function successCallback(response) {
		if(response.data.code != "0000" || response.data.data==null) {
			console.log(response.data.msg)
			window.location.href='/'+projectName+'/html/404.html'
		}
		var data = response.data.data;
		$scope.addressInfo = data;

	}, function errorCallback(response) {
		console.log("tx info error");
	});


})