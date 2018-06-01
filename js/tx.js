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

app.controller("tx-Controller",function ($scope, $translate, $http) {

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
			$(".disclaimerBtn").text("confirm").removeClass("disabled");
		} else {
			$scope.langSelectIndex = 0;
			$(".disclaimerBtn").text("已阅读").removeClass("disabled");
		}
		$translate.use($scope.langs[$scope.langSelectIndex].lang);
		window.localStorage.setItem("lang",$scope.langs[$scope.langSelectIndex].lang);
		$("#top-navbar-1").removeClass("in");
	};

	var currenttime = getQueryString("currenttime");
	currenttime = currenttime ? currenttime : Date.parse(new Date())/1000;
	var currentPage = getQueryString("pageNumber") ? getQueryString("pageNumber") : 1;
	currentPage = currentPage ? currentPage : 1;

	/**
	 * 交易列表
	 */
	/*var totalItems;
	var pageNum;*/
	$http({
		method: 'POST',
		url:   host + '/api/ifood/tx',
		data:{
			pageNumber: currentPage,
			currenttime: currenttime,
			excludeTxTypes: '0'
		},
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		transformRequest: function(obj) {
			var str = [];
			for(var p in obj){
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
			}
			return str.join("&");
		}
	}).then(function successCallback(response) {
		if(response.data.code != "0000" || response.data.data==null || response.data.data.length == 0) {
			console.log(response.data.msg)
			window.location.href= '/html/404.html'
		}
		var data = response.data.data;
		$scope.txList = data;

		/* page list*/
		var totaoNum = data[0].totalNum;
		var currentPage = currentPage ? currentPage : data[0].pageNum;
		var element = $('#paginationTxId');
		var options = {
			bootstrapMajorVersion: 3,
			currentPage: currentPage,
			numberOfPages: 7,
			totalPages: totaoNum/20+1,
			size: "large",
			alignment: "Center",
			itemTexts: function (type, page, current) {
				switch (type) {
					case "first":
						return "<<";
					case "prev":
						return "<";
					case "next":
						return ">";
					case "last":
						return ">>";
					case "page":
						return page;
				}
			}, onPageClicked: function (event, originalEvent, type, page) {
				window.location.href= "/html/tx.html?pageNumber="+page+"&currenttime="+currenttime;
			},
		};
		element.bootstrapPaginator(options);

	}, function errorCallback(response) {
		console.log("tx list error");
	});

	/**
	 * tx info by hash
	 * @param txHash
	 */
	$scope.getTxByHash=function(txHash) {
		window.location.href= '/html/tx-info.html?hash=' + txHash
	}

})

